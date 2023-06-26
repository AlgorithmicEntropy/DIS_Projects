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
        // FIXME CB start connection
        Connection connectionDW = null;

        // First: Find the products to build the columns.
        List<Product> products = ReadDW.findAllProducts(connectionDW, productGranulate);

        // Map the product to its geo-Values and their sales
        Map<Product, Map<String, List<Sale>>> mapStuff = new HashMap<>();

        products.forEach(p -> mapStuff.put(p, null));

        // Then find the sales for each Product
        for(Product product : products){

            List<Sale> sales = ReadDW.findAllSalesByProduct(connectionDW, productGranulate, geo,  product);

            //Add them to  the ginormous Map
            for(Sale sale : sales){
                // FIXME CB read the right attribute of the sale
            List<Sale> geoList = mapStuff.get(product).get(sale.geoName);
            if(geoList == null){
                geoList = new ArrayList<>();
            }
            geoList.add(sale);

            // FIXME CB Not sure if we need that next line
                mapStuff.get(product).put(sale.geoName, geoList);

            }
                   }

        // FIXME CB Then do the time grouping and compute the totals for each time group

        // FIXME CB and then SOMEHOW compute the total per line ...

        // FIXME CB and then do the drawing :)
        // Irgendwie so, dass die Geo-Spalte nicht hochkannt ist sondern einfach in jeder Zeile den Wert drin hat?
        // Man kann da bestimmt auch rumwurschteln dass dann der Strich drunter leer bleibt sodass man nur einmal schreiben muss.

    }

    private static final int PRODUCT_COL_WIDTH = 10;
    private static final int GROUP_COL_WIDTH = 15;

    private String prepareStringForFormat(String original, int length) {
        return "%-" + length + "s";
    }

    private String getRowSeparator(int numberOfProducts) {
        // First Column needs to be for the group attribute
        return "+"+"-".repeat(GROUP_COL_WIDTH) + "+"
                // Then a column for each product
                + ("-".repeat(PRODUCT_COL_WIDTH) + "+").repeat(numberOfProducts)
                // then a column for the total
                + "-".repeat(10) + "+";
    }



}
