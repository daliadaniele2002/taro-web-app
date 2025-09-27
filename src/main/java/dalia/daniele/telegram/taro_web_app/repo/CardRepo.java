package dalia.daniele.telegram.taro_web_app.repo;

import dalia.daniele.telegram.taro_web_app.domain.card.entity.CardEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CardRepo extends ReactiveCrudRepository<CardEntity, Integer> {

}
