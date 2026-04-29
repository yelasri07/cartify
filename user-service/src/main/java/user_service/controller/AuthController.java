package user_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import user_service.model.User;
import user_service.service.AuthService;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    
    private final AuthService authService;


    @PostMapping("/register")
    public Map<String, Object>register(@RequestBody User request) {
        return authService.register(request.getName(), request.getEmail(),
                request.getPassword(), request.getRole());
        
    }
    
    @PostMapping("/login")
    public String login(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
}
