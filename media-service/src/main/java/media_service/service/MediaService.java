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
import java.util.UUID;

import org.apache.kafka.common.protocol.types.Field.Str;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import media_service.dto.ProductDTO.ProductInput;
import media_service.dto.UserDTO.AvatarInput;
import media_service.exception.BadRequestException;
import media_service.model.Media;
import media_service.model.Target;
import media_service.model.dto.MediaDTO.MediaInput;
import media_service.repository.MediaRepository;
import media_service.restApi.ProductClient;
import media_service.restApi.UserClient;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private String product_dir = "upload-dir/products";
    private String user_dir = "upload-dir/avatars";

    public Map<String, Object> uploadMedia(MediaInput mediaInput, String userId) {
        if (mediaInput.files() == null || mediaInput.files().length == 0) {
            throw new BadRequestException("No files uploaded");
        }

        for (MultipartFile file : mediaInput.files()) {
            FileValidator.validateImage(file);
        }

        if (mediaInput.target() == Target.USER && mediaInput.files().length > 1) {
            throw new BadRequestException("Users accept only one media");
        } else if (mediaInput.target() == Target.PRODUCT && mediaInput.files().length > 5) {
            throw new BadRequestException("Products accept maximum 5 media");
        }

        if (mediaInput.target() == Target.PRODUCT) {
            ProductInput product = productClient.getProduct(mediaInput.targetId());
            System.out.println(product);
        }

        String location = mediaInput.target() == Target.PRODUCT ? product_dir : user_dir;
        Map<String, Object> response = new HashMap<>();
        List<String> message = new ArrayList<>();
        for (MultipartFile file : mediaInput.files()) {
            String filePath = this.saveFile(mediaInput, location, file);
            if (mediaInput.target() == Target.PRODUCT) {
                Media media = Media.builder()
                        .productId(mediaInput.targetId())
                        .imagePath(filePath)
                        .build();
                mediaRepository.save(media);
            }
            message.add(filePath);
        }

        if (mediaInput.target() == Target.USER) {
            AvatarInput avatarInput = new AvatarInput(userId, message.get(0));
            Map<String, Object> res = userClient.updateAvatar(avatarInput);
            System.out.println(res);
        }
        response.put("files", message);
        return response;

    }

    private String saveFile(MediaInput mediaInput, String location, MultipartFile file) {
        try {
            Path uploadPath = Paths.get(location);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // get media extension
            String fileName = file.getOriginalFilename();
            String extension = "";

            if (fileName != null && fileName.contains(".")) {
                extension = fileName.substring(fileName.lastIndexOf("."));
            }
            fileName = mediaInput.target() == Target.PRODUCT
                    ? UUID.randomUUID().toString() + extension
                    : mediaInput.targetId() + extension;

            Path filePath = uploadPath.resolve(fileName);
            try (FileOutputStream fos = new FileOutputStream(filePath.toString())) {
                byte[] bytes = file.getBytes();
                fos.write(bytes);
            }

            return "/media/images/" + filePath;

        } catch (IOException | IllegalStateException e) {
            throw new InternalError("Upload failed: " + e.getMessage());
        }
    }

}
