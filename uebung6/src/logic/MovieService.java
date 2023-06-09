package logic;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.orderBy;
import static logic.Movie.*;
import static logic.Tweet.COL_GEOTAGGED;

/**
 * This class holds the data/backend logic for the Movie Web-App. It uses
 * MongoDB to perform different kinds of queries.
 */
public class MovieService extends MovieServiceBase {

    public static final String INDEX_TYPE_TEXT = "text";
    public static final String OPERATOR_EXISTS = "$exists";
    public static final String FS_FILTER_FILENAME = "filename";
    private MongoClient mongo;
    private MongoDatabase db;
    private MongoCollection<Document> movies;
    private MongoCollection<Document> tweets;
    private GridFSBucket fs;

    /**
     * Create a new MovieService by connecting to MongoDB.
     */
    public MovieService() {
        // see for example https://mongodb.github.io/mongo-java-driver/3.12/driver/tutorials/
        mongo = MongoClients.create("mongodb://root:password@localhost");
        // Select database "imdb"
        db = mongo.getDatabase("imbd");
        // Create a GriFS FileSystem Object using the db
        fs = GridFSBuckets.create(db);
        createSampleImage();
        // Print the name of all collections in that database
        printCollections();

        // Take "movies" and "tweets" collection
        movies = db.getCollection("movies");
        tweets = db.getCollection("tweets");

        // If database isn't filled (has less than 1000 documents) delete
        // everything and fill it
        if (tweets.countDocuments() < 1000) {
            createMovieData();
        }
    }


    /**
     * Find a movie by title. Only return one match.
     *
     * @param title the title to query
     * @return the matching DBObject
     */
    public Document findMovieByTitle(String title) {
        // exclude no field, so that every field is included
        return movies.find(eq(COL_TITLE, title)).first();
    }


    /**
     * Find the best movies, i.e. those that have a rating greater minRating and
     * at least minVotes votes.
     * <p>
     * db.movies.find({votes: {$gt: minVotes}, rating: {$gt: minRating}}).sort({votes: -1, rating: -1}).limit(limit)
     *
     * @param minVotes  number of votes required at least
     * @param minRating rating required at least
     * @param limit     maximum number of records to be returned
     * @return the FindIterable for the query
     */
    public FindIterable<Document> getBestMovies(int minVotes, double minRating, int limit) {
        return movies.find(and(gt(COL_VOTES, minVotes), gt(COL_RATING, minRating)))
                .sort(orderBy(Sorts.descending(COL_VOTES), Sorts.descending(COL_RATING))).limit(limit);
    }

    /**
     * Find movies by genres. To achieve that, find all movies whose "genre"
     * property contains all of the specified genres.
     *
     * @param genreList comma-separated genres
     * @param limit     maximum number of records to be returned
     * @return the FindIterable for the query
     */
    public FindIterable<Document> getByGenre(String genreList, int limit) {
        List<String> genres = Arrays.asList(genreList.split(","));
        // strip leading and trailing spaces from each genre
        genres.replaceAll(String::trim);
        return movies.find(all(COL_GENRE, genres)).limit(limit);
    }

    /**
     * Find movies by prefix, i.e. find movies whose "title" property begins
     * with the given prefix. Use a regular expression similar to the
     * {@link #suggest(String, int)} method. This method is used to display
     * search results while typing.
     *
     * @param titlePrefix the prefix entered by the user
     * @param limit       maximum number of records to be returned
     * @return the FindIterable for the query
     */
    public FindIterable<Document> searchByPrefix(String titlePrefix, int limit) {
        Document prefixQuery = new Document(COL_TITLE, Pattern.compile("^" + titlePrefix + ".*"));
        return movies.find(prefixQuery).limit(limit);
    }

    /**
     * Find all movies that have a "tweets" attribute, i.e. that were at least
     * once subject of a tweet.
     *
     * @return the FindIterable for the query
     */
    public FindIterable getTweetedMovies() {
        FindIterable<Document> result = movies.find(new Document(COL_TWEETS, new Document(OPERATOR_EXISTS, true)));
        return result;
    }

    /**
     * Saves a changed comment for a movie, which is displayed in the movies
     * details under the search tab.
     *
     * @param id      the movie _id of the move where the comment was set.
     * @param comment the comment to save
     */
    public void saveMovieComment(String id, String comment) {
        // Code fragment from project:
//        Document query = new Document(Movie.COL_ID, id);
//        Document update = new Document(COL_COMMENT, comment);
//        movies.updateOne(query, update);

        // Easier to read:
        movies.updateOne(eq(Movie.COL_ID, id), Updates.set(COL_COMMENT, comment));
    }


    /**
     * Find all Movies, that have tweets that contain a given keyword. Solve
     * this using a Regular Expression like this:
     *
     * <pre>
     * .*keyword.*
     * </pre>
     * <p>
     * Regular expressions can be created in Java using the Pattern.compile
     * method. To make the search case insensitive specify the according
     * parameter for the pattern.<br>
     *
     * <b>Remember</b>: This is an example of a powerful query that should not
     * be done in practice. Wildcard regular expression queries always have to
     * scan the full index which is very costly. A full text search is far more
     * efficient (see {@link #searchTweets(String)} for an example) and would be
     * preferred in practice.
     *
     * @param keyword the keyword to search
     * @param limit   maximum number of records to be returned
     * @return the FindIterable for the query
     */
    public FindIterable<Document> getByTweetsKeywordRegex(String keyword, int limit) {
        FindIterable<Document> result = movies.find(Filters.regex(COL_TWEETS_TEXT, ".*" + keyword + ".*", "i")).limit(limit);
        return result;
    }

    /**
     * Does Full Text Search (FTS) on Tweets.
     * <p>
     * FTS search is powerful new feature of MongoDB. It allows Queries similar
     * to those performed in Google, including negation, Stemming, Stop Words
     * and Wildcards. To Do FTS first ensure an index of type "text" on the
     * tweet property "text".
     *
     * @param query the search query
     * @return a List of Objects returned by the FTS query
     */
    public FindIterable<Document> searchTweets(String query) {
        return tweets.find(text(query));
    }

    /**
     * Find the newest tweets, with respect to their insertion order. To achieve
     * this, order the results by their "_id" attribute, descending (-1).
     *
     * @param limit maximum number of records to be returned
     * @return the FindIterable for the query
     */
    public FindIterable<Document> getNewestTweets(int limit) {
        return tweets.find().sort(Sorts.descending(Tweet.COL_ID)).limit(limit);
    }

    /**
     * Find all tweets that are geotagged, i.e. have a "coordinates" attribute
     * that is neither null nor non-existent.
     *
     * @param limit maximum number of records to be returned
     * @return the FindIterable for the query
     */
    // FIXME cb 11.06.2023 What is the difference supposed to be between this and getTaggedTweets?!?
    public FindIterable<Document> getGeotaggedTweets(int limit) {
        return tweets.find(new Document(COL_GEOTAGGED, new Document(OPERATOR_EXISTS, true))).limit(limit);
    }

    // GridFS Interaction

    /**
     * Saves a file to GridFS. The file has the given name and is filled using
     * the provided InputStream. The given Content-Type has to be set on the
     * file.
     *
     * @param name        name that the file will have
     * @param inputStream input stream used to fill the file
     * @param contentType content type that the file will have
     */
    public void saveFile(String name, InputStream inputStream, String contentType) {
        GridFSUploadOptions options = new GridFSUploadOptions().chunkSizeBytes(358400).metadata(new Document("contentType", contentType));
        ObjectId fileId = fs.uploadFromStream(name, inputStream, options);
    }

    /**
     * Retrieves a file from GridFS. If the file is not found (==null) the file
     * with the name "sample.png" should be loaded instead.
     *
     * @param name the name of the file
     * @return The retrieved GridFS File
     */
    public GridFSFile getFile(String name) {
        GridFSFile file = fs.find(eq(FS_FILTER_FILENAME, name)).first();
        if (file == null) {
            file = fs.find(eq(FS_FILTER_FILENAME, "sample.png")).first();
        }
        return file;
    }


    // === already implemented methods ===

    /**
     * Suggest movies based on a title prefix. This is used for the search
     * typeahead feature. The suggestions rely on a regular expression of the
     * form:
     *
     * <pre>
     * ^prefix.*
     * </pre>
     * <p>
     * Anchoring the prefix to the begin using "^" ensures the efficiency of the
     * query. Perform a projection to the property "title", so no unnecessary
     * data is transferred from MongoDB.
     *
     * @param prefix
     * @param limit  maximum number of records to be returned
     * @return the FindIterable for the query
     */
    public FindIterable<Document> suggest(String prefix, int limit) {
        Document query = new Document(COL_TITLE, Pattern.compile("^" + prefix + ".*"));
        Document projection = new Document(COL_TITLE, true);
        FindIterable<Document> suggestions = movies.find(query).projection(projection).limit(limit);
        return suggestions;
    }

    /**
     * Find all tweets that are geotagged, i.e. that have a "coordinates"
     * attribute. Make sure that the "coordinates" attribute is indexed, so that
     * this query is efficient. Furthermore perform a projection to the
     * attributes "text", "movie", "user.name", "coordinates". The result is
     * used to display markers on the map and the projection ensures, that no
     * unnecessary data is transfered. The tweets should be order by the
     * descending (-1) "_id" property so, the newest tweets are returned first.
     *
     * @return the FindIterable for the query
     */
    public FindIterable<Document> getTaggedTweets() {
        tweets.createIndex(new Document("coordinates", 1));

        Document projection = new Document("text", true).append("movie", true).append("user.name", true)
                .append("coordinates", true);
        Document query = new Document("coordinates", new Document("$exists", true));
        Document sort = new Document("id", -1);
        FindIterable<Document> results = tweets.find(query).projection(projection).sort(sort);
        return results;
    }

    /**
     * Do a geospatial query to find all tweets in the given radius for a
     * specified point. The syntax of such a query is the following:
     *
     * <pre>
     * db.*collection*.find({ *location field* :
     *                          { $near :
     *                             { $geometry :
     *                                 { type : "Point" ,
     *                                   coordinates : [ *lng* , *lat*] } },
     *                               $maxDistance : *distance in meters*
     *        } } )
     * </pre>
     * <p>
     * Details on geospatial querys are documented online:
     * http://docs.mongodb.org/manual/reference/operator/near/#op._S_near Note
     * that the Radius parameter is given in km while the query takes a distance
     * in meters.
     *
     * @param lat      the latitude of the center point
     * @param lng      the longitude of the center point
     * @param radiusKm the radius to search in
     * @return
     */
    public FindIterable<Document> getTweetsNear(double lat, double lng, int radiusKm) {
        tweets.createIndex(Indexes.geo2dsphere("coordinates"));

        Document pointQuery = new Document("coordinates", new Document("$near",
                new Document("$geometry", new Document("type", "Point").append("coordinates", new Double[]{
                        lng, lat})).append("$maxDistance", radiusKm * 1000)));
        return tweets.find(pointQuery);
    }
    // GridFS Interaction

    /**
     * Load the sample.png and store it in the database. The content type has to
     * be set, so the file can be retrieved and displayed by web clients.
     */
    public void createSampleImage() {
        // Create file
        // fs.drop();
        GridFSFile file = fs.find(new Document("filename", "sample.png")).first();
        if (file == null) {
            InputStream streamToUploadFrom = MovieService.class.getResourceAsStream("/data/sample.png");
            saveFile("sample.png", streamToUploadFrom, "image/png");
        }
    }

    public void downloadFile(GridFSFile file, OutputStream outputStream) {
        fs.downloadToStream(file.getId(), outputStream);
    }

    /**
     * Find all movies that have at least one tweet in their "tweets" array
     * which does have the "coordinates" attribute
     *
     * @return the FindIterable for the query
     */
    public FindIterable<Document> getViewableMovies() {
        FindIterable results = movies.find(new BasicDBObject("tweets.coordinates", new Document("$exists", true)));
        return results;
    }


    // Given Helper Functions:

    /**
     * Fill the database using the data files in the data directory
     */
    public void createMovieData() {
        clearDatabase();
        // Load a CSV file of IMDB titles into the database
        // List<DBObject> data =
        // loadMovies_megaNice("/data/imdb_megaNice-full.csv");
        // Smaller Dataset with less properties:
        // List<DBObject> data = loadMovies("/data/imdb_nicer-full.csv");
        loadJSON("/data/movies.json", movies);
        loadJSON("/data/tweets.json", tweets);

        // Index Movie attributes title, rating, votes, tweets.coordinates
        movies.createIndex(Indexes.ascending("title"));
        movies.createIndex(Indexes.ascending("rating"));
        movies.createIndex(Indexes.ascending("votes"));
        movies.createIndex(Indexes.ascending("tweets.coordinates"));

        // Index tweets.text for full text search
        tweets.createIndex(Indexes.text("text"));
    }

    public void upsertMovie(Document movie) {
        if (movies.count(eq("_id", movie.getString("_id"))) <= 0) {
            movies.insertOne(movie);
        } else {
            //System.out.println(movie.get("title") + "  already exists");
        }
    }

    /**
     * Get the movie collection
     *
     * @return the movie collection
     */
    public MongoCollection<Document> getMovies() {
        return movies;
    }

    /**
     * Get the tweets collection
     *
     * @return the tweets collection
     */
    public MongoCollection<Document> getTweets() {
        return tweets;
    }

    /**
     * Delete all documents from both the tweets and the movie collection
     */
    public void clearDatabase() {
        movies.deleteMany(new Document());
        tweets.deleteMany(new Document());
    }

    /**
     * Bulk insert tweets
     *
     * @param movie
     * @param stati
     */
    public void saveTweets(String movie, List<Status> stati) {
        for (Status status : stati) {
            saveTweet(movie, status);
        }
    }

    /**
     * Save a tweet emitted by the Twitter Stream. The tweet has to be saved
     * twice: 1) in the movie document that has a title that matches the keyword
     * using the "tweets" list of each movie. 2) in the separate tweets
     * collection which stores the JSON tweets, as outputted by the Twitter REST
     * API.<br>
     * Add the matching movie to the tweets in the tweet collection by adding a
     * new field "movie" to it. Also remove the "coordinates" property of the
     * raw tweets where that property is null. This ensure that we can uses it
     * for geospatial queries.
     *
     * @param movie  the name of the movie the tweet corresponds to
     * @param status the tweet
     */
    public void saveTweet(String movie, Status status) {
        // Extract information from tweet
        String user = status.getUser().getName();
        String text = status.getText();
        Date date = status.getCreatedAt();
        boolean retweet = status.isRetweet();

        // Output the Tweet
        System.out.format("%-20s %-20s %-140s%n", movie, user, text.replace("\n", " "));

        // Get raw JSON Tweet
        String rawJson = TwitterObjectFactory.getRawJSON(status);
        Document rawTweet = Document.parse(rawJson);
        rawTweet.put("movie", movie);
        if (status.getGeoLocation() == null) {
            rawTweet.remove("coordinates");
        }

        Document tweet = new Document().append("user", user).append("text", text).append("retweet", retweet)
                .append("date", date);

        // Add coordinates if the tweet is geotagged
        if (status.getGeoLocation() != null) {
            Double lat = status.getGeoLocation().getLatitude();
            Double lng = status.getGeoLocation().getLongitude();
            tweet.append("coordinates", Arrays.asList(new Double[]{lat, lng}));
        } else if (status.getPlace() != null && status.getPlace().getBoundingBoxCoordinates() != null) {
            // If the tweet isn't explicitly tagged, try to take the user's
            // position
            GeoLocation gl = status.getPlace().getBoundingBoxCoordinates()[0][0];
            Double lat = gl.getLatitude();
            Double lng = gl.getLongitude();
            tweet.append("coordinates", Arrays.asList(new Double[]{lat, lng}));
        }

        // Insert Raw Tweet
        tweets.insertOne(rawTweet);
        // Find matching Movie(s) and append Tweet
        movies.updateMany(new Document("title", movie),
                new Document("$push", new Document("tweets", tweet)), new UpdateOptions().upsert(true));
    }

    /**
     * Output all Collections known to the database.
     */
    public void printCollections() {
        MongoIterable<String> colls = db.listCollectionNames();
        System.out.println("Connected to MongoDB\nCollections in imdb db: ");
        for (String s : colls) {
            System.out.println("- " + s);
        }
    }
}
