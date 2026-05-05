package media_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import media_service.model.dto.MediaDTO.MediaInput;
import media_service.service.MediaService;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    // private final MediaProducerService mediaProducerService;

    @PostMapping("/images")
    public Map<String, Object> uploadImage(@ModelAttribute @Valid MediaInput media, @AuthenticationPrincipal String userId) throws Exception {
        // mediaProducerService.sendMessage("my-topic", "uploading avatar");
        return mediaService.uploadMedia(media, userId);
    }

}