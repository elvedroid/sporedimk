package myitmarket.contracts;

import myitmarket.model.Markets;
import myitmarket.model.Offer;

public interface CrawlerContract {
    void addOffer(String seller, Offer offer);
    void crawlerFinish(Markets market);
}

