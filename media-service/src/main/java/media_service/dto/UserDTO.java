package media_service.dto;

import jakarta.validation.constraints.NotEmpty;

public class UserDTO {
    public static record AvatarInput(
            @NotEmpty(message = "User id cannot be empty") String userId,
            @NotEmpty(message = "Avatar url cannot be empty") String avatarUrl) {

    }
}
