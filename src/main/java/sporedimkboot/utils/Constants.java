package sporedimkboot.utils;

public class Constants {
    public static final String MODEL_URL = "http://purl.net/net/esm-owl";
    public static final String BASE_OWL_URL = "http://purl.net/net/esm-owl#";
    public static final String STORES_URL = "http://purl.org/net/esm-owl/stores#";
    public static final String PRODUCTS_URL = "http://purl.org/net/esm-owl/products#";
    public static final String OFFERS_URL = "http://purl.org/net/esm-owl/offers#";
    public static final String CATEGORIES_URL = "http://purl.org/net/esm-owl/categories#";
    public static final String FAVORITES_URL = "http://purl.org/net/esm-owl/favorites#";

    public static final String BRANDS_OWL_URL = "http://purl.net/net/esm-owl/brands#";

    public static final String BASE_GR_URL = "http://purl.org/goodrelations/v1#";
    public static final String PRODUCT_OR_SERVICE_URL = BASE_GR_URL + "ProductOrServiceModel";
    public static final String OFFERING_URL = BASE_GR_URL + "Offering";
    public static final String UNIT_PRICE_SPECIFICATION_URL = BASE_GR_URL + "UnitPriceSpecification";
    public static final String GRNAME_PROPERTY_URL = "name";
    public static final String GRHAS_BRAND_PROPERTY_URL = "hasBrand";
    public static final String GRDESC_PROPERTY_URL = "description";
    public static final String GRINCLUDES_PROPERTY_URL = "includes";
    public static final String GRHAS_PRICE_SPECS_PROPERTY_URL = "hasPriceSpecification";
    public static final String GRCATEGORY_PROPERTY_URL = "category";
    public static final String GRHAS_CURRENCY_PROPERTY_URL = "hasCurrency";
    public static final String GRHAS_CURRENCY_VALUE_PROPERTY_URL = "hasCurrencyValue";
    public static final String GROFFERS_PROPERTY_URL = "offers";
    public static final String SUB_CATEGORY_OF = "subCategoryOf";


    public static final String FILE_ONTOLOGY = System.getProperty("user.dir") + "/tmp/ontology/sporedimk.owl";
    public static final String FILE_BASE = System.getProperty("user.dir") + "/tmp/ontology/base.ttl";
    public static final String FILE_STORES = System.getProperty("user.dir") + "/tmp/ontology/stores.ttl";
    public static final String FILE_STORES_BASE = System.getProperty("user.dir") + "/tmp/ontology/stores_base.ttl";
    public static final String FILE_OFFERS = System.getProperty("user.dir") + "/tmp/ontology/offers.ttl";
    public static final String FILE_PRODUCTS = System.getProperty("user.dir") + "/tmp/ontology/products.ttl";
    public static final String FILE_CATEGORIES = System.getProperty("user.dir") + "/tmp/kategorii.txt";
    public static final String FILE_CATEGORIES_TTL = System.getProperty("user.dir") + "/tmp/ontology/categories.ttl";
    public static final String FILE_FAVORITES = System.getProperty("user.dir") + "/tmp/ontology/favorites.ttl";

    public static final String FILTER_SPECIAL_CHARACTERS = "([\\\\\\.\\[\\{\\(\\*\\+\\?\\^\\$\\|\\%\\√\\`\\…\\'\\\"\\`‌​])";
    public static final String FILTER_NON_LETTERS_OR_NUMBERS = "([^а-шА-Ш\\w])";
    public static final String FILTER_CATEGORIES = "([\\\\\\.\\[\\{\\(\\*\\+\\?\\^\\$\\|\\%\\√\\`\\…\\'\\\"\\`‌​])";

    public static final String ROOT_CATEGORIES = "root";

}
