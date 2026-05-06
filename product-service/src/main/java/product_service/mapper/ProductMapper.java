package product_service.mapper;

import java.util.List;

import product_service.dto.ProductDTO.ProductOutput;
import product_service.model.Product;

public class ProductMapper {

    public static ProductOutput toProductOutputDto(Product product, List<String> files) {
        return ProductOutput.builder()
                .id(product.getId())
                .name(product.getName())
                .discription(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .userId(product.getUserId())
                .files(files)
                .build();
    }

}
