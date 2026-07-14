package order_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import order_service.model.ShoppingCart;

@Repository
public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {
    
}
