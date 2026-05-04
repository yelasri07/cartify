package media_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import media_service.kafka.MediaProducerService;
import media_service.model.dto.MediaDTO.MediaInput;
import media_service.service.MediaService;
import java.util.Map;

import org.apache.tika.Tika;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final MediaProducerService mediaProducerService;

    @PostMapping("/images")
    public Map<String, Object> uploadImage(@ModelAttribute @Valid MediaInput media) {
        mediaProducerService.sendMessage("my-topic", "uploading avatar");
        return mediaService.uploadMedia(media);
    }

}