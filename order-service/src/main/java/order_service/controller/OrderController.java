package order_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/orders")
public class OrderController {

    // ---------- Checkout ----------

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Object request) {
        // TODO: create order from cart, snapshot items, set status PENDING, clear cart
        return null;
    }

    // ---------- Client-facing ----------

    @GetMapping
    public ResponseEntity<?> getMyOrders(@RequestParam(required = false) String status) {
        // TODO: list orders for logged-in client (filter by status/date, paginate)
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        // TODO: fetch order + items, check ownership
        return null;
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable String id) {
        // TODO: fetch order_items for this order
        return null;
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