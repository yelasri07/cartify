package product_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import product_service.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
}
