package media_service.restApi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import media_service.dto.ProductDTO.ProductInput;

@FeignClient(name = "product-service")
public interface ProductClient {
    
    @GetMapping("/api/products/{id}")
    public ProductInput getProduct(@PathVariable("id") String productId);

}
