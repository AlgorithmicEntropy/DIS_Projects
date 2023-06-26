package main;

import datadw.Product;
import datadw.Sale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadDW {

    public static List<Product> findAllProducts(Connection connectionDW, Granulates.PRODUCT granulate) throws SQLException {
        // Find all the products categories/families/groups/articles, depending on the granulate
        String rawSql = "SELECT * FROM " + granulate.tableName;
        try(PreparedStatement statement = connectionDW.prepareStatement(rawSql)){

            List<Product> products = new ArrayList<>();

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    Product product = new Product();
                    product.id = resultSet.getInt(granulate.tableName + "ID");
                    product.name = resultSet.getString("Name");
                    products.add(product);
                }
            }

            return products;
        }
    }

    public static List<Sale> findAllSalesByProduct(Connection connectionDW, Granulates.PRODUCT productGranulate, Granulates.GEO geoGranulate, Product product) throws SQLException {
        // Find all the products categories/families/groups/articles, depending on the granulate
        String rawSql = "SELECT SALES.* FROM SALES " + getProductJoinsForSales() + getGeoJoinsForSales() + " WHERE " + productGranulate.tableName + "." + productGranulate.tableName + "ID = ? AND " + geoGranulate.tableName + "." + geoGranulate.tableName + "ID = ?";
        PreparedStatement statement = connectionDW.prepareStatement(rawSql);

        statement.setInt(1, product.id);
        //statement.setInt(2, product);

        List<Sale> sales = new ArrayList<>();

        try(ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                sales.add(Sale.fromResultSet(resultSet));
                // GEO is depending on the geoGranulate. (geoGranulate.tableName + ".name")
            }
        }
        return sales;
    }

    private static String getProductJoinsForSales() {
        return " LEFT OUTER JOIN ARTICLE ON SALES.ARTICLEID = ARTICLE.ARTICLEID"
                + " LEFT OUTER JOIN ProductGroup ON ARTICLE.ProductGroupID = ProductGroup.ProductGroupID"
                + " LEFT OUTER JOIN ProductFamily ON ProductGroup.ProductFamilyID = ProductFamily.ProductFamilyID"
                + " LEFT OUTER JOIN ProductCategory ON ProductFamily.ProductFamilyID = ProductCategory.ProductCategoryID";
    }

    private static String getGeoJoinsForSales() {
        return " LEFT OUTER JOIN SHOP ON SALES.SHOPID = SHOP.SHOPID"
                + " LEFT OUTER JOIN CITY ON SHOP.CITYID = CITY.CITYID"
                + " LEFT OUTER JOIN REGION ON CITY.REGIONID = REGION.REGIONID"
                + " LEFT OUTER JOIN COUNTRY ON REGION.COUNTRYID = COUNTRY.COUNTRYID";
    }

}
