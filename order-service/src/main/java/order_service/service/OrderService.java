package order_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import order_service.dto.ProductDTO.ProductOutput;
import order_service.exception.BadRequestException;
import order_service.exception.NotFoundException;
import order_service.model.CartItem;
import order_service.model.OrderDetails;
import order_service.model.OrderItem;
import order_service.model.OrderStatus;
import order_service.model.ShoppingCart;
import order_service.repository.CartItemRepository;
import order_service.repository.OrderDetailsRepository;
import order_service.repository.OrderItemRepository;
import order_service.repository.ShoppingCartRepository;
import order_service.restApi.ProductClient;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderItemRepository orderItemsRepository;

    private final ProductClient productClient;

    public Map<String, Object> createOrder(String currentUserId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new NotFoundException("Whoops! shopping cart not found."));

        OrderDetails orderDetails = OrderDetails.builder()
                .userId(currentUserId)
                .status(OrderStatus.ORDERED)
                .total(0.)
                .build();

        OrderDetails savedOrder = orderDetailsRepository.save(orderDetails);

        List<CartItem> cartItems = cartItemRepository.findByShoppingCartId(shoppingCart.getId());
        List<OrderItem> orderItems = cartItems.stream().map(item -> {
            ProductOutput product = this.productClient.get(item.getProductId());
            if (product.quantity() < item.getQuantity()) {
                throw new BadRequestException("Not enough available quantity.");
            }
            return cartItemToOrderItem(item, savedOrder.getId(), product.price());
        }).toList();

        orderItemsRepository.saveAll(orderItems);

        shoppingCartRepository.delete(shoppingCart);
        cartItemRepository.deleteAll(cartItems);

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
        if (!orderDetails.getUserId().equals(currentUserID)) {
            throw new NotFoundException("Whoops! order not found for you.");
        }
        return orderDetails;
    }

    public List<OrderItem> getOrderItems(String orderId, String currentUserID) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Whoops! order  not found."));
        if (!orderDetails.getUserId().equals(currentUserID)) {
            throw new NotFoundException("Whoops! order not found for you.");
        }
        System.out.println(currentUserID);
        System.out.println(orderDetails.getUserId());
        return orderItemsRepository.findAllByOrderId(orderId);
    }

    public OrderDetails cancelOrder(String orderId) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Whoops! order  not found."));
        orderDetails.setStatus(OrderStatus.CANCELLED);

        return orderDetailsRepository.save(orderDetails);

    }

    public OrderDetails redoOrder(String orderId) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Whoops! order  not found."));
        orderDetails.setStatus(OrderStatus.ORDERED);

        return orderDetailsRepository.save(orderDetails);

    }

    public OrderItem cartItemToOrderItem(CartItem cartItem, String orderId, double price) {
        return OrderItem.builder()
                .orderId(orderId)
                .productId(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .checkoutPrice(price)
                .build();

    }

}
