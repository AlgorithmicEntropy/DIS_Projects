package main;

import datasource.SaleData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;

public class ETL {
    // FIXME CB Add more Logging so we can see what the thingy does.

    private static int saleId = 1;

    private Connection connectionDW;
    private Connection connectionDB;


    public void doEtlStuff() {
        // FIXME CB Start Connections to postgres like we did in previous exercises

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
        Path filePath = new File("files" + File.separator + "sales.csv").toPath();
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

    private void loadSaleData(Path dataFile) throws IOException, SQLException {

        List<String> sales = Files.readAllLines(dataFile);

        // Remove the first entry, assuming that's the captions for the columns
        sales.remove(0);

        int counter = 0;

        for (String saleEntry : sales) {
            // convert entry into a data object
            SaleData saleData = SaleData.parseSaleData(saleEntry);

            // Find the foreign keys necessary for shop and article
            saleData.shopID = findShopIdByName(saleData.shopName);
            saleData.articleID = findShopIdByName(saleData.articleName);

            // Write that into the DW schema


            counter++;

            if (counter % 500 == 0) {
                System.out.println(counter + " sale entries loaded.");
            }
        }

        System.out.println("All sale entries loaded.");
    }

    private Integer findShopIdByName(String shopName) throws SQLException {
        String rawSql = "SELECT SHOPID FROM Shop WHERE Name = ?";
        try (PreparedStatement statement = connectionDW.prepareStatement(rawSql)) {
            statement.setString(1, shopName);

            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int id = result.getInt("SHOPID");
                    return result.wasNull() ? null : id;
                }
            }
        }
        return null;
    }

    private Integer findArticleIdByName(String articleName) throws SQLException {
        String rawSql = "SELECT ArticleID FROM Article WHERE Name = ?";
        try (PreparedStatement statement = connectionDW.prepareStatement(rawSql)) {
            statement.setString(1, articleName);

            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int id = result.getInt("ArticleID");
                    return result.wasNull() ? null : id;
                }
            }
        }
        return null;
    }

    private void insertSale(SaleData sale) throws SQLException {
        String rawSql = "Insert into SALES (SALESID, SALE_DATE, SHOPID, ArticleID, Sells, Revenue) VALUES ?,?,?,?,?,?)";
        try (PreparedStatement statement = connectionDW.prepareStatement(rawSql)) {
            statement.setInt(1, saleId);
            statement.setDate(2, new Date(sale.saleDate.getTime()));
            statement.setObject(3, sale.shopID);
            statement.setObject(4, sale.articleID);
            statement.setObject(5, sale.sold);
            statement.setBigDecimal(6, sale.revenue);

            statement.execute();
        }
    }

}
