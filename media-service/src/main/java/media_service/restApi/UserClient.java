package media_service.restApi;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import media_service.dto.UserDTO.AvatarInput;

@FeignClient(name = "user-service")
public interface UserClient {
    
    @PutMapping("/users/me")
    public Map<String, Object> updateAvatar(@RequestBody AvatarInput avatarInput);

}
