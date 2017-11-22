package sporedimkboot.utils;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import sporedimkboot.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sporedimkboot.utils.Constants.*;
import static sporedimkboot.utils.Constants.GRHAS_PRICE_SPECS_PROPERTY_URL;
import static sporedimkboot.utils.Constants.GRINCLUDES_PROPERTY_URL;

public class QueryUtils {


    /**
     * Return offers which includes given product.
     */
    public static List<ProductOffers> getOffersWithSameProduct(Model model, Product product) {
        List<ProductOffers> offerList = new ArrayList<ProductOffers>();
        String resourceUri = PRODUCTS_URL + Utils.getUriReplacingChars(product.getName());
        Resource resource = model.getResource(resourceUri);
        String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\n" +
                "SELECT ?seller ?offer\n" +
                "WHERE {" +
                "?seller gr:offers ?offer." +
                "?offer gr:includes <" + resource + ">" +
                "}";

        ResultSet resultSet = executeQuery(model, query);

        for (; resultSet != null && resultSet.hasNext(); ) {
            ProductOffers offer = new ProductOffers();
            QuerySolution soln = resultSet.nextSolution();
            Resource r = soln.getResource("offer"); // Get a result variable - must be a resource
            Resource sellerResource = soln.getResource("seller"); // Get a result variable - must be a resource
            offer.setPrice(getPriceFromResource(model, r.getPropertyResourceValue(model.getProperty(BASE_GR_URL, GRHAS_PRICE_SPECS_PROPERTY_URL))));
            offer.setUrl(r.getProperty(FOAF.made).getString());
            offer.setSellerLogo(sellerResource.getProperty(FOAF.made).getString());
            offerList.add(offer);
        }

        return offerList;
    }

    /**
     * Return offers for the given category grouped by products.
     * If more than one offer includes the same product, the offer with the smallest price is returned.
     */
    public static List<Offer> getOffersPerCategory(Model model, CatLang category) {
        List<Offer> offerList = new ArrayList<Offer>();

        String queryGetProductWithMinPrice = "SELECT ?product (MIN(?price) AS ?minPrice)\n" +
                "WHERE { \n" +
                "?offer gr:includes ?product;\n" +
                "gr:hasPriceSpecification ?priceSpec.\n" +
                "?priceSpec gr:hasCurrencyValue ?price }\n" +
                "GROUP BY ?product\n";

        String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\n" +
                "SELECT ?offer\n" +
                "WHERE {\n" +
                "?offer gr:category ?category;\n" +
                "gr:includes ?product;\n" +
                "gr:hasPriceSpecification ?priceSpec.\n" +
                "?priceSpec gr:hasCurrencyValue ?price1.\n" +
                "{ " + queryGetProductWithMinPrice + " }" +
                "FILTER(lcase(str(?category)) = \"" + category.getCategoryName().toLowerCase() + "\" )" +
                " }";

        ResultSet resultSet = executeQuery(model, query);
        for (; resultSet != null && resultSet.hasNext(); ) {
            Offer offer = new Offer();
            QuerySolution soln = resultSet.nextSolution();
            Resource r = soln.getResource("offer"); // Get a result variable - must be a resource
            offer.setName(r.getProperty(model.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL)).getString());
            String categoryStr = r.getProperty(model.getProperty(BASE_GR_URL, GRCATEGORY_PROPERTY_URL)).getString();
            offer.setCategory(new Category(categoryStr));
            offer.setProduct(getProductFromResource(model, r.getPropertyResourceValue(model.getProperty(BASE_GR_URL, GRINCLUDES_PROPERTY_URL))));
            offer.setPrice(getPriceFromResource(model, r.getPropertyResourceValue(model.getProperty(BASE_GR_URL, GRHAS_PRICE_SPECS_PROPERTY_URL))));
            offer.setUrl(r.getProperty(FOAF.made).getString());
            offerList.add(offer);
        }
        return offerList;
    }

    private static Product getProductFromResource(Model model, Resource productResource) {
        Product product = new Product();
        product.setName(productResource.getProperty(model.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL)).getString());
        product.setDescription(productResource.getProperty(model.getProperty(BASE_GR_URL, GRDESC_PROPERTY_URL)).getString());
        product.setImage(productResource.getProperty(FOAF.made).getString());
        product.setBrand(getBrandName(model, productResource.getPropertyResourceValue(model.getProperty(BASE_GR_URL, GRHAS_BRAND_PROPERTY_URL))));
        return product;
    }

    private static String getBrandName(Model model, Resource brand) {
        return brand.getProperty(model.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL)) != null ?
                brand.getProperty(model.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL)).getString() : "";
    }

    private static Price getPriceFromResource(Model model, Resource propertyResourceValue) {
        Property hasCurrency = model.getProperty(BASE_GR_URL, GRHAS_CURRENCY_PROPERTY_URL);
        Property hasCurrencyValue = model.getProperty(BASE_GR_URL, GRHAS_CURRENCY_VALUE_PROPERTY_URL);
        return new Price(propertyResourceValue.getProperty(hasCurrencyValue).getDouble(), propertyResourceValue.getProperty(hasCurrency).getString());
    }

    public static ResultSet executeQuery(Model model, String queryString) {
        Query query = QueryFactory.create(queryString);
        try {
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qexec.execSelect();
            resultSet = ResultSetFactory.copyResults(resultSet);
            return resultSet;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Category> getRootCateogries(Model categoriesModel, String lang) {
        List<Category> categories = new ArrayList<Category>();

        String query = "PREFIX cat: <http://purl.org/net/esm-owl/categories#>\n" +
                "PREFIX gr: <http://purl.org/goodrelations/v1#>\n" +
                "SELECT ?category\n" +
                "WHERE \n" +
                "{ \n" +
                "    ?category gr:name ?name . \n" +
                "    MINUS \n" +
                "    {  \n" +
                "        ?category cat:subCategoryOf ?categ\n" +
                "    } \n" +
                "    FILTER (langMatches( lang(?name), \"en\" ) || lang(?name) = \"en\")\n" +
                "}";

        ResultSet resultSet = executeQuery(categoriesModel, query);

        for (; resultSet != null && resultSet.hasNext(); ) {
            QuerySolution soln = resultSet.nextSolution();
            Resource r = soln.getResource("category"); // Get a result variable - must be a resource
            Category cat = new Category();
            cat.setName(categoriesModel.getProperty(r, categoriesModel.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL), lang).getLiteral().getLexicalForm());
            cat.setIcon(r.getProperty(FOAF.made).getString());
            cat.setSubCategoryOf("");
            categories.add(cat);
        }

        return categories;
    }

    public static List<Category> getLeafCateogries(Model categoriesModel, String lang) {
        List<Category> categories = new ArrayList<Category>();

        String query = "PREFIX cat: <http://purl.org/net/esm-owl/categories#>\n" +
                "PREFIX gr: <http://purl.org/goodrelations/v1#>\n" +
                "SELECT ?category\n" +
                "WHERE \n" +
                "{ \n" +
                "    ?category gr:name ?name . \n" +
                "    MINUS \n" +
                "    {  \n" +
                "        ?categ cat:subCategoryOf ?category\n" +
                "    } \n" +
                "    FILTER (langMatches( lang(?name), \"en\" ) || lang(?name) = \"en\")\n" +
                "}";

        ResultSet resultSet = executeQuery(categoriesModel, query);

        for (; resultSet != null && resultSet.hasNext(); ) {
            QuerySolution soln = resultSet.nextSolution();
            Resource r = soln.getResource("category"); // Get a result variable - must be a resource
            Category cat = new Category();
            cat.setName(categoriesModel.getProperty(r, categoriesModel.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL), lang).getLiteral().getLexicalForm());
            cat.setIcon(r.getProperty(FOAF.made).getString());
            if (r.getPropertyResourceValue(categoriesModel.getProperty(CATEGORIES_URL, SUB_CATEGORY_OF)) == null) {
                cat.setSubCategoryOf("");
            } else {
                cat.setSubCategoryOf(r.getPropertyResourceValue(categoriesModel.getProperty(CATEGORIES_URL, SUB_CATEGORY_OF)).getURI());
                categories.add(cat);
            }
        }

        return categories;
    }

    public static List<Category> getSubcategories(Model categoriesModel, CatLang fromCategory) {
        List<Category> categories = new ArrayList<Category>();

        String query = "PREFIX cat: <http://purl.org/net/esm-owl/categories#>\n" +
                "PREFIX gr: <http://purl.org/goodrelations/v1#>\n" +
                "SELECT ?subcat\n" +
                "WHERE \n" +
                "{ \n" +
                "    ?subcat cat:subCategoryOf ?category .\n" +
                "    ?category gr:name \"" + fromCategory.getCategoryName() + "\"@" + fromCategory.getLang() + ".\n" +
                "}";

        ResultSet resultSet = executeQuery(categoriesModel, query);

        for (; resultSet != null && resultSet.hasNext(); ) {
            QuerySolution soln = resultSet.nextSolution();
            Resource r = soln.getResource("subcat"); // Get a result variable - must be a resource
            Category cat = new Category();
            cat.setName(categoriesModel.getProperty(r, categoriesModel.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL), fromCategory.getLang()).getLiteral().getLexicalForm());
            cat.setIcon(r.getProperty(FOAF.made).getString());
            if (r.getPropertyResourceValue(categoriesModel.getProperty(CATEGORIES_URL, SUB_CATEGORY_OF)) == null) {
                cat.setSubCategoryOf("");
            } else {
                cat.setSubCategoryOf(r.getPropertyResourceValue(categoriesModel.getProperty(CATEGORIES_URL, SUB_CATEGORY_OF)).getURI());
                categories.add(cat);
            }
        }
        return categories;
    }

    public static Boolean isProductFavorite(Model model, Favorites favorites) {
        String query = "PREFIX favs: <http://purl.org/net/esm-owl/favorites#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX vcard30: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "SELECT ?fav ?phone\n" +
                "WHERE {\n" +
                "?fav foaf:phone ?phone ." +
                "?fav vcard30:AGENT <" + PRODUCTS_URL + Utils.getUriReplacingChars(favorites.getProductName()) + ">.\n" +
                "FILTER(lcase(str(?phone)) = \"" + favorites.getAppID().toLowerCase() + "\" )" +
                " }";

        ResultSet resultSet = executeQuery(model, query);
        return resultSet != null && resultSet.hasNext();
    }

    public static List<Offer> getMyFavoriteProducts(Model model, String appId) {
        String query = "PREFIX favs: <http://purl.org/net/esm-owl/favorites#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX gr: <http://purl.org/goodrelations/v1#>\n" +
                "PREFIX vcard30: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "SELECT ?offer \n" +
                "WHERE {\n" +
                "?offer gr:includes ?product ." +
                "?fav foaf:phone " + appId + " ." +
                "?fav vcard30:AGENT ?product.\n" +
                " }";

        ResultSet resultSet = executeQuery(model, query);
        List<Offer> offerList = new ArrayList<Offer>();
        for (; resultSet != null && resultSet.hasNext(); ) {
            Offer offer = new Offer();
            QuerySolution soln = resultSet.nextSolution();
            Resource r = soln.getResource("offer"); // Get a result variable - must be a resource
            offer.setName(r.getProperty(model.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL)).getString());
            String categoryStr = r.getProperty(model.getProperty(BASE_GR_URL, GRCATEGORY_PROPERTY_URL)).getString();
            offer.setCategory(new Category(categoryStr));
            offer.setProduct(getProductFromResource(model, r.getPropertyResourceValue(model.getProperty(BASE_GR_URL, GRINCLUDES_PROPERTY_URL))));
            offer.setPrice(getPriceFromResource(model, r.getPropertyResourceValue(model.getProperty(BASE_GR_URL, GRHAS_PRICE_SPECS_PROPERTY_URL))));
            offer.setUrl(r.getProperty(FOAF.made).getString());
            offerList.add(offer);
        }
        return offerList;
    }

    public static List<Offer> getMostFavoriteProducts(Model model) {
        String query = "PREFIX favs: <http://purl.org/net/esm-owl/favorites#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX gr: <http://purl.org/goodrelations/v1#>\n" +
                "PREFIX vcard30: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "SELECT ?offer ?count \n" +
                "WHERE {\n" +
                "?offer gr:includes ?product .\n" +
                "?fav vcard30:AGENT ?product.\n" +
                "?fav rdf:value ?count.\n" +
                " }\n" +
                "ORDER BY DESC(?count) \n LIMIT 10";

        ResultSet resultSet = executeQuery(model, query);
        List<Offer> offerList = new ArrayList<Offer>();
        for (; resultSet != null && resultSet.hasNext(); ) {
            Offer offer = new Offer();
            QuerySolution soln = resultSet.nextSolution();
            Resource r = soln.getResource("offer"); // Get a result variable - must be a resource
            offer.setName(r.getProperty(model.getProperty(BASE_GR_URL, GRNAME_PROPERTY_URL)).getString());
            String categoryStr = r.getProperty(model.getProperty(BASE_GR_URL, GRCATEGORY_PROPERTY_URL)).getString();
            offer.setCategory(new Category(categoryStr));
            offer.setProduct(getProductFromResource(model, r.getPropertyResourceValue(model.getProperty(BASE_GR_URL, GRINCLUDES_PROPERTY_URL))));
            offer.setPrice(getPriceFromResource(model, r.getPropertyResourceValue(model.getProperty(BASE_GR_URL, GRHAS_PRICE_SPECS_PROPERTY_URL))));
            offer.setUrl(r.getProperty(FOAF.made).getString());
            offerList.add(offer);
        }
        return offerList;
    }
}
