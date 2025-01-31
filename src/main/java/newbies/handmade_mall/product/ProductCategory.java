package newbies.handmade_mall.product;

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
