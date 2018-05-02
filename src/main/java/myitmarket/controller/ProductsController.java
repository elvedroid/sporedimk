package myitmarket.controller;

import myitmarket.utils.CrawlerUtils;
import myitmarket.utils.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import myitmarket.contracts.CrawlerContract;
import myitmarket.model.*;
import myitmarket.service.ProductService;
import myitmarket.utils.Constants;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Component
@RequestMapping("/private/products")
public class ProductsController implements CrawlerContract {

  private static final Logger log = LoggerFactory.getLogger(ProductsController.class);

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
  private int threadCount = 0;

  @Scheduled(fixedDelay = 14 * 24 * 60 * 60 * 1000)
  public void addSellers() {
    if (Filter.shouldLoadData) {
      productService.addSeller(new Seller(Constants.SETEC, Constants.SETEC_IMAGE_URL));
      CrawlerUtils.addSetec(this);
    }

    log.info("The time is now {}", dateFormat.format(new Date()));
  }

  private ProductService productService;

  @PostConstruct
  public void initController() {
    productService.init();
  }

  @Autowired
  public ProductsController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/get-all-categories")
  List<Category> getCategories() {
    return productService.getAllCategories();
  }

  @GetMapping("/get-leaf-categories/{lang}")
  List<Category> getLeafCategories(@PathVariable("lang") String lang) {
    return productService.getLeafCategories(lang);
  }

  @GetMapping("/get-root-categories")
  List<Category> getRootCategories(@RequestParam(value = "lang", defaultValue = "mk") String lang) {
    return productService.getRootCategories(lang);
  }

  @PostMapping("/get-subcategory")
  List<Category> getSubategories(@RequestBody CatLang fromCategory) {
    return productService.getSubcategories(fromCategory);
  }

  @PostMapping("/get-categories-or-subcategories")
  List<Category> getMapCategories(@RequestBody Category fromCategory) {
    return productService.getCategoriesOrSubcategories(fromCategory.getName());
  }

  @PostMapping("/get-offers-per-category/{page}/{per_page}")
  List<Offer> getOffersPerCategory(@RequestBody CatLang category,
                                   @PathVariable("page") Integer page,
                                   @PathVariable("per_page") Integer perPage) {
    return productService.getOffersPerCategory(category, page, perPage);
  }

  @PostMapping("/get-offers-with-same-product")
  List<ProductOffers> getOffersWithSameProduct(@RequestBody Product product) {
    return productService.getOffersWithSameProduct(product);
  }

  @PostMapping("/add-product-to-favorites")
  Boolean addProductToFavorites(@RequestBody Favorites favorites) {
    return productService.addProductToFavorites(favorites);
  }

  @PostMapping("/is_product_favorite")
  Boolean isProductFavorite(@RequestBody Favorites favorites) {
    return productService.isProductFavorite(favorites);
  }

  @PostMapping("/get_my_favorites")
  List<Offer> getMyFavoriteProducts(@RequestBody String appId) {
    return productService.getMyFavoriteProducts(appId);
  }

  @GetMapping("/get_most_favorites")
  List<Offer> getMostFavoriteProducts() {
    return productService.getMostFavoriteProducts();
  }

  @GetMapping("/filter-search/{search_phrase}/{lang}/{page}/{per_page}")
  List<Offer> getFilteredOffers(@PathVariable("search_phrase") String phrase,
                                @PathVariable("lang") String lang,
                                @PathVariable("page") Integer page,
                                @PathVariable("per_page") Integer perPage) {
    return productService.getFilteredOffers(phrase, lang, page, perPage);
  }


  @Override
  public void addOffer(String seller, Offer offer) {
    productService.addOffer(seller, offer);
  }

  @Override
  public void crawlerFinish(Markets market) {
    threadCount++;
    if (threadCount > 6) {
      if (Markets.SETEC.equals(market)) {
        productService.addSeller(new Seller(Constants.TEHNOMARKET, Constants.TEHNOMARKET_IMAGE_URL));
        CrawlerUtils.addTehnomarket(this);
      } else if (Markets.TEHNOMARKET.equals(market)) {
        productService.addSeller(new Seller(Constants.NEPTUN, Constants.NEPTUN_IMAGE_URL));
        CrawlerUtils.addNeptun(this);
      } else if (Markets.NEPTUN.equals(market)) {
        productService.addSeller(new Seller(Constants.ANHOCH, Constants.ANHOCH_IMAGE_URL));
        CrawlerUtils.addAnhoch(this);
      } else if (Markets.ANHOCH.equals(market)) {
        productService.addSeller(new Seller(Constants.AMC, Constants.AMC_IMAGE_URL));
        CrawlerUtils.addAMC(this);
      } else if (Markets.AMC.equals(market)) {
        productService.addSeller(new Seller(Constants.NIKO, Constants.NIKO_IMAGE_URL));
        CrawlerUtils.addNIKO(this);
      } else {
        productService.swapModels();
      }
      threadCount = 0;
    }
  }
}