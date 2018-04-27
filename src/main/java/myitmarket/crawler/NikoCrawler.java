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

public class NikoCrawler extends WebCrawler {

    private CrawlerContract listener;

    private int count = 0;

    public NikoCrawler(CrawlerContract listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !Constants.FILTER_FILES.matcher(href).matches() && (href.contains("/p/") || href.contains("kategorija"));
    }

    @Override
    public void visit(Page page) {
        if (page.getWebURL().getURL().contains("/p/")) {
            String productName = "";
            Category category = new Category();
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Document doc = Jsoup.parse(htmlParseData.getHtml());

            Element bread = doc.getElementsByClass("mc-breadcrumb") != null
                    ? doc.getElementsByClass("mc-breadcrumb")
                    .get(0) : null;
            if (bread != null && bread.children().size() > 2) {
                for (int i = 1; i < bread.children().size(); i++) {
                    category = new Category(bread.child(i).child(0).child(0).text());
                }
                productName = doc.getElementsByClass("product_title").get(0).text();
                String brand = doc.getElementsByClass("product-brand").get(0).text();
                String pricestr = doc.getElementsByClass("price").get(0).getElementsByClass("amount").get(0).text();
                Double price = Double.valueOf(pricestr);
                String description = doc.getElementsByClass("woocommerce-product-details__short-description") != null ? doc.getElementsByClass("woocommerce-product-details__short-description").get(0).html() : null;

                Element imageEl = doc.getElementsByClass("woocommerce-product-gallery__image").get(0).child(0);
                String imageURl = imageEl.attr("src");

                Product product = new Product(productName, brand, description, imageURl);

                String offerName = Constants.NIKO + "__" + productName;
                Offer offer = new Offer(offerName, product, category, new Price(price, Constants.MKD_UNIT), page.getWebURL().getURL());

                listener.addOffer(Constants.NIKO, offer);
                count++;
                if (count >= 100) {
                    getMyController().shutdown();
                }
            }
            super.visit(page);
        }
    }

    @Override
    public void onBeforeExit() {
        super.onBeforeExit();
        listener.crawlerFinish(Markets.NIKO);
    }
}
