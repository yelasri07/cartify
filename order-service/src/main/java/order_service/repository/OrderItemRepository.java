package order_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import order_service.model.OrderItem;

@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem, String> {
    
}
