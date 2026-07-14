package order_service.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import order_service.dto.CartDTO.CartItemInput;
import order_service.service.CartService;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Map<String, Object> post(@Valid @RequestBody CartItemInput cartItem,
            @AuthenticationPrincipal String userId) {
        return this.cartService.createCartItem(cartItem, userId);
    }

}
