package user_service.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import user_service.service.UserService;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String getMethodName() {
        return "Hello users!";
    }

    @GetMapping("/me")
    public Map<String, Object> getProfile(@AuthenticationPrincipal String id) {
        return userService.getUser(id);
    }

    // @PutMapping("/me/avatar")
    // public String updateAvatar(@PathVariable String id, @RequestBody String
    // entity) {

    // return entity;
    // }

}
