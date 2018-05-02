package myitmarket.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})

public class Product {
  @Id
  private String name;
  private String brand;
  private String description;
  private String image;

  public Product() {
  }

  public Product(String name, String brand, String description) {
    this.name = name;
    this.brand = brand;
    this.description = description;
  }

  public Product(String name, String brand, String description, String image) {
    this.name = name;
    this.brand = brand;
    this.description = description;
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "Product{" +
            ", name='" + name + '\'' +
            ", brand='" + brand + '\'' +
            ", description='" + description + '\'' +
            ", image='" + image + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Objects.equals(name, product.name) &&
            Objects.equals(brand, product.brand) &&
            Objects.equals(description, product.description) &&
            Objects.equals(image, product.image);
  }

  @Override
  public int hashCode() {

    return Objects.hash(name, brand, description, image);
  }
}
