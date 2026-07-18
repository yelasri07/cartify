package order_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import lombok.RequiredArgsConstructor;
import order_service.exception.NotFoundException;
import order_service.model.CartItem;
import order_service.model.OrderDetails;
import order_service.model.OrderItem;
import order_service.model.ShoppingCart;
import order_service.repository.CartItemRepository;
import order_service.repository.OrderDetailsRepository;
import order_service.repository.OrderItemRepository;
import order_service.repository.ShoppingCartRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderItemRepository orderItemsRepository;

    public Map<String, Object> createOrder(String currentUserId) {

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new NotFoundException("Whoops! shopping cart not found."));

        OrderDetails orderDetails = OrderDetails.builder()
                .userId(currentUserId)
                .status("PENDING")
                .total(0.)
                .build();
        OrderDetails savedOrder = orderDetailsRepository.save(orderDetails);

        List<OrderItem> orderItems = cartItemRepository.findByShoppingCartId(shoppingCart.getId())
                .stream().map(item -> cartItemToOrderItem(item, savedOrder.getId())).toList();

        orderItemsRepository.saveAll(orderItems);

        Map<String, Object> response = new HashMap<>();
        response.put("order_details", savedOrder);

        return response;
    }

    public List<OrderDetails> getMyOrders(int page, int size, String currentUserID) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");


        return orderDetailsRepository.findAllByUserId(currentUserID, pageable);
    }

    public OrderDetails getOrderById(String orderId, String currentUserID) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Whoops! order  not found."));
        if (orderDetails.getUserId() != currentUserID) {
            throw new NotFoundException("Whoops! order not found for you.");
        }
        System.out.println(currentUserID);
        System.out.println(orderDetails.getUserId());
        return orderDetails;
    }

    public List<OrderItem> getOrderItems(String orderId, String currentUserID) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Whoops! order  not found."));
        if (orderDetails.getUserId() != currentUserID) {
            throw new NotFoundException("Whoops! order not found for you.");
        }
        System.out.println(currentUserID);
        System.out.println(orderDetails.getUserId());
        return orderItemsRepository.findAllByOrderId(orderId);
    }

    public OrderItem cartItemToOrderItem(CartItem cartItem, String orderId) {
        return OrderItem.builder()
                .orderId(orderId)
                .productId(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .build();

    }

}
