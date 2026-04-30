package product_service.dto;

public class ProductDTO {

    public static record ProductInput(
            String name,
            String description,
            Double price,
            Integer quantity) {
    }

}
