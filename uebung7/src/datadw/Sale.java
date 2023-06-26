package datadw;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Sale {
    public int id;
    public Date date;
    public int shopID;
    public int articleID;
    public int sold;
    public BigDecimal revenue;

    public static Sale fromResultSet(ResultSet rs) {
        try {
            Sale sale = new Sale();
            sale.id = rs.getInt("salesid");
            sale.date = new Date(rs.getTimestamp("sale_date").getTime());
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

