package order_service.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import order_service.dto.CartDTO.CartItemInput;
import order_service.dto.ProductDTO.ProductOutput;
import order_service.model.CartItem;
import order_service.model.ShoppingCart;
import order_service.repository.CartItemRepository;
import order_service.repository.ShoppingCartRepository;
import order_service.restApi.ProductClient;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    public Map<String, Object> createCartItem(final CartItemInput cartItemData, final String userId) {
        ProductOutput product = this.productClient.get(cartItemData.productId());

        // Create a shopping cart if not exists
        Optional<ShoppingCart> existingShoppingCart = this.shoppingCartRepository.findByUserId(userId);
        if (existingShoppingCart.isEmpty()) {
            existingShoppingCart = Optional.of(shoppingCartRepository.insert(ShoppingCart.builder()
                    .userId(userId)
                    .build()));
        }

        final ShoppingCart shoppingCart = existingShoppingCart.get();
        final CartItem cartItem = CartItem.builder()
                .shoppingCartId(shoppingCart.getId())
                .productId(product.id())
                .quantity(cartItemData.quantity())
                .build();

        this.cartItemRepository.insert(cartItem);

        return Map.of("message", "Item created successfully",
                "shopping_cart_id", shoppingCart.getId(),
                "product_id", cartItemData.productId(),
                "quantity", cartItemData.quantity());
    }

}
