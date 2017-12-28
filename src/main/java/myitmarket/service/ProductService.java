package myitmarket.service;

import myitmarket.model.*;

import java.util.List;

public interface ProductService {
    void init();
    void addSeller(Seller seller);
    List<Category> getAllCategories();
    List<Category> getLeafCategories(String lang);
    List<Category> getRootCategories(String lang);
    List<Category> getSubcategories(CatLang fromCategory);
    List<Category> getCategoriesOrSubcategories(String fromCategory);
    List<Offer> getOffersPerCategory(CatLang category, Integer page, Integer perPage);
    List<ProductOffers> getOffersWithSameProduct(Product product);
    String getCategoriesJson();
    Boolean addProductToFavorites(Favorites favorites);
    Boolean isProductFavorite(Favorites favorites);

    Product findProductByName(String name);

    List<Offer> getMyFavoriteProducts(String appId);

    List<Offer> getMostFavoriteProducts();

    List<Offer> getFilteredOffers(String phrase, String lang, Integer page, Integer per_page);

    void addOffer(String seller, Offer offer);

    void swapModels();
}
