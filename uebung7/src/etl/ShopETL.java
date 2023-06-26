package etl;

import datasource.GeoDim;
import etl.db.Tuple;
import main.ConnectionManager;

import java.sql.*;
import java.util.HashMap;

public class ShopETL {
    private final HashMap<Integer, Tuple<String,Integer>> cityMap = new HashMap<>();
    private final HashMap<Integer, Tuple<String,Integer>> regionMap = new HashMap<>();
    private final HashMap<Integer, String> countryMap = new HashMap<>();
    private final Connection conn;

    public ShopETL() {
        this.conn = ConnectionManager.getInstance().getDbCon();
    }
    
    public GeoDim BuildGeoDim(int shopID, String shopName, int cityID) {
        var city = GetCity(cityID);
        var region = GetRegion(city.T2());
        var country = GetCountry(region.T2());
        var geoDim = new GeoDim(shopID, shopName, city.T1(), region.T1(), country);
        return geoDim;
    }
    
    private Tuple<String,Integer> GetCity(int id) {
        if (cityMap.containsKey(id)) {
            return cityMap.get(id);
        }
        else {
            var city = GetCityFromDB(id);
            cityMap.put(id, city);
            return city;
        }
    }

    private Tuple<String,Integer> GetCityFromDB(int id) {
        String rawSql = "SELECT regionid, name FROM city WHERE cityid = ?";
        try (PreparedStatement statement = conn.prepareStatement(rawSql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int region = result.getInt("regionid");
                    String name = result.getString("name");
                    if (!result.wasNull()) {
                        return new Tuple<>(name, region);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    
    private Tuple<String,Integer> GetRegion(int id) {
        if (regionMap.containsKey(id)) {
            return regionMap.get(id);
        }
        else {
            var region = GetRegionFromDB(id);
            regionMap.put(id, region);
            return region;
        }
    }

    private Tuple<String,Integer> GetRegionFromDB(int id) {
        String rawSql = "SELECT countryid, name FROM region WHERE regionid = ?";
        try (PreparedStatement statement = conn.prepareStatement(rawSql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                // Move the cursor to the first row, if there is one
                if (result.next()) {
                    int countryid = result.getInt("countryid");
                    String name = result.getString("name");
                    if (!result.wasNull()) {
                        return new Tuple<>(name, countryid);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    
    private String GetCountry(int id) {
        if (countryMap.containsKey(id)) {
            return countryMap.get(id);
        }
        else {
            var country = GetCountryFromDB(id);
            countryMap.put(id, country);
            return country;
        }
    }

    private String GetCountryFromDB(int id) {
        String rawSql = "SELECT name FROM country WHERE countryid = ?";
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
