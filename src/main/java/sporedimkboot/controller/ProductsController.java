package sporedimkboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sporedimkboot.model.*;
import sporedimkboot.service.ProductService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/private/products")
public class ProductsController {

    private ProductService productService;

    @PostConstruct
    public void init() {
        productService.init();
    }

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get-all-categories")
    List<Category> getCategories(){
        return productService.getAllCategories();
    }

    @GetMapping("/get-leaf-categories/{lang}")
    List<Category> getLeafCategories(@PathVariable("lang") String lang){
        return productService.getLeafCategories(lang);
    }

    @GetMapping("/get-root-categories/{lang}")
    List<Category> getRootCategories(@PathVariable("lang") String lang){
        return productService.getRootCategories(lang);
    }

    @PostMapping("/get-subcategory")
    List<Category> getSubategories(@RequestBody CatLang fromCategory){
        return productService.getSubcategories(fromCategory);
    }

    @PostMapping("/get-categories-or-subcategories")
    List<Category> getMapCategories(@RequestBody Category fromCategory){
        return productService.getCategoriesOrSubcategories(fromCategory.getName());
    }

    @PostMapping("/add-seller")
    void addSeller(@RequestBody Seller seller) {
        productService.addSeller(seller);
    }

    @PostMapping("/get-offers-per-category")
    List<Offer> getOffersPerCategory(@RequestBody CatLang category){
        return productService.getOffersPerCategory(category);
    }

    @PostMapping("/get-offers-with-same-product")
    List<ProductOffers> getOffersWithSameProduct(@RequestBody Product product){
        return productService.getOffersWithSameProduct(product);
    }

    @PostMapping("/add-product-to-favorites")
    void addProductToFavorites(@RequestBody Favorites favorites){
        productService.addProductToFavorites(favorites);
    }

    @PostMapping("/is_product_favorite")
    Boolean isProductFavorite(@RequestBody Favorites favorites){
        return productService.isProductFavorite(favorites);
    }

    @PostMapping("/get_my_favorites")
    List<Offer> getMyFavoriteProducts(@RequestBody String appId){
        return productService.getMyFavoriteProducts(appId);
    }

    @GetMapping("/get_most_favorites")
    List<Offer> getMostFavoriteProducts(){
        return productService.getMostFavoriteProducts();
    }

}