package sporedimkboot.service;

import org.springframework.data.jpa.repository.Query;
import sporedimkboot.model.*;

import java.util.HashMap;
import java.util.List;

public interface ProductService {
    void init();
    void addSeller(Seller seller);
    List<Category> getAllCategories();
    List<Category> getLeafCategories(String lang);
    List<Category> getRootCategories(String lang);
    List<Category> getSubcategories(CatLang fromCategory);
    List<Category> getCategoriesOrSubcategories(String fromCategory);
    List<Offer> getOffersPerCategory(CatLang category);
    List<ProductOffers> getOffersWithSameProduct(Product product);
    String getCategoriesJson();
    void addProductToFavorites(Favorites favorites);
    Boolean isProductFavorite(Favorites favorites);

    Product findProductByName(String name);

    List<Offer> getMyFavoriteProducts(String appId);

    List<Offer> getMostFavoriteProducts();
}
