package order_service.restApi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import order_service.dto.ProductDTO.ProductOutput;

@FeignClient(name = "product-service")
public interface ProductClient {
    
    @GetMapping("/api/products/{id}")
    public ProductOutput get(@PathVariable("id") String productId);

}
