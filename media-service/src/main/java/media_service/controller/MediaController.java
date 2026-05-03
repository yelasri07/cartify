package media_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import media_service.exception.BadRequestException;
import media_service.model.Target;
import media_service.service.MediaService;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/images")
    public Map<String, Object> uploadImage(@RequestParam MultipartFile[] media, @RequestParam Target target, @RequestParam("target_id") String targetId) {

        return mediaService.uploadMedia(media, target, targetId);
    }

}