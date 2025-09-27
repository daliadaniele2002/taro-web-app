package dalia.daniele.telegram.taro_web_app.repo;

import dalia.daniele.telegram.taro_web_app.domain.spread.entity.SpreadEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface SpreadRepo extends ReactiveCrudRepository<SpreadEntity, UUID> {
}
