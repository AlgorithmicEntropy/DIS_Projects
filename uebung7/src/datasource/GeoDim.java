package datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GeoDim {
    private int shopID;
    private String shopName;
    private String cityName;
    private String regionName;
    private String countryName;

    public GeoDim(int shopID, String shopName, String cityName, String regionName, String countryName) {
        this.shopID = shopID;
        this.shopName = shopName;
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
    }

    // Getters and setters
    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void store(Connection con) throws SQLException {
        try {
            String query = "INSERT INTO Geo (ShopID, ShopName, CityName, RegionName, CountryName) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, shopID);
            statement.setString(2, shopName);
            statement.setString(3, cityName);
            statement.setString(4, regionName);
            statement.setString(5, countryName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

