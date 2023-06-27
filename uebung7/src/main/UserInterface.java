package main;

import datadw.Product;
import datadw.Sale;
import datadw.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInterface {

    public static void main(String[] args) throws SQLException {
        printProductTable(Granulates.GEO.CITY, Granulates.TIME.QUARTER, Granulates.PRODUCT.PRODUCT_FAMILY);
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
        var table = new Table(geo, time, productGranulate);
        table.PrintTable();
    }
}
