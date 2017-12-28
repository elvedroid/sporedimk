package myitmarket.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import myitmarket.contracts.CrawlerContract;
import myitmarket.model.*;
import myitmarket.utils.Constants;

public class SetecCrawler extends WebCrawler {

    private CrawlerContract listener;

    private int count = 0;

    SetecCrawler(CrawlerContract listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !Constants.FILTER_FILES.matcher(href).matches() && href.contains("product");
    }

    @Override
    protected WebURL handleUrlBeforeProcess(WebURL curURL) {
        if (curURL.getURL().toLowerCase().contains("category")) {
            curURL.setURL(curURL.getURL() + "#limit=500");
            return curURL;
        }
        return super.handleUrlBeforeProcess(curURL);
    }

    @Override
    public void visit(Page page) {
        if (page.getWebURL().getURL().contains("product_id")) {
            String productName = "";
            Category category = new Category();
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Document doc = Jsoup.parse(htmlParseData.getHtml());

            Element bread = doc.getElementById("bread");
            if (bread.children().size() > 3) {
                for (int i = 1; i < bread.children().size(); i++) {
                    String productNameOrCategory = bread.child(i).text();
                    if (i == bread.children().size() - 1) {
                        productName = productNameOrCategory;
                    } else if (i == bread.children().size() - 2) {
                        category = new Category(productNameOrCategory);
                    }
                }
                String brand = doc.getElementById("TextBrand").child(0).child(0).text();
                Double price = Double.valueOf(doc.getElementById("cena").text().replaceAll("\\D+", ""));
                String description = doc.getElementById("tab-description").text().replaceAll(", ", "\n");
                Element imageEl = doc.getElementById("image");
                String imageURl = imageEl.attr("src");

                Product product = new Product(productName, brand, description, imageURl);

                String offerName = Constants.SETEC + "__" + productName;
                Offer offer = new Offer(offerName, product, category, new Price(price, Constants.MKD_UNIT), page.getWebURL().getURL());

                listener.addOffer(Constants.SETEC, offer);
                count++;
                if (count >= 50) {
                    getMyController().shutdown();
                }
            }
        }
    }

    @Override
    public void onBeforeExit() {
        super.onBeforeExit();
        listener.crawlerFinish(Markets.SETEC);
    }

}
