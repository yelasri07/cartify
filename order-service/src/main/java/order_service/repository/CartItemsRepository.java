package order_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import order_service.model.CartItems;

@Repository
public interface CartItemsRepository extends MongoRepository<CartItems, String> {
    
}
