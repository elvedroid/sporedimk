package sporedimkboot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sporedimkboot.model.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByName(String name);
}
