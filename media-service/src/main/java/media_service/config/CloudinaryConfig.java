package media_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
    @Value("${CLOUDINARY_URL:default}")
    private String cloudUrl;

    @Bean
    public Cloudinary cloudinary(){
        System.out.println("##########################################");
        System.out.println(cloudUrl);
        System.out.println("##########################################");

        return new Cloudinary(cloudUrl);
    }
}