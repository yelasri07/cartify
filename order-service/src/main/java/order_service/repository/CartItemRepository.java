package order_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import order_service.model.CartItem;

@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {
    
}
