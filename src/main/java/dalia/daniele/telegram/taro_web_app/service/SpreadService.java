package dalia.daniele.telegram.taro_web_app.service;

import dalia.daniele.telegram.taro_web_app.domain.exceptions.SpreadNotFountException;
import dalia.daniele.telegram.taro_web_app.domain.spread.GeneratedSpread;
import dalia.daniele.telegram.taro_web_app.domain.spread.entity.SpreadEntity;
import dalia.daniele.telegram.taro_web_app.repo.SpreadRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Service
public class SpreadService {

    private final Integer deckSize;
    private final Integer spreadCardsNumber;
    private final SpreadRepo spreadRepo;

    public SpreadService(
            @Value("${taro.deck-size}") Integer deckSize,
            @Value("${taro.spread-cards-num}") Integer spreadCardsNumber,
            SpreadRepo spreadRepo
    ) {
        this.deckSize = deckSize;
        this.spreadCardsNumber = spreadCardsNumber;
        this.spreadRepo = spreadRepo;
    }

    public Mono<GeneratedSpread> generate(Long telegramId, String topic) {
        return Mono.defer(() -> {
                    var isd = getCardsId();
                    return Mono.just(new SpreadEntity(null, telegramId, topic, isd));
                }).flatMap(spreadRepo::save)
                .map(entity -> new GeneratedSpread(entity.spreadId(), entity.cardsId()));
    }

    private List<Integer> getCardsId() {
        return Stream.generate(() -> ThreadLocalRandom.current().nextInt(1, deckSize + 1))
                .limit(spreadCardsNumber)
                .toList();
    }

    public Mono<SpreadEntity> findSpreadById(UUID spreadId) {
        return spreadRepo.findById(spreadId)
                .switchIfEmpty(Mono.error(new SpreadNotFountException()));
    }
}
