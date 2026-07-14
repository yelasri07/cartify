package order_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import order_service.model.OrderDetails;

@Repository
public interface OrderDetailsRepository extends MongoRepository<OrderDetails, String> {

}
