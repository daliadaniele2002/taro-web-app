package dalia.daniele.telegram.taro_web_app.service;

import dalia.daniele.telegram.taro_web_app.domain.card.Card;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CardService {
    private final CardAggregatorService cardAggregatorService;

    public CardService(CardAggregatorService cardAggregatorService) {
        this.cardAggregatorService = cardAggregatorService;
    }

    public Mono<List<Card>> cardsById(List<Integer> ids) {
        return cardAggregatorService.findAllById(ids).collectList();
    }
}
