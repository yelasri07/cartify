package media_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import media_service.model.dto.MediaDTO.MediaInput;
import media_service.service.MediaService;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    // private final MediaProducerService mediaProducerService;

    @PostMapping("/images")
    public Map<String, Object> uploadImage(@ModelAttribute @Valid MediaInput media,
            @AuthenticationPrincipal String userId) throws Exception {
        // mediaProducerService.sendMessage("my-topic", "uploading avatar");
        return mediaService.uploadMedia(media, userId);
    }

    @GetMapping("/images/products/{id}")
    public List<String> getProductMedia(@PathVariable("id") String productId) {
        return this.mediaService.getProductMedia(productId);
    }

    @PostMapping("/images/products")
    public Map<String, List<String>> getMediaProducts(@RequestBody List<String> productIds) {
        return this.mediaService.getMediaProducts(productIds);
    }

}