package order_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CartDTO {

    public static record CartItemInput(
            @JsonProperty("product_id") @NotNull(message = "Product id cannot be empty  ") String productId,
            @NotNull(message = "Product quantity cannot be empty") @Positive(message = "Quantity must be 1 or more") Integer quantity) {
    }

}
