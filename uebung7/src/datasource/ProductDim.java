package datasource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDim {
    private int articleID;
    private String articleName;
    private String productGroupName;
    private String productFamilyName;
    private String productCategoryName;
    private BigDecimal price;

    public ProductDim(int articleID, String articleName, String productGroupName, String productFamilyName, String productCategoryName, BigDecimal price) {
        this.articleID = articleID;
        this.articleName = articleName;
        this.productGroupName = productGroupName;
        this.productFamilyName = productFamilyName;
        this.productCategoryName = productCategoryName;
        this.price = price;
    }

    // Getters and setters
    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }

    public String getProductFamilyName() {
        return productFamilyName;
    }

    public void setProductFamilyName(String productFamilyName) {
        this.productFamilyName = productFamilyName;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void store(Connection con) throws SQLException {
        try {
            String query = "INSERT INTO Product (ArticleID, ArticleName, ProductGroupName, ProductFamilyName, ProductCategoryName, Price) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, articleID);
            statement.setString(2, articleName);
            statement.setString(3, productGroupName);
            statement.setString(4, productFamilyName);
            statement.setString(5, productCategoryName);
            statement.setBigDecimal(6, price);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
