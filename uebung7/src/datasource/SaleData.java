package datasource;

import main.Util;

import java.math.BigDecimal;
import java.util.Date;

public class SaleData {

    // Data parsed from the csv file
    public Date saleDate;
    public String shopName;
    public String articleName;
    public Integer sold;
    public BigDecimal revenue;

    public static SaleData parseSaleData(String saleEntry) throws NumberFormatException {

        String[] values = saleEntry.split(";");

        if (values.length < 5) {
            System.out.println("Corrupt data! Entry has less than five columns! Corrupt Entry: [" + saleEntry + "]");
        }

        SaleData sale = new SaleData();
        sale.saleDate = Util.parseDate(get(values, 0));
        sale.shopName = get(values, 1);
        sale.articleName = get(values, 2);
        sale.sold = Util.parseInt(get(values, 3));
        sale.revenue = Util.parseMoney(get(values, 4));

        return sale;
    }


    private static String get(String[] values, int index) {
        return index >= values.length ? null : values[index];
    }

}
