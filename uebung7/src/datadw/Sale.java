package datadw;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Sale {
    public Date date;
    public int shopID;
    public int articleID;
    public int sold;
    public BigDecimal revenue;

    public Sale() {
    }

    public Sale(Date date, int shopID, int articleID, int sold, BigDecimal revenue) {
        this.date = date;
        this.shopID = shopID;
        this.articleID = articleID;
        this.sold = sold;
        this.revenue = revenue;
    }

    public static Sale fromResultSet(ResultSet rs) {
        try {
            Sale sale = new Sale();
            sale.date = rs.getDate("Date");
            sale.shopID = rs.getInt("shopid");
            sale.articleID = rs.getInt("articleid");
            sale.sold = rs.getInt("sells");
            sale.revenue = rs.getBigDecimal("revenue");

            return sale;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

