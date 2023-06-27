package datadw;

import main.ConnectionManager;
import main.Granulates;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private final Granulates.GEO geo;
    private final Granulates.TIME time;
    private final Granulates.PRODUCT productGranulate;

    private final Connection conn;

    public Table(Granulates.GEO geo, Granulates.TIME time, Granulates.PRODUCT productGranulate) {
        this.geo = geo;
        this.time = time;
        this.productGranulate = productGranulate;
        conn = ConnectionManager.getInstance().getDwCon();
    }

    public void PrintTable() {
        var geos = GetGeos(geo);
        var times = GetTimes(time);
        var products = GetProducts(productGranulate);
        var totals = GetTotals();

        // add total column
        products.add("total");

        // column map for padding
        var colMap = new HashMap<String, Integer>();
        for (var p : products) {
            colMap.put(p, p.length());
        }

        var rowBuilder = new StringBuilder();

        // header
        // get length for geo column
        int geo_col_l = (int)Math.ceil((double) geos.stream().mapToInt(String::length).max().orElse(0) / 4) + 1;
        int sales_col_l = ((times.get(0).length() + time.tableName.length()) / 4);
        System.out.print("|");
        System.out.print("\t".repeat(geo_col_l));
        System.out.print("|\tsales" + "\t".repeat(sales_col_l+1));
        for (var p: products) {
            System.out.print("|\t"+p+"\t");
        }
        System.out.println();
        PrintHSep();

        // geo granule
        for (var g : geos) {
            // get geo total
            var geoTotals = GetGeoTotals(g);
            System.out.print("|\t" + g + " ".repeat(Math.max(0, (geo_col_l-1) * 4 - g.length())));
            int times_c = 0;
            for (var t : times) {
                rowBuilder = new StringBuilder();
                if (times_c > 0)
                {
                    rowBuilder.append("|");
                    rowBuilder.append("\t".repeat(geo_col_l));
                }
                var rowMap = BuildRowMap(g, t);

                rowBuilder.append("|\t").append(time.tableName).append(" ").append(t);
                // padding
                rowBuilder.append(" ".repeat(Math.max(0, (time.tableName.length() + t.length()) - sales_col_l)));
                for (var p : products) {
                    var val = rowMap.getOrDefault(p, 0);
                    rowBuilder.append("|\t").append(val);
                    // padding
                    rowBuilder.append(" ".repeat(Math.max(0, colMap.get(p) - val.toString().length())));
                    rowBuilder.append("\t");
                }
                System.out.println(rowBuilder);
                times_c++;
            }
            // geo total
            rowBuilder = new StringBuilder();
            rowBuilder.append("|");
            rowBuilder.append("\t".repeat(geo_col_l));
            rowBuilder.append("|");
            rowBuilder.append("\ttotal\t\t");
            for (var p : products) {
                var val = geoTotals.getOrDefault(p, 0);
                rowBuilder.append("|\t").append(val);
                // padding
                rowBuilder.append(" ".repeat(Math.max(0, colMap.get(p) - val.toString().length())));
                rowBuilder.append("\t");
            }
            System.out.println(rowBuilder);
            // sep
            PrintHSep();
        }
        // totals
        rowBuilder = new StringBuilder();
        rowBuilder.append("|");
        rowBuilder.append("\t".repeat(geo_col_l));
        rowBuilder.append("|");
        rowBuilder.append("\ttotal\t\t");
        for (var p : products) {
            var val = totals.getOrDefault(p, 0);
            rowBuilder.append("|\t").append(val);
            // padding
            rowBuilder.append(" ".repeat(Math.max(0, colMap.get(p) - val.toString().length())));
            rowBuilder.append("\t");
        }
        System.out.println(rowBuilder);
    }

    private void PrintHSep()
    {
        System.out.print("--".repeat(100));
        System.out.println();
    }

    private HashMap<String, Integer> GetTotals() {
        var row = new HashMap<String, Integer>();
        var sql = "select " + productGranulate.tableName + ", sum(s.sells) as sold from sales s join time t on s.date = t.date join geo g on s.shopid = " +
                "g.shopid JOIN product p ON s.articleid = p.articleid group by p." + productGranulate.tableName;
        try (var statement = conn.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                row.put(resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        var sql_total = "select sum(s.sells) as sold from sales s join time t on s.date = t.date join geo g on s.shopid = " +
                "g.shopid JOIN product p ON s.articleid = p.articleid";
        try (var statement = conn.prepareStatement(sql_total)) {
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                row.put("total", resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return row;
    }

    private HashMap<String, Integer> GetGeoTotals(String g)
    {
        var row = new HashMap<String, Integer>();
        var sql = "select p." + productGranulate.tableName +", sum(s.sells) as sold from sales s join time t on s.date = t.date join geo g on s.shopid = " +
                "g.shopid JOIN product p ON s.articleid = p.articleid where g." + geo.tableName + " = '" + g + "' group by p." +
                productGranulate.tableName;
        try (var statement = conn.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                row.put(resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // get total
        var sql_total = "select sum(s.sells) as sold from sales s join time t on s.date = t.date join geo g on s.shopid = " +
                "g.shopid JOIN product p ON s.articleid = p.articleid where g." + geo.tableName + " = '" + g + "'";
        try (var statement = conn.prepareStatement(sql_total)) {
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                row.put("total", resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return row;
    }

    private HashMap<String, Integer> BuildRowMap(String g, String t) {
        var row = new HashMap<String, Integer>();
        String sql = "select p." + productGranulate.tableName + ", sum(s.sells) as sold from sales s join time t on s.date = t.date join geo g on s.shopid = " +
                "g.shopid JOIN product p ON s.articleid = p.articleid where t." + time.tableName + " = '" + t +
                "' and g." + geo.tableName + " = '" + g +
                "' group by p." + productGranulate.tableName;
        try (var statement = conn.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                row.put(resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // get total
        var sql_total = "select sum(s.sells) as sold from sales s join time t on s.date = t.date join geo g on s.shopid = " +
                "g.shopid JOIN product p ON s.articleid = p.articleid where t." + time.tableName + "='" + t +
                "' and g." + geo.tableName + "= '" + g + "'";
        try (var statement = conn.prepareStatement(sql_total)) {
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                row.put("total", resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return row;
    }

    private List<String> GetGeos(Granulates.GEO geo) {
        var geos = new ArrayList<String>();
        var sql = "SELECT DISTINCT " + geo.tableName + " FROM geo ORDER BY " + geo.tableName + " ASC";
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
        var sql = "SELECT DISTINCT " + product.tableName + " FROM product ORDER BY " + product.tableName + " ASC";
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
        var sql = "SELECT DISTINCT " + time.tableName + " FROM time ORDER BY " + time.tableName + " ASC";
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
