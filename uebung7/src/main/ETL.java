package main;

import datasource.SaleData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ETL {
    // FIXME CB Add more Logging so we can see what the thingy does.

    private static int saleId = 1;

    private Connection connectionDW;
    private Connection connectionDB;

    // Maps for speed up
    private final HashMap<String, Integer> shopMap = new HashMap<>();
    private final HashMap<String, Integer> articleMap = new HashMap<>();

    public static void main(String[] args) {
        ETL etl = new ETL();
        etl.doEtlStuff();
    }
    public void doEtlStuff() {
        connectionDB = ConnectionManager.getInstance().getDbCon();
        connectionDW = ConnectionManager.getInstance().getDwCon();

        // Copy all the tables - Order IS important since we have FK constraints
        List<String> tablesToCopy = List.of("Country", "Region", "City", "Shop", "ProductCategory", "ProductFamily", "ProductGroup", "Article");

        try {
            // fancy forEach did not work with the try/catch :(
            for (String table : tablesToCopy) {
                copyDataToDW(table);
            }
        } catch (SQLException ex) {
            System.out.println("Error on loading the data from the DB.");
            throw new RuntimeException(ex);
        }

        // Then AFTER that copy the sales, since those need the shops and the articles
        String filePath = "files" + File.separator + "sales.csv";
        try {
            loadSaleData(filePath);
        } catch (IOException e) {
            System.out.println("Error on loading the data from the file.");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Error on looking for FKs in the DW.");
            throw new RuntimeException(e);
        }
    }

    // This is somewhat disgusting in style with all the nesting, but should work. Not sure about the performance though.
    private void copyDataToDW(String tableName) throws SQLException {
        try (PreparedStatement loadStatement = connectionDB.prepareStatement("SELECT * FROM " + tableName)) {
            // Read all the data
            try (ResultSet resultSet = loadStatement.executeQuery()) {
                // See how many columns we got
                int colCount = resultSet.getMetaData().getColumnCount();

                int counter = 0;

                // Iterate through the results
                while (resultSet.next()) {
                    // Insert each result entry
                    insertFromResultSet(resultSet, tableName, colCount);

                    counter++;
                    if (counter % 500 == 0) {
                        System.out.println(counter + tableName + " entries loaded.");
                    }
                }
            }

        }

        System.out.println("All " + tableName + " entries loaded.");

    }

    private void insertFromResultSet(ResultSet result, String tableName, int colCount) throws SQLException {
        // Build the SQL in the style of "INSERT INTO TABLE VALUES (?,?,?) with a flexible number of ?
        String rawStatement = "Insert into " + tableName + " VALUES (" + "?,".repeat(colCount).replaceFirst(".$", "") + ")";
        try (PreparedStatement statement = connectionDW.prepareStatement(rawStatement)) {

            // Set the values for all the ? by using the value from the result set entry
            for (int i = 1; i <= colCount; i++) {
                statement.setObject(i, result.getObject(i));
            }

            // Do the insert
            statement.executeUpdate();
        }
    }

    private void loadSaleData(String dataFile) throws IOException, SQLException {
        int counter = 0;
        // precompile statement
        String rawSql = "Insert into SALES (SALESID, SALE_DATE, SHOPID, ArticleID, Sells, Revenue) VALUES (?,?,?,?,?,?)";
        PreparedStatement statement = connectionDW.prepareStatement(rawSql);
        var insertBuff = new ArrayList<SaleData>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Remove the first entry, assuming that's the captions for the columns
                if (counter == 0) {
                    counter++;
                    continue;
                }
                if (counter % 500 == 0) {
                    System.out.println(counter + " sale entries loaded.");
                }
                // convert entry into a data object
                try {
                    SaleData saleData = SaleData.parseSaleData(line);

                    // Find the foreign keys necessary for shop and article
                    saleData.shopID = findShopIdByName(saleData.shopName);
                    saleData.articleID = findArticleIdByName(saleData.articleName);

                    if (saleData.shopID == null) {
                        System.out.println("Error on finding the shop ID for: " + saleData.shopName);
                        continue;
                    }

                    if (saleData.articleID == null) {
                        System.out.println("Error on finding the article ID for: " + saleData.articleName);
                        continue;
                    }

                    insertBuff.add(saleData);

                    if (insertBuff.size() == 70) {
                        // Write that into the DW schema
                        insertSale(statement, insertBuff);
                        insertBuff.clear();
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Error on parsing the sale entry: " + line);
                }
                counter++;
            }
            // flush buffer
            insertSale(statement, insertBuff);

            System.out.println("CSV file read successfully.");
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        System.out.println("All sale entries loaded.");
    }

    private Integer findShopIdByName(String shopName) throws SQLException {
        var shopID = shopMap.get(shopName);
        if (shopID != null) {
            return shopID;
        }

        // cache miss -> ask db
        String rawSql = "SELECT SHOPID FROM Shop WHERE Name = ?";
        try (PreparedStatement statement = connectionDW.prepareStatement(rawSql)) {
            statement.setString(1, shopName);

            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int id = result.getInt("SHOPID");
                    if (!result.wasNull()) {
                        shopMap.put(shopName, id);
                        return id;
                    }
                }
            }
        }
        return null;
    }

    private Integer findArticleIdByName(String articleName) throws SQLException {
        var articleID = articleMap.get(articleName);
        if (articleID != null) {
            return articleID;
        }

        // cache miss -> ask db
        String rawSql = "SELECT ArticleID FROM Article WHERE Name = ?";
        try (PreparedStatement statement = connectionDW.prepareStatement(rawSql)) {
            statement.setString(1, articleName);

            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int id = result.getInt("ArticleID");
                    if (!result.wasNull()) {
                        articleMap.put(articleName, id);
                        return id;
                    }
                }
            }
        }
        return null;
    }

    private void insertSale(PreparedStatement statement, List<SaleData> sales) throws SQLException {
        for (var sale : sales) {
            statement.setInt(1, saleId);
            statement.setDate(2, new Date(sale.saleDate.getTime()));
            statement.setObject(3, sale.shopID);
            statement.setObject(4, sale.articleID);
            statement.setObject(5, sale.sold);
            statement.setBigDecimal(6, sale.revenue);
            saleId++;

            statement.addBatch();
        }
        statement.execute();
    }
}
