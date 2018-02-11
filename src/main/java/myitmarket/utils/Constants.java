package myitmarket.utils;

import java.util.regex.Pattern;

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
    public static final String PRODUCT_OR_SERVICE_URL = "ProductOrServiceModel";
    public static final String OFFERING_URL = "Offering";
    public static final String UNIT_PRICE_SPECIFICATION_URL = "UnitPriceSpecification";
    public static final String GRNAME_PROPERTY_URL = "name";
    public static final String GRHAS_BRAND_PROPERTY_URL = "hasBrand";
    public static final String GRDESC_PROPERTY_URL = "description";
    public static final String GRINCLUDES_PROPERTY_URL = "includes";
    public static final String GRHAS_PRICE_SPECS_PROPERTY_URL = "hasPriceSpecification";
    public static final String GRCATEGORY_PROPERTY_URL = "category";
    public static final String GRSTORE_PROPERTY_URL = "BusinessEntity";
    public static final String GRLEGAL_NAME_PROPERTY_URL = "legalName";
    public static final String GRHAS_CURRENCY_PROPERTY_URL = "hasCurrency";
    public static final String GRHAS_CURRENCY_VALUE_PROPERTY_URL = "hasCurrencyValue";
    public static final String GROFFERS_PROPERTY_URL = "offers";
    public static final String SUB_CATEGORY_OF = "subCategoryOf";


    public static final String FILE_ONTOLOGY = System.getProperty("user.dir") + "/tmp/ontology/sporedimk.owl";
    public static final String FILE_BASE = System.getProperty("user.dir") + "/tmp/ontology/prefixes.ttl";
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
    public final static Pattern FILTER_FILES = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    public static final String ROOT_CATEGORIES = "root";

    public static final String MKD_UNIT = "MKD";

//    Neptun general kategorii: ( замрзнувачи_вертикални, замрзнувачи_хоризонтални, вградна_техника+{аспиратори, микробранови печки, фурни, машини за перење,
// плотни, шпорети, машини за садови, фрижидери}, фото_апарати_и_камери_торбички, компјутери_торбички)
//    Техномаркет (Галантерија, Батерии, Машини за садови, Фурни, Останато, Опрема)

    //Sellers
    public static final String SETEC = "Setec";
    public static final String SETEC_IMAGE_URL = "http://setec.mk/image/data/Sliki/Setec_Logo_Web.png";
    public static final String TEHNOMARKET = "Tehnomarket";
    public static final String TEHNOMARKET_IMAGE_URL = "http://tehnomarket.com.mk/img/tm_big_logo.png";
    public static final String NEPTUN = "Neptun";
    public static final String NEPTUN_IMAGE_URL = "https://www.neptun.mk/image/data/logo.png";
    public static final String ANHOCH = "Anhoch";
    public static final String ANHOCH_IMAGE_URL = "http://www.anhoch.com/img/new_year_logo_anh-min.png";
    public static final String AMC = "AMC";
    public static final String AMC_IMAGE_URL = "https://www.amc.com.mk/content/images/thumbs/0013676.png";
    public static final String NIKO = "NIKO";
    public static final String NIKO_IMAGE_URL = "http://niko.com.mk/wp-content/uploads/2016/09/NIKO-1.png";

}
