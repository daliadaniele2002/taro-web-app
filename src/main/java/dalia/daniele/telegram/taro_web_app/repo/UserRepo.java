package dalia.daniele.telegram.taro_web_app.repo;

import dalia.daniele.telegram.taro_web_app.domain.user.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepo extends ReactiveCrudRepository<UserEntity, UUID> {
    Mono<UserEntity> findByTelegramId(Long telegramId);

    Mono<Boolean> existsByTelegramId(Long telegramId);
}
