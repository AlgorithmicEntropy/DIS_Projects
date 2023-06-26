package etl.db;

import java.math.BigDecimal;

public class Article {
    public int articleID;
    public int productgroupid;
    public String name;
    public BigDecimal price;

    @Override
    public int hashCode() {
        return new String(name + productgroupid + price).hashCode();
    }
}
