package main;

import datadw.Product;
import datadw.Sale;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInterface {

    public static void main(String[] args) throws SQLException {
        printProductTable(Granulates.GEO.CITY, Granulates.TIME.QUARTER, Granulates.PRODUCT.PRODUCT_GROUP);
    }

    /**
     * Produces output that the manager can use.
     * The desired granularity level
     * of each dimension is given by the parameters;
     * e.g. geo = "country" is the most general
     * and geo = "shop" is the most fine-grained
     * granularity level for the geographical dimension.
     *
     * @param geo
     * admissible values: shop, city, region, country
     * @param time
     * admissible values: date, day, month, quarter, year
     * @param productGranulate
     * admissible values: article, productGroup, productFamily, productCategory
     * @throws java.sql.SQLException
     */
    public static void printProductTable(Granulates.GEO geo, Granulates.TIME time, Granulates.PRODUCT productGranulate) throws SQLException {
        Connection connectionDW = ConnectionManager.getInstance().getDwCon();
        String sql = "Select g."+ geo.tableName + ", t." + time.tableName + ", p." + productGranulate.tableName +
                ", SUM(s.sells) as total_sells FROM sales s JOIN Time t ON s.date = t.date " +
                "JOIN Geo g ON s.ShopID = g.ShopID " +
                "JOIN product p ON s.articleid = p.articleid " +
                "GROUP BY t." + time.tableName + ", g." + geo.tableName + ", p." + productGranulate.tableName;
        var statement = connectionDW.prepareStatement(sql);
        var resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getInt(4));
        }
    }

    private void PrintTable(Granulates.GEO geo, Granulates.TIME time, Granulates.PRODUCT productGranulate) {
        var geos = GetGeos(geo);
        var times = GetTimes(time);
        var products = GetProducts(productGranulate);

        // Header
        System.out.println();

    }

    private List<String> GetGeos(Granulates.GEO geo) {
        var geos = new ArrayList<String>();
        var sql = "SELECT " + geo.tableName + " FROM geo";
        try (var statement = ConnectionManager.getInstance().getDwCon().prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                geos.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return geos;
    }

    private List<String> GetProducts(Granulates.PRODUCT product) {
        var products = new ArrayList<String>();
        var sql = "SELECT " + product.tableName + " FROM product";
        try (var statement = ConnectionManager.getInstance().getDwCon().prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    private List<String> GetTimes(Granulates.TIME time) {
        var times = new ArrayList<String>();
        var sql = "SELECT " + time.tableName + " FROM time";
        try (var statement = ConnectionManager.getInstance().getDwCon().prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                times.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return times;
    }
}
