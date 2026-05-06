package product_service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductDTO {

    public static record ProductInput(

            @NotBlank(message = "Product name cannot be empty") @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters") String name,
            @NotBlank(message = "Product description cannot be empty") @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters") String description,
            @NotNull(message = "Product price cannot be null") @DecimalMin(value = "0.0", message = "Price must be greater than 0") @Digits(integer = 10, fraction = 2, message = "Price must have max 2 decimal places") Double price,
            @NotNull(message = "Product quantity cannot be null") @PositiveOrZero(message = "Quantity must be 0 or more") Integer quantity

    ) {

        public ProductInput {
            name = name != null ? name.trim() : null;
            description = description != null ? description.trim() : null;
        }
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static record ProductOutput(
            String id,
            String name,
            String discription,
            Double price,
            Integer quantity,
            @JsonProperty("user_id") String userId,
            List<String> files) {
    }
}