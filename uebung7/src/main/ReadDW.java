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
        String rawSql = "SELECT SALES.*,  FROM SALES " + getProductJoinsForSales() + " WHERE " + productGranulate.tableName + "ID = ?" ;


        // FIXME CB Same join thing for GEO - if that is even the right approach...

        try(PreparedStatement statement = connectionDW.prepareStatement(rawSql)){
            statement.setInt(1, product.id);

            List<Sale> sales = new ArrayList<>();

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    Sale sale = new Sale();
                    // FIXME CB fill up the attributes!
                    // GEO is depending on the geoGranulate. (geoGranulate.tableName + ".name")
                }
            }

            return sales;
        }
    }

    private static String getProductJoinsForSales() {
        return " LEFT OUTER JOIN ARTICLE ON SALES.ARTICLEID = ARTICLE.ARTICLEID"
               +  " LEFT OUTER JOIN ProductGroup ON  ARTICLE.ProductGroupID = ProductGroup.ProductGroupID"
                +  " LEFT OUTER JOIN ProductFamily ON  ProductGroup.ProductFamilyID = ProductFamily.ProductFamilyID"
                +  " LEFT OUTER JOIN ProductCategory ON  ProductFamily.ProductFamilyID = ProductCategory.ProductCategoryID";
    }
}
