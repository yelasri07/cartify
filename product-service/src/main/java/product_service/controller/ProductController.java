package product_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import product_service.dto.ProductDTO.ProductInput;
import product_service.dto.ProductDTO.ProductOutput;
import product_service.service.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductOutput post(@RequestBody @Valid ProductInput productDate) {
        return this.productService.createProduct(productDate);
    }

}
