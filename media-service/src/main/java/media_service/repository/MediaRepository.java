package media_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import media_service.model.Media;


@Repository
public interface MediaRepository extends MongoRepository<Media, String> {
    
}
