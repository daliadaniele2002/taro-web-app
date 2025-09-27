package dalia.daniele.telegram.taro_web_app.service;

import dalia.daniele.telegram.taro_web_app.domain.card.Card;
import dalia.daniele.telegram.taro_web_app.domain.card.OnServerPicture;
import dalia.daniele.telegram.taro_web_app.domain.card.entity.CardEntity;
import dalia.daniele.telegram.taro_web_app.repo.CardRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class CardAggregatorService {

    private final String pathPrefix;
    private final CardRepo cardRepo;

    public CardAggregatorService(
            @Value("${taro.card.path-prefix}") String pathPrefix,
            CardRepo cardRepo
    ) {
        this.pathPrefix = pathPrefix;
        this.cardRepo = cardRepo;
    }

    public Flux<Card> findAllById(List<Integer> ids) {
        return cardRepo.findAllById(ids)
                .map(this::entityToCard);
    }

    private Card entityToCard(CardEntity entity) {
        var cardPicture = new OnServerPicture(pathPrefix + entity.cardId());
        return new Card(entity.title(), cardPicture);
    }
}
