package myitmarket.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import myitmarket.contracts.CrawlerContract;

public class AMCCrawlerControllerFactory implements CrawlController.WebCrawlerFactory{
    private CrawlerContract listener;

    public AMCCrawlerControllerFactory(CrawlerContract listener) {
        this.listener = listener;
    }

    @Override
    public WebCrawler newInstance() throws Exception {
        return new AMCCrawler(listener);
    }
}
