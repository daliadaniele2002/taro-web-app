package dalia.daniele.telegram.taro_web_app.repo;

import dalia.daniele.telegram.taro_web_app.domain.activity.entity.ActivityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ActivityRepo extends ReactiveCrudRepository<ActivityEntity, UUID> {
}
