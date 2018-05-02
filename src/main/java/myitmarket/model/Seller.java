package myitmarket.model;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

public class Seller {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String name;
  @ElementCollection
  private List<Offer> offers;
  private String image;

  public Seller() {
  }

  public Seller(String name, String imageUrl) {
    this.name = name;
    this.image = imageUrl;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Offer> getOffers() {
    if (offers == null) {
      offers = new ArrayList<Offer>();
    }
    return offers;
  }

  public void setOffers(List<Offer> offers) {
    this.offers = offers;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
