package datasource;

public class SalesFact {
    private int salesID;
    private TimeDim dateID;
    private GeoDim shopID;
    private ProductDim articleID;
    private int sells;
    private double revenue;

    public SalesFact(int salesID, TimeDim dateID, GeoDim shopID, ProductDim articleID, int sells, double revenue) {
        this.salesID = salesID;
        this.dateID = dateID;
        this.shopID = shopID;
        this.articleID = articleID;
        this.sells = sells;
        this.revenue = revenue;
    }

    // Getters and setters
    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public TimeDim getDateID() {
        return dateID;
    }

    public void setDateID(TimeDim dateID) {
        this.dateID = dateID;
    }

    public GeoDim getShopID() {
        return shopID;
    }

    public void setShopID(GeoDim shopID) {
        this.shopID = shopID;
    }

    public ProductDim getArticleID() {
        return articleID;
    }

    public void setArticleID(ProductDim articleID) {
        this.articleID = articleID;
    }

    public int getSells() {
        return sells;
    }

    public void setSells(int sells) {
        this.sells = sells;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
