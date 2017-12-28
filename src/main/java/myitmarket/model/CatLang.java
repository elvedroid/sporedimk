package myitmarket.model;

public class CatLang {
    private String categoryName;
    private String lang;

    public CatLang() {
    }

    public CatLang(String categoryName, String lang) {
        this.categoryName = categoryName;
        this.lang = lang;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
