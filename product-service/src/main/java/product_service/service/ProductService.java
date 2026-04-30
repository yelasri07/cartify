package product_service.service;

import org.springframework.stereotype.Service;

import product_service.dto.ProductDTO;
import product_service.exception.BadRequestException;

@Service
public class ProductService {

    public void createProduct(ProductDTO.ProductInput productData) {
        if (productData.price() < 0) {
            throw new BadRequestException("Product price must be greater or equal than 0");
        }
        if (productData.quantity() < 0) {
            throw new BadRequestException("Product quantity must be greater or equal than 0");
        }
    }

}
