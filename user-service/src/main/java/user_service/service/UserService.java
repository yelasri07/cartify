package user_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import user_service.model.User;
import user_service.model.DTO.UserDTO.UserOutput;
import user_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Map<String, Object> getUser(String id) {
        User user = userRepository.findById(id).get();

        Map<String, Object> response = new HashMap<>();
        response.put("user_details", UserToUserOutput(user));

        return response;
    }

    private UserOutput UserToUserOutput(User user) {
        return UserOutput.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

}
