package main;

public class Granulates {

    public static enum GEO {
        COUNTRY("countryname"), REGION("regionname"), CITY("cityname"), SHOP("shopname");
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
        PRODUCT_CATEGORY("productcategoryname"), PRODUCT_FAMILY("productfamilyname"), PRODUCT_GROUP("productgroupname"), ARTICLE("articlename");
        public String tableName;

        private PRODUCT(String geo) {
            this.tableName = geo;
        }
    }
}
