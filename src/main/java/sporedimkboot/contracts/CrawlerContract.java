package sporedimkboot.contracts;

import sporedimkboot.model.Markets;
import sporedimkboot.model.Offer;

public interface CrawlerContract {
    void addOffer(String seller, Offer offer);
    void crawlerFinish(Markets market);
}

