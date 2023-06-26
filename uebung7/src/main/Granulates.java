package main;

public class Granulates {

    public static enum GEO {
        COUNTRY("country"), REGION("region"), CITY("city"), SHOP("shop");
        public String tableName;

        private GEO(String tableName) {
            this.tableName = tableName;
        }
    }

    public static enum TIME {
        DATE("date"), DAY("day"), MONTH("month"), QUARTER("quarter"), YEAR("year");
        public String tableName;

        private TIME(String tableName) {
            this.tableName = tableName;
        }
    }

    public static enum PRODUCT {
        PRODUCT_CATEGORY("ProductCategory"), PRODUCT_FAMILY("ProductFamily"), PRODUCT_GROUP("ProductGroup"), ARTICLE("Article");
        public String tableName;

        private PRODUCT(String geo) {
            this.tableName = geo;
        }
    }
}
