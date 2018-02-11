package myitmarket.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import myitmarket.contracts.CrawlerContract;

public class AnhochCrawlerControllerFactory implements CrawlController.WebCrawlerFactory {
    private CrawlerContract listener;

    public AnhochCrawlerControllerFactory(CrawlerContract listener) {
        this.listener = listener;
    }

    @Override
    public WebCrawler newInstance() throws Exception {
        return new AnhochCrawler(listener);
    }
}
