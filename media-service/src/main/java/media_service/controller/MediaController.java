package media_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import media_service.service.MediaService;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/images")
    public Map<String, Object> uploadImage(@RequestParam("media") MultipartFile media) {
        // TODO: process POST request

        return mediaService.uploadMedia(media);
    }

    @GetMapping("/images/{id}")
    public String getImage(@PathVariable("id") String imageId) {
        // TODO: process POST request

        return imageId;
    }

}