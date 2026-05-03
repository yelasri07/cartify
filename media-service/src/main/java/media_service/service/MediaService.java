package media_service.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import media_service.exception.BadRequestException;
import media_service.model.Media;
import media_service.model.Target;
import media_service.repository.MediaRepository;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private String product_dir = "upload-dir/products";
    private String user_dir = "upload-dir/avatars";

    public Map<String, Object> uploadMedia(MultipartFile[] medias, Target target, String targetId) {
        
        if (target == Target.USER && medias.length > 1) {
            throw new BadRequestException("Users accept only one media");
        } else if (target == Target.PRODUCT && medias.length > 5) {
            throw new BadRequestException("Products accept maximum 5 media");
        }

        String location = target == Target.PRODUCT ? product_dir : user_dir;
        Map<String, Object> response = new HashMap<>();
        List<String> message = new ArrayList<>();
        for (MultipartFile media : medias) {
            try {
                Path uploadPath = Paths.get(location);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                // get media extension
                String fileName = media.getOriginalFilename();
                String extension = "";

                if (fileName != null && fileName.contains(".")) {
                    extension = fileName.substring(fileName.lastIndexOf("."));
                }
                fileName = target == Target.PRODUCT ? targetId + "-" + media.getOriginalFilename()
                        : targetId + extension;

                Path filePath = uploadPath.resolve(fileName);
                try (FileOutputStream fos = new FileOutputStream(filePath.toString())) {
                    byte[] bytes = media.getBytes();
                    fos.write(bytes);
                }
                message.add("File uploaded: " + filePath.getFileName());

            } catch (IOException | IllegalStateException e) {
                throw new InternalError("Upload failed: " + e.getMessage());
            }
        }

        if (target == Target.PRODUCT) {
            Media media = Media.builder()
                    .productId(targetId)
                    .imagePath(location + "/" + targetId)
                    .build();
            mediaRepository.save(media);
        }
        response.put("message", message);
        return response;

    }

}
