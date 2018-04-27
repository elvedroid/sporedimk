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

public class AnhochCrawler extends WebCrawler {
  private CrawlerContract listener;

  private int count = 0;

  AnhochCrawler(CrawlerContract listener) {
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

      Element bread = doc.getElementById("breadcrumbs") != null ? doc.getElementById("breadcrumbs").child(0) : null;
      if (bread != null && bread.children().size() > 2) {
        for (int i = 1; i < bread.children().size(); i++) {
          String productNameOrCategory = bread.child(i).text();
          if (i == bread.children().size() - 1) {
            productName = productNameOrCategory;
          } else if (i == bread.children().size() - 2) {
            category = new Category(bread.child(i).child(0).text());
          }
        }
        String brand = doc.getElementsByClass("product-desc").get(0).child(1).text();
        String pricestr = doc.getElementsByClass("price").get(0).getElementsByClass("nm").get(0).text();
        Double price = Double.valueOf(pricestr);
        String description = doc.getElementById("description") != null ? doc.getElementById("description").child(0).html() : null;

        Element imageEl = doc.getElementById("product_gallery").child(0);
        String imageURl = imageEl.attr("src");

        Product product = new Product(productName, brand, description, imageURl);

        String offerName = Constants.ANHOCH + "__" + productName;
        Offer offer = new Offer(offerName, product, category, new Price(price, Constants.MKD_UNIT), page.getWebURL().getURL());

        listener.addOffer(Constants.ANHOCH, offer);
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
    listener.crawlerFinish(Markets.AMC);
  }
}
