package order_service.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import order_service.dto.OrderDTO.SoldProductOutput;
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

    public List<SoldProductOutput> getSellerOrders(String currentUserId, String status) {
        List<OrderDetails> orders = orderDetailsRepository.findAll();
        if (status != null && !status.isEmpty()) {
            orders = orders.stream()
                    .filter(o -> o.getStatus() != null && o.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }

        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        java.util.Map<String, OrderDetails> orderMap = orders.stream()
                .collect(Collectors.toMap(OrderDetails::getId, o -> o));

        List<OrderItem> orderItems = orderItemsRepository.findAll().stream()
                .filter(item -> orderMap.containsKey(item.getOrderId()))
                .toList();

        if (orderItems.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        java.util.Set<String> productIds = orderItems.stream()
                .map(OrderItem::getProductId)
                .collect(java.util.stream.Collectors.toSet());

        List<ProductOutput> products = productClient.getProductsCarts(productIds);

        java.util.Map<String, ProductOutput> sellerProductsMap = products.stream()
                .filter(p -> p.userId() != null && p.userId().equals(currentUserId))
                .collect(java.util.stream.Collectors.toMap(ProductOutput::id, p -> p));

        List<order_service.dto.OrderDTO.SoldProductOutput> result = new java.util.ArrayList<>();
        for (OrderItem item : orderItems) {
            if (sellerProductsMap.containsKey(item.getProductId())) {
                ProductOutput product = sellerProductsMap.get(item.getProductId());
                OrderDetails order = orderMap.get(item.getOrderId());

                result.add(order_service.dto.OrderDTO.SoldProductOutput.builder()
                        .orderId(order.getId())
                        .orderStatus(order.getStatus().name())
                        .productId(product.id())
                        .productName(product.name())
                        .quantity(item.getQuantity())
                        .price(item.getCheckoutPrice())
                        .createdAt(order.getCreatedAt())
                        .build());
            }
        }

        result.sort((a, b) -> {
            if (a.createdAt() == null || b.createdAt() == null) return 0;
            return b.createdAt().compareTo(a.createdAt());
        });

        return result;
    }

    public OrderItem cartItemToOrderItem(CartItem cartItem, String orderId, double price) {
        return OrderItem.builder()
                .orderId(orderId)
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .checkoutPrice(price)
                .build();

    }

}
