package myitmarket.controller;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import myitmarket.crawler.NeptunCrawlerControllerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import myitmarket.contracts.CrawlerContract;
import myitmarket.crawler.SetecCrawlerControllerFactory;
import myitmarket.crawler.TehnomarketCrawlerControllerFactory;
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
//        addSetec();
        addNeptun();
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
    List<Category> getRootCategories(@RequestParam(value="lang", defaultValue="mk") String lang) {
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
            if (market.equals(Markets.SETEC)) {
                addTehnomarket();
            } else if (market.equals(Markets.TEHNOMARKET)) {
                addNeptun();
            } else if (market.equals(Markets.NEPTUN)){
                productService.swapModels();
            }
            threadCount = 0;
        }
    }

    private void addSetec() {
        productService.addSeller(new Seller(Constants.SETEC, Constants.SETEC_IMAGE_URL));

        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/setec";
        int numberOfCrawlers = 7;
        SetecCrawlerControllerFactory factory = new SetecCrawlerControllerFactory(this);
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(3);
        config.setPolitenessDelay(100);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = null;
        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
            controller.addSeed("http://setec.mk/");
            controller.startNonBlocking(factory, numberOfCrawlers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTehnomarket() {
        productService.addSeller(new Seller(Constants.TEHNOMARKET, Constants.TEHNOMARKET_IMAGE_URL));

        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/tehnomarket";
        int numberOfCrawlers = 7;
        TehnomarketCrawlerControllerFactory factory = new TehnomarketCrawlerControllerFactory(this);
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(3);
        config.setPolitenessDelay(100);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller;
        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
            controller.addSeed("http://tehnomarket.com.mk/");
            controller.startNonBlocking(factory, numberOfCrawlers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNeptun() {
        productService.addSeller(new Seller(Constants.NEPTUN, Constants.NEPTUN_IMAGE_URL));

        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/neptun";
        int numberOfCrawlers = 7;
        NeptunCrawlerControllerFactory factory = new NeptunCrawlerControllerFactory(this);
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(4);
        config.setPolitenessDelay(100);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller;
        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
            controller.addSeed("http://www.neptun.mk/index.php?route=common/home");
            controller.addSeed("http://www.neptun.mk/index.php?route=product/category&path=2");
            controller.startNonBlocking(factory, numberOfCrawlers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}