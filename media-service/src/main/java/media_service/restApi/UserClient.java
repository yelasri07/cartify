package media_service.restApi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import media_service.dto.ProductDTO.ProductInput;
import media_service.dto.UserDTO.AvatarInput;

@FeignClient(name = "user-service")
public interface UserClient {
    
    @PutMapping("/users/me")
    public ProductInput updateAvatar(@RequestBody AvatarInput avatarInput);

}
