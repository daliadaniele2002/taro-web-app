package dalia.daniele.telegram.taro_web_app.command;

import dalia.daniele.telegram.taro_web_app.domain.card.Card;
import dalia.daniele.telegram.taro_web_app.domain.spread.entity.SpreadEntity;
import dalia.daniele.telegram.taro_web_app.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Component
@Scope("prototype")
public class InterpretSpreadStreamCommand extends SpreadCommand<UUID, Flux<String>> {
    private static final Logger logger = LoggerFactory.getLogger(InterpretSpreadStreamCommand.class);

    private final UserService userService;
    private final AIService aiService;
    private final CardService cardService;
    private final SpreadService spreadService;

    public InterpretSpreadStreamCommand(
            UserService userService,
            AIService aiService,
            CardService cardService,
            SpreadService spreadService,
            ActivitiesService activitiesService
    ) {
        super(activitiesService);
        this.userService = userService;
        this.aiService = aiService;
        this.cardService = cardService;
        this.spreadService = spreadService;
    }

    @Override
    public Flux<String> execute(UUID spreadId) {

        Mono<SpreadEntity> spreadEntity = spreadService.findSpreadById(spreadId).cache();
        var spreadUserData = Mono.zip(
                spreadEntity,
                spreadEntity.flatMap(s -> userService.getPersonData(s.telegramId()))
        ).cache();


        var result = spreadUserData.doOnSubscribe(__ -> logger.debug("Start spread interpret"))
                .flatMapMany(t ->
                        cardService.cardsById(t.getT1().cardsId())
                                .flatMapMany(
                                        cs -> {
                                            var titles = cs.stream().map(Card::title).toList();
                                            var personData = t.getT2();
                                            var spread = t.getT1();
                                            return aiService.interpretSpreadStream(personData, titles, spread.topic());
                                        }
                                )
                )
                .doOnTerminate(() -> logger.debug("End spread interpret"));


        Mono<Void> sideEffect = getSideEffect(spreadUserData);

        return result.publishOn(Schedulers.boundedElastic()).doOnSubscribe(__ -> sideEffect.subscribe());
    }
}
