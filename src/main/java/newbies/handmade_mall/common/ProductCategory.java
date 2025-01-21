package newbies.handmade_mall.common;

public enum ProductCategory {
    GENERAL, GIFT;

    public static ProductCategory getEnum(String arg) {
        try {
            return ProductCategory.valueOf(arg);
        } catch (IllegalArgumentException ex) {
            return ProductCategory.GENERAL;
        }
    }
}
