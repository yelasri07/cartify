package order_service.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import order_service.dto.CartDTO.CartItemInput;
import order_service.dto.CartDTO.CartItemOutput;
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

        final var existingCartItem = cartItemRepository.findByShoppingCartIdAndProductId(shoppingCart.getId(),
                cartItemData.productId());
        final CartItem cartItem;
        if (existingCartItem.isPresent()) {
            existingCartItem.get().setQuantity(existingCartItem.get().getQuantity() + cartItemData.quantity());
            cartItem = existingCartItem.get();
        } else {
            cartItem = CartItem.builder()
                    .shoppingCartId(shoppingCart.getId())
                    .productId(product.id())
                    .quantity(cartItemData.quantity())
                    .build();
        }

        this.cartItemRepository.save(cartItem);

        return Map.of("message", "Item created successfully",
                "shopping_cart_id", shoppingCart.getId(),
                "product_id", cartItemData.productId(),
                "quantity", cartItemData.quantity());
    }

    public List<CartItemOutput> getItems(final String userId) {
        final Optional<ShoppingCart> existingShoppingCart = shoppingCartRepository.findByUserId(userId);
        if (existingShoppingCart.isEmpty()) {
            return List.of();
        }

        final ShoppingCart shoppingCart = existingShoppingCart.get();

        final List<CartItem> cartItems = cartItemRepository.findByShoppingCartId(shoppingCart.getId());
        final Set<String> productIds = cartItems.stream().map(item -> item.getProductId()).collect(Collectors.toSet());

        Map<String, ProductOutput> products = this.productClient.getProductsCarts(productIds)
                .stream().collect(Collectors.toMap(ProductOutput::id, Function.identity()));

        return cartItems.stream().map(item -> CartItemOutput.builder()
                .id(item.getId())
                .shoppingCartId(item.getShoppingCartId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .product(products.get(item.getProductId()))
                .build()).toList();
    }

}
