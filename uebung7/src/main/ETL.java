package main;

import datadw.Sale;
import datasource.SaleData;
import datasource.TimeDim;
import etl.ProductETL;
import etl.ShopETL;
import etl.db.Article;
import etl.db.Tuple;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ETL {
    private Connection connectionDW;
    private Connection connectionDB;

    private final ShopETL shopETL = new ShopETL();
    private final ProductETL productETL = new ProductETL();

    // Maps for speed up
    private final HashMap<String, Tuple<Integer, Integer>> shopMap = new HashMap<>();
    private final HashMap<String, Article> articleMap = new HashMap<>();
    private final ArrayList<Integer> geoDimList = new ArrayList<>();
    private final ArrayList<Integer> productDimList = new ArrayList<>();
    private final ArrayList<java.util.Date> timeDimList = new ArrayList<>();

    public static void main(String[] args) {
        ETL etl = new ETL();
        etl.doEtlStuff();
    }
    public void doEtlStuff() {
        connectionDB = ConnectionManager.getInstance().getDbCon();
        connectionDW = ConnectionManager.getInstance().getDwCon();

        String filePath = "files" + File.separator + "sales.csv";
        try {
            loadSaleData(filePath);
        } catch (IOException e) {
            System.out.println("Error on loading the data from the file.");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSaleData(String dataFile) throws IOException, SQLException {
        int counter = 0;
        // precompile statement
        String rawSql = "Insert into SALES (DATE, SHOPID, ArticleID, Sells, Revenue) VALUES (?,?,?,?,?)";
        PreparedStatement statement = connectionDW.prepareStatement(rawSql);
        var insertBuff = new ArrayList<Sale>();

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
                    var shop = findShopIdByName(saleData.shopName);
                    Article article = findArticleIdByName(saleData.articleName);

                    if (shop == null) {
                        System.out.println("Error on finding the shop ID for: " + saleData.shopName);
                        continue;
                    }

                    if (article == null) {
                        System.out.println("Error on finding the article ID for: " + saleData.articleName);
                        continue;
                    }

                    // build dimensions
                    var geoDim = shopETL.BuildGeoDim(shop.T1(), saleData.shopName, shop.T2());
                    var timeDim = new TimeDim(saleData.saleDate);
                    var productDim = productETL.BuildProductDim(article);

                    if (!geoDimList.contains(geoDim.getShopID())) {
                        geoDimList.add(geoDim.getShopID());
                        geoDim.store(connectionDW);
                    }

                    if (!productDimList.contains(productDim.getArticleID())) {
                        productDimList.add(productDim.getArticleID());
                        productDim.store(connectionDW);
                    }

                    if (!timeDimList.contains(timeDim.getDate())) {
                        timeDimList.add(timeDim.getDate());
                        timeDim.store(connectionDW);
                    }

                    // build sale
                    var sale = new Sale(timeDim.getDate(), geoDim.getShopID(), productDim.getArticleID(), saleData.sold, saleData.revenue);

                    insertBuff.add(sale);
                    if (insertBuff.size() >= 70) {
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

    private Tuple<Integer, Integer> findShopIdByName(String shopName) throws SQLException {
        var shopID = shopMap.get(shopName);
        if (shopID != null) {
            return shopID;
        }

        // cache miss -> ask db
        String rawSql = "SELECT SHOPID, cityid FROM Shop WHERE Name = ?";
        try (PreparedStatement statement = connectionDB.prepareStatement(rawSql)) {
            statement.setString(1, shopName);

            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int id = result.getInt("SHOPID");
                    int cityid = result.getInt("cityid");
                    if (!result.wasNull()) {
                        var t = new Tuple<>(id, cityid);
                        shopMap.put(shopName, t);
                        return t;
                    }
                }
            }
        }
        return null;
    }

    private Article findArticleIdByName(String articleName) throws SQLException {
        var articleID = articleMap.get(articleName);
        if (articleID != null) {
            return articleID;
        }

        // cache miss -> ask db
        String rawSql = "SELECT * FROM article WHERE name = ?";
        try (PreparedStatement statement = connectionDB.prepareStatement(rawSql)) {
            statement.setString(1, articleName);

            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int id = result.getInt("articleid");
                    int productgroupid = result.getInt("productgroupid");
                    BigDecimal price = result.getBigDecimal("price");
                    var article = new Article();
                    article.articleID = id;
                    article.name = articleName;
                    article.price = price;
                    article.productgroupid = productgroupid;
                    if (!result.wasNull()) {
                        articleMap.put(articleName, article);
                        return article;
                    }
                }
            }
        }
        return null;
    }

    private void insertSale(PreparedStatement statement, List<Sale> sales) throws SQLException {
        for (var sale : sales) {
            statement.setDate(1, new Date(sale.date.getTime()));
            statement.setObject(2, sale.shopID);
            statement.setObject(3, sale.articleID);
            statement.setObject(4, sale.sold);
            statement.setBigDecimal(5, sale.revenue);

            statement.addBatch();
        }
        statement.execute();
    }
}
