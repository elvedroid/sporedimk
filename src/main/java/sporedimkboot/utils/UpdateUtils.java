package sporedimkboot.utils;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
import sporedimkboot.model.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static sporedimkboot.utils.Constants.*;
import static sporedimkboot.utils.Constants.BRANDS_OWL_URL;

public class UpdateUtils {
    public static void setUpModel(Model model, String modelURL) throws IOException {
        URL url = new URL(modelURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);

        String newUrl = conn.getHeaderField("Location");
        conn = (HttpURLConnection) new URL(newUrl).openConnection();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getURL().openStream()));
        model.read(in, null);
        in.close();
    }

    public static void setUpLocalModel(Model model, String modelURL) throws IOException {
        InputStream in = FileManager.get().open(modelURL);
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + modelURL + " not found");
        }
        model.read(in, null, "TURTLE");
        in.close();
    }

    private static Resource createProductResource(Model productsModel, Product product) {
//        Create product Resource
        String resourceUri = PRODUCTS_URL + Utils.getUriReplacingChars(product.getName());
        Resource resource = productsModel.getResource(resourceUri);
        resource.addProperty(RDF.type, PRODUCT_OR_SERVICE_URL);

//        Adding name property
        Property prName = productsModel.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL);
        resource.addProperty(prName, product.getName());

//        Adding brand property
        Property prBrand = productsModel.getProperty(BASE_GR_URL, GRHAS_BRAND_PROPERTY_URL);
        resource.addProperty(prBrand, getBrandResource(productsModel, product.getBrand()));

//        Adding image property
        resource.addProperty(FOAF.made, product.getImage());

//        Adding desc property
        Property prDesc = productsModel.getProperty(BASE_GR_URL, GRDESC_PROPERTY_URL);
        resource.addProperty(prDesc, product.getDescription());

        return resource;
    }

    public static Resource createOfferResource(Model offersModel, Model productsModel, Offer offer) {
//        Create offer Resource
        String offerUri = OFFERS_URL + Utils.getUriReplacingChars(offer.getName());
        Resource resource = offersModel.createResource(offerUri);
        resource.addProperty(RDF.type, OFFERING_URL);

//        Adding name property
        Property prName = offersModel.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL);
        resource.addProperty(prName, offer.getName());

//        Adding product property
        Property prProduct = offersModel.getProperty(BASE_GR_URL, GRINCLUDES_PROPERTY_URL);
        resource.addProperty(prProduct, createProductResource(productsModel, offer.getProduct()));

//        Adding category property
        Property prCategory = offersModel.getProperty(BASE_GR_URL, GRCATEGORY_PROPERTY_URL);

        resource.addProperty(prCategory, offer.getCategory().getName().replace(Constants.FILTER_CATEGORIES, "_"));

//        Adding Price Specification
        Property prHasPriceSpecs = offersModel.getProperty(BASE_GR_URL, GRHAS_PRICE_SPECS_PROPERTY_URL);
        resource.addProperty(prHasPriceSpecs, getPriceSpec(offersModel, offer.getPrice()));

//        Add url property
        resource.addProperty(FOAF.made, offer.getUrl());
        return resource;
    }

    private static Resource getPriceSpec(Model model, Price price) {
        String priceUri = OFFERS_URL + Utils.getUriReplacingChars(price.getValue() + "__" + price.getUnit());
        Resource priceResource = model.getResource(priceUri);
        priceResource.addProperty(RDF.type, UNIT_PRICE_SPECIFICATION_URL);

        Property hasCurrency = model.getProperty(BASE_GR_URL, GRHAS_CURRENCY_PROPERTY_URL);
        Property hasCurrencyValue = model.getProperty(BASE_GR_URL, GRHAS_CURRENCY_VALUE_PROPERTY_URL);

        priceResource.addProperty(hasCurrency, price.getUnit());
        priceResource.addProperty(hasCurrencyValue, String.valueOf(price.getValue()));

        return priceResource;
    }

    private static Resource getBrandResource(Model model, String brandName) {
        String brandUri = BRANDS_OWL_URL + Utils.getUriReplacingChars(brandName);
        Resource brandResource = model.getResource(brandUri);

//        Adding name property
        Property prName = model.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL);
        brandResource.addProperty(prName, brandName);

        return brandResource;
    }

    public static void addOfferingToSeller(Model model, String sellerName, Resource offering) {
        String sellerUri = STORES_URL + Utils.getUriReplacingChars(sellerName);
        Resource sellerResource = model.getResource(sellerUri);
        Property prOffering = model.getProperty(BASE_GR_URL, GROFFERS_PROPERTY_URL);
        sellerResource.addProperty(prOffering, offering);
    }

    public static void writeModelToFile(Model model, String file) {
        try {
            OutputStream out = new FileOutputStream(file);
            model.write(out, "TURTLE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateFavorites(Model favoritesModel, Model productModel, Favorites favorites) {
        Resource favoritesResource = favoritesModel.createResource(FAVORITES_URL + Utils.getUriReplacingChars(favorites.getProductName()));

        String query = "PREFIX favs: <http://purl.org/net/esm-owl/favorites#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX vcard30: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "SELECT ?fav ?phone\n" +
                "WHERE {\n" +
                "?fav foaf:phone ?phone ." +
                "?fav vcard30:AGENT <" + PRODUCTS_URL + Utils.getUriReplacingChars(favorites.getProductName()) + ">.\n" +
                "FILTER(lcase(str(?phone)) = \"" + favorites.getAppID().toLowerCase() + "\" )" +
                " }";

        ResultSet resultSet = QueryUtils.executeQuery(favoritesModel, query);

        int favs = 0;
        if (favoritesResource.getProperty(RDF.value) != null) {
            favs = favoritesResource.getProperty(RDF.value).getInt();
        }

        if (favorites.isFavorite()
                && productModel.getResource(PRODUCTS_URL + Utils.getUriReplacingChars(favorites.getProductName())) != null) {
            if (resultSet != null && resultSet.hasNext()) {
                return;
            }
            favoritesResource.addProperty(FOAF.phone, favorites.getAppID());
            favoritesResource.addProperty(VCARD.AGENT, productModel.getResource(PRODUCTS_URL + Utils.getUriReplacingChars(favorites.getProductName())));
            if (favoritesResource.getProperty(RDF.value) != null) {
                favoritesResource.getProperty(RDF.value).changeLiteralObject(++favs);
            } else {
                favoritesResource.addLiteral(RDF.value, ++favs);
            }
            writeModelToFile(favoritesModel, FILE_FAVORITES);
        } else {
            if (resultSet == null || !resultSet.hasNext()) {
                return;
            }
            if (favs <= 1 || favoritesResource.getProperty(FOAF.phone) == null) {
                favoritesModel.removeAll(favoritesResource, null, null);
                writeModelToFile(favoritesModel, FILE_FAVORITES);
                return;
            }

            favoritesModel.remove(favoritesResource, FOAF.phone, ResourceFactory.createStringLiteral(favorites.getAppID()));
            favoritesResource.getProperty(RDF.value).changeLiteralObject(--favs);
            writeModelToFile(favoritesModel, FILE_FAVORITES);

        }
    }
}
