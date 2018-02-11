package myitmarket.utils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import myitmarket.contracts.CrawlerContract;
import myitmarket.controller.ProductsController;
import myitmarket.crawler.*;
import myitmarket.model.Seller;

public class CrawlerUtils {

    public static void addSetec(CrawlerContract listener) {
        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/setec";
        int numberOfCrawlers = 7;
        SetecCrawlerControllerFactory factory = new SetecCrawlerControllerFactory(listener);
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

    public static void addTehnomarket(CrawlerContract listener) {
        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/tehnomarket";
        int numberOfCrawlers = 7;
        TehnomarketCrawlerControllerFactory factory = new TehnomarketCrawlerControllerFactory(listener);
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

    public static void addNeptun(CrawlerContract listener) {
        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/neptun";
        int numberOfCrawlers = 7;
        NeptunCrawlerControllerFactory factory = new NeptunCrawlerControllerFactory(listener);
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

    public static void addAnhoch(CrawlerContract listener) {
        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/anhoch";
        int numberOfCrawlers = 7;
        AnhochCrawlerControllerFactory factory = new AnhochCrawlerControllerFactory(listener);
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
            controller.addSeed("http://www.anhoch.com/category/3003/prenosni-kompjuteri-laptopi");
//            controller.addSeed("http://www.anhoch.com/");
            controller.startNonBlocking(factory, numberOfCrawlers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNIKO(CrawlerContract listener) {
        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/anhoch";
        int numberOfCrawlers = 7;
        NikoCrawlerControllerFactory factory = new NikoCrawlerControllerFactory(listener);
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
            controller.addSeed("http://www.anhoch.com/category/3003/prenosni-kompjuteri-laptopi");
//            controller.addSeed("http://www.anhoch.com/");
            controller.startNonBlocking(factory, numberOfCrawlers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAMC(CrawlerContract listener) {
        String crawlStorageFolder = System.getProperty("user.dir") + "/tmp/crawl/root/anhoch";
        int numberOfCrawlers = 7;
        AMCCrawlerControllerFactory factory = new AMCCrawlerControllerFactory(listener);
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
            controller.addSeed("https://amc.com.mk/");
//            controller.addSeed("http://www.anhoch.com/");
            controller.startNonBlocking(factory, numberOfCrawlers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
