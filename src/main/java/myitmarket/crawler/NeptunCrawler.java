package myitmarket.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import myitmarket.contracts.CrawlerContract;
import myitmarket.model.*;
import myitmarket.utils.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class NeptunCrawler extends WebCrawler {

    private CrawlerContract listener;

    private int count = 0;

    NeptunCrawler(CrawlerContract listener) {
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
            curURL.setURL(curURL.getURL() + "&sort=p.price&order=ASC&limit=1200");
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

            Element bread = doc.getElementsByClass("breadcrumb") != null ? doc.getElementsByClass("breadcrumb").get(0) : null;
            if (bread != null && bread.children().size() > 3) {
                for (int i = 1; i < bread.children().size(); i++) {
                    String productNameOrCategory = bread.child(i).text();
                    if (i == bread.children().size() - 1) {
                        productName = productNameOrCategory;
                    } else if (i == bread.children().size() - 2) {
                        category = new Category(productNameOrCategory);
                    }
                }
                String brand = "";
                Double price = Double.valueOf(doc.getElementsByClass("regular-price-instore") != null ? doc.getElementsByClass("regular-price-instore").text() :
                "0");
                String description = doc.getElementsByClass("description") != null ? doc.getElementsByClass("description").get(0).html() : null;

                Element imageEl = doc.getElementById("image");
                String imageURl = imageEl.attr("src");

                Product product = new Product(productName, brand, description, imageURl);

                String offerName = Constants.NEPTUN + "__" + productName;
                Offer offer = new Offer(offerName, product, category, new Price(price, Constants.MKD_UNIT), page.getWebURL().getURL());

                listener.addOffer(Constants.SETEC, offer);
                count++;
                if (count >= 10) {
                    getMyController().shutdown();
                }
            }
            super.visit(page);
        }
    }

    @Override
    public void onBeforeExit() {
        super.onBeforeExit();
        listener.crawlerFinish(Markets.NEPTUN);
    }
}
