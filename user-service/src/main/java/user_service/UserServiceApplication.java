package user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class UserServiceApplication {

	@GetMapping("/users")
	public String getMethodName() {
		return "Hello users!";
	}
	

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
