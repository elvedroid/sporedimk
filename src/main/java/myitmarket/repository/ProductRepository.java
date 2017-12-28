package myitmarket.repository;

import org.springframework.data.repository.CrudRepository;
import myitmarket.model.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByName(String name);
}
