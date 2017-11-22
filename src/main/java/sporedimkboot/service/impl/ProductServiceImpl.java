package sporedimkboot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sporedimkboot.model.*;
import sporedimkboot.repository.ProductRepository;
import sporedimkboot.service.JenaTemplate;
import sporedimkboot.service.ProductService;
import sporedimkboot.utils.QueryUtils;
import sporedimkboot.utils.UpdateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import static sporedimkboot.utils.Constants.*;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private Model storesModel, offersModel, productsModel, categoriesModel, favoritesModel;
    private JenaTemplate jenaTemplate;
    private HashMap categories;
    private String categoriesJson;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void init() {
        initModels();
//        initCategories();
    }

    private void initCategories() {
        categories = new HashMap();
        InputStream in = FileManager.get().open(FILE_CATEGORIES);
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + FILE_CATEGORIES + " not found");
        }
        try {
            categoriesJson = IOUtils.toString(in, Charset.forName("UTF-8"));
            categories = new ObjectMapper().readValue(categoriesJson, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initModels() {
        storesModel = ModelFactory.createDefaultModel();
        offersModel = ModelFactory.createDefaultModel();
        productsModel = ModelFactory.createDefaultModel();
        categoriesModel = ModelFactory.createDefaultModel();
        favoritesModel = ModelFactory.createDefaultModel();

        try {
            UpdateUtils.setUpLocalModel(storesModel, FILE_STORES);
            UpdateUtils.setUpLocalModel(offersModel, FILE_OFFERS);
            UpdateUtils.setUpLocalModel(productsModel, FILE_PRODUCTS);
            UpdateUtils.setUpLocalModel(categoriesModel, FILE_CATEGORIES_TTL);
            UpdateUtils.setUpLocalModel(favoritesModel, FILE_FAVORITES);
        } catch (Exception e) {
            e.printStackTrace();
        }

        categoriesModel.write(System.out, "TURTLE");
    }

    @Override
    public void addSeller(Seller seller) {
        try {
            UpdateUtils.setUpLocalModel(storesModel, FILE_STORES_BASE);
            UpdateUtils.setUpLocalModel(offersModel, FILE_BASE);
            UpdateUtils.setUpLocalModel(productsModel, FILE_BASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Offer o : seller.getOffers()) {
            UpdateUtils.addOfferingToSeller(storesModel,
                    seller.getName(),
                    UpdateUtils.createOfferResource(offersModel, productsModel, o));
        }
        UpdateUtils.writeModelToFile(productsModel, FILE_PRODUCTS);
        UpdateUtils.writeModelToFile(offersModel, FILE_OFFERS);
        UpdateUtils.writeModelToFile(storesModel, FILE_STORES);
        System.out.println("Added offers");
    }

    @Override
    public List<Category> getAllCategories() {
        return null;
    }

    @Override
    public List<Category> getLeafCategories(String lang) {
        return QueryUtils.getLeafCateogries(categoriesModel, lang);
    }

    @Override
    public List<Category> getRootCategories(String lang) {
        return QueryUtils.getRootCateogries(categoriesModel, lang);
    }

    @Override
    public List<Category> getSubcategories(CatLang fromCategory) {
        return QueryUtils.getSubcategories(categoriesModel, fromCategory);
    }

    public List<Category> getCategoriesOrSubcategories(String fromCategory) {
        return null;
    }

    @Override
    public List<Offer> getOffersPerCategory(CatLang category) {
        return QueryUtils.getOffersPerCategory(offersModel.union(productsModel), category);
    }

    @Override
    public List<ProductOffers> getOffersWithSameProduct(Product product) {
        return QueryUtils.getOffersWithSameProduct(storesModel.union(offersModel).union(productsModel), product);
    }

    @Override
    public String getCategoriesJson() {
        return categoriesJson.replaceAll("\"","\\\\\"");
    }

    @Override
    public void addProductToFavorites(Favorites favorites) {
        UpdateUtils.updateFavorites(favoritesModel, productsModel, favorites);
    }

    @Override
    public Boolean isProductFavorite(Favorites favorites) {
        return QueryUtils.isProductFavorite(favoritesModel.union(productsModel), favorites);
    }

    @Override
    public Product findProductByName(String name) {
        return productRepository.findByName(name) != null ? productRepository.findByName(name).get(0) : null;
    }

    @Override
    public List<Offer> getMyFavoriteProducts(String appId) {
        return QueryUtils.getMyFavoriteProducts(offersModel.union(productsModel).union(favoritesModel), appId);
    }

    @Override
    public List<Offer> getMostFavoriteProducts() {
        return QueryUtils.getMostFavoriteProducts(offersModel.union(productsModel).union(favoritesModel));
    }

}
