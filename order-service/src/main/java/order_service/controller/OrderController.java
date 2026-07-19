package order_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import order_service.model.OrderDetails;
import order_service.model.OrderItem;
import order_service.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    // ---------- Checkout ----------

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Map<String, Object> createOrder(@AuthenticationPrincipal String currentUserId) throws Exception {
        return orderService.createOrder(currentUserId);
    }

    // ---------- Client-facing ----------

    @GetMapping
    public List<OrderDetails> getMyOrders(@RequestParam(required = false) String status,
            @AuthenticationPrincipal String currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // TODO: filter
        return orderService.getMyOrders(page, size, currentUserId);
    }

    @GetMapping("/{id}")
    public OrderDetails getOrderById(@PathVariable String id, @AuthenticationPrincipal String currentUserId) {
        return orderService.getOrderById(id, currentUserId);
    }

    @GetMapping("/{id}/items")
    public List<OrderItem> getOrderItems(@PathVariable String id, @AuthenticationPrincipal String currentUserId) {
        return orderService.getOrderItems(id, currentUserId);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        // TODO: cancel order if status allows it
        return null;
    }

    @PostMapping("/{id}/redo")
    public ResponseEntity<?> redoOrder(@PathVariable String id) {
        // TODO: create new order using old order's items
        return null;
    }

    // ---------- Seller-facing ----------

    @GetMapping("/seller")
    public ResponseEntity<?> getSellerOrders(@RequestParam(required = false) String status) {
        // TODO: list orders containing this seller's products
        return null;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String id, @RequestBody Object request) {
        // TODO: seller updates order status
        return null;
    }

}