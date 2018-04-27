package myitmarket.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import myitmarket.contracts.CrawlerContract;
import myitmarket.model.*;
import myitmarket.utils.Constants;

public class TehnomarketCrawler extends WebCrawler {

    private CrawlerContract listener;

    private int counter = 0;

    TehnomarketCrawler(CrawlerContract listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !Constants.FILTER_FILES.matcher(href).matches() && (href.contains("product") || href.contains("category"));
    }

    @Override
    public void visit(Page page) {
        if (page.getWebURL().getURL().contains("product")) {
            String productName = "";
            Category category = new Category();
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Document doc = Jsoup.parse(htmlParseData.getHtml());

            Element bread = doc.getElementById("breadcrumbs").child(0);
            if (bread.children().size() > 3) {
                for (int i = 3; i < bread.children().size(); i++) {
                    if (i == bread.children().size() - 1) {
                        productName = bread.child(i).text();
                    } else if (i == bread.children().size() - 2) {
                        String productNameOrCategory = bread.child(i).child(0).text();
                        if (i == bread.children().size() - 1) {
                            productName = productNameOrCategory;
                        } else if (i == bread.children().size() - 2) {
                            category = new Category(productNameOrCategory);
                        }
                    }
                }
                String brand = doc.getElementsByClass("product-desc").get(0).textNodes().get(1).text();
                Long id = Long.valueOf(doc.getElementsByClass("product-desc").get(0).textNodes().get(3).text().replaceAll(" ", ""));
                Double price = Double.valueOf(doc.getElementsByClass("price3").get(0).child(0).text().replaceAll("\\D+", ""));
                price = Math.abs(price);

                StringBuilder description = new StringBuilder();
                for (Node element : doc.getElementById("description").child(0).childNodes()) {
                    if (element instanceof Element) {
                        if (((Element) element).text().replaceAll(" ", "").equals("Гаранција:")) {
                            description.append(((Element) element).text());
                        } else if (!"".equals(((Element) element).text().replaceAll(" ", ""))) {
                            description.append(((Element) element).text()).append("\n");
                        }
                    } else {
                        if (!"".equals(element.toString().replaceAll(" ", ""))) {
                            description.append(element).append("\n");
                        }
                    }
                }
                Element imageEl = doc.getElementById("product_gallery") != null ? doc.getElementById("product_gallery").child(0) : null;
                String imageURl = imageEl != null ? imageEl.attr("src") : "";

                Product product = new Product(productName, brand, description.toString(), imageURl);

                String offerName = Constants.TEHNOMARKET + "__" + productName;
                Offer offer = new Offer(offerName, product, category, new Price(price, Constants.MKD_UNIT), page.getWebURL().getURL());
                listener.addOffer(Constants.TEHNOMARKET, offer);

                counter++;
                if (counter >= 100) {
                    getMyController().shutdown();
                }
            }
        }
        super.visit(page);
    }

    @Override
    public void onBeforeExit() {
        super.onBeforeExit();
        listener.crawlerFinish(Markets.TEHNOMARKET);
    }
}
