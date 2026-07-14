package order_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import order_service.model.OrderItems;

@Repository
public interface OrderItemsRepository extends MongoRepository<OrderItems, String> {
    
}
