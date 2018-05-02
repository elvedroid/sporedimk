package myitmarket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Category {
  @Id
  private String id;
  private String name;
  private Category subCategoryOf;
  private String image;
  private Category sameAs;

  public Category() {
  }

  public Category(String id, String name, Category subCategoryOf, String icon) {
    this.id = id;
    this.name = name;
    this.subCategoryOf = subCategoryOf;
    this.image = icon;
  }

  public Category(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category getSubCategoryOf() {
    return subCategoryOf;
  }

  public void setSubCategoryOf(Category subCategoryOf) {
    this.subCategoryOf = subCategoryOf;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Category getSameAs() {
    return sameAs;
  }

  public void setSameAs(Category sameAs) {
    this.sameAs = sameAs;
  }

  @Override
  public String toString() {
    return "Category{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", subCategoryOf='" + subCategoryOf + '\'' +
            ", image='" + image + '\'' +
            ", sameAs='" + sameAs + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Category category = (Category) o;
    return Objects.equals(id, category.id) &&
            Objects.equals(name, category.name) &&
            Objects.equals(subCategoryOf, category.subCategoryOf) &&
            Objects.equals(image, category.image) &&
            Objects.equals(sameAs, category.sameAs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, subCategoryOf, image, sameAs);
  }
}
