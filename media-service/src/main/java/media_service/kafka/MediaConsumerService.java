package media_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MediaConsumerService {
    @KafkaListener(topics = "my-topic", groupId = "mediaGroup")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
    }
}