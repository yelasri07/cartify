package media_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import media_service.repository.MediaRepository;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private String location = "upload-dir";

    public Map<String, Object> uploadMedia(MultipartFile media) {


        Map<String, Object> response = new HashMap<>();
        return response;
    }

}
