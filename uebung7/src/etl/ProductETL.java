package etl;

import datasource.ProductDim;
import etl.db.Article;
import etl.db.Tuple;
import main.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ProductETL {
    private Connection conn;

    private final HashMap<Integer, Tuple<String, Integer>> productGroupMap = new HashMap<>();
    private final HashMap<Integer, Tuple<String, Integer>> productFamilyMap = new HashMap<>();
    private HashMap<Integer, String> productcategoryMap = new HashMap<>();

    public ProductETL() {
        this.conn = ConnectionManager.getInstance().getDbCon();
    }

    public ProductDim BuildProductDim(Article article) {
        var productGroup = GetProductGroup(article.productgroupid);
        var productFamily = GetProductFamily(productGroup.T2());
        var productCategory = GetProductCategory(productFamily.T2());
        var productDim = new ProductDim(article.articleID, article.name, productGroup.T1(), productFamily.T1(), productCategory, article.price);
        return productDim;
    }

    private Tuple<String,Integer> GetProductGroup(int id) {
        if (productGroupMap.containsKey(id)) {
            return productGroupMap.get(id);
        }
        else {
            var productGroup = GetproductGroupFromDB(id);
            productGroupMap.put(id, productGroup);
            return productGroup;
        }
    }

    private Tuple<String,Integer> GetproductGroupFromDB(int id) {
        String rawSql = "SELECT productfamilyid, name FROM productgroup WHERE productgroupid = ?";
        try (PreparedStatement statement = conn.prepareStatement(rawSql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int productfamilyid = result.getInt("productfamilyid");
                    String name = result.getString("name");
                    if (!result.wasNull()) {
                        return new Tuple<>(name, productfamilyid);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Tuple<String,Integer> GetProductFamily(int id) {
        if (productFamilyMap.containsKey(id)) {
            return productFamilyMap.get(id);
        }
        else {
            var region = GetProductFamilyFromDB(id);
            productFamilyMap.put(id, region);
            return region;
        }
    }

    private Tuple<String,Integer> GetProductFamilyFromDB(int id) {
        String rawSql = "SELECT productcategoryid, name FROM productfamily WHERE productfamilyid = ?";
        try (PreparedStatement statement = conn.prepareStatement(rawSql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int productcategoryid = result.getInt("productcategoryid");
                    String name = result.getString("name");
                    if (!result.wasNull()) {
                        return new Tuple<>(name, productcategoryid);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String GetProductCategory(int id) {
        if (productcategoryMap.containsKey(id)) {
            return productcategoryMap.get(id);
        }
        else {
            var productcategory = GetProductcategoryFromDB(id);
            productcategoryMap.put(id, productcategory);
            return productcategory;
        }
    }

    private String GetProductcategoryFromDB(int id) {
        String rawSql = "SELECT name FROM productcategory WHERE productcategoryid = ?";
        try (PreparedStatement statement = conn.prepareStatement(rawSql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    String name = result.getString("name");
                    if (!result.wasNull()) {
                        return name;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
