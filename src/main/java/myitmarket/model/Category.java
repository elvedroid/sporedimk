package myitmarket.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Category {
    @Id
    private String uri;
    private String name;
    private String subCategoryOf;
    private String image;
    private String sameAs;

    public Category() {
    }

    public Category(String uri, String name, String subCategoryOf, String icon) {
        this.uri = uri;
        this.name = name;
        this.subCategoryOf = subCategoryOf;
        this.image = icon;
    }

    public Category(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubCategoryOf() {
        return subCategoryOf;
    }

    public void setSubCategoryOf(String subCategoryOf) {
        this.subCategoryOf = subCategoryOf;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSameAs() {
        return sameAs;
    }

    public void setSameAs(String sameAs) {
        this.sameAs = sameAs;
    }

    @Override
    public String toString() {
        return "Category{" +
                "uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                ", subCategoryOf='" + subCategoryOf + '\'' +
                ", image='" + image + '\'' +
                ", sameAs='" + sameAs + '\'' +
                '}';
    }
}
