package dalia.daniele.telegram.taro_web_app.command;

import dalia.daniele.telegram.taro_web_app.domain.activity.Activity;
import dalia.daniele.telegram.taro_web_app.domain.activity.ActivityInfo;
import dalia.daniele.telegram.taro_web_app.domain.spread.Spread;
import dalia.daniele.telegram.taro_web_app.domain.spread.dto.SpreadRequest;
import dalia.daniele.telegram.taro_web_app.service.ActivitiesService;
import dalia.daniele.telegram.taro_web_app.service.CardService;
import dalia.daniele.telegram.taro_web_app.service.SpreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Scope("prototype")
public class GenerateSpreadCommand implements Command<SpreadRequest, Mono<Spread>> {
    private static final Logger logger = LoggerFactory.getLogger(GenerateSpreadCommand.class);

    private final CardService cardService;
    private final SpreadService spreadService;
    private final ActivitiesService activitiesService;

    public GenerateSpreadCommand(
            CardService cardService,
            SpreadService spreadService,
            ActivitiesService activitiesService
    ) {
        this.cardService = cardService;
        this.spreadService = spreadService;
        this.activitiesService = activitiesService;
    }

    @Override
    public Mono<Spread> execute(SpreadRequest request) {
        final var telegramId = request.telegramId();

        var spread = spreadService.generate(telegramId, request.topic()).cache();

        var result = spread.doOnSubscribe(__ -> logger.debug("Start spread assembly"))
                .flatMap(gs ->
                        cardService.cardsById(gs.cardsId()).map(cs -> new Spread(gs.spreadId(), cs))
                ).doOnTerminate(() -> logger.debug("End spread assembly"));

        var sideEffect = spread.doOnSubscribe(__ -> logger.debug("Start side effects"))
                .flatMap(s ->
                        activitiesService.save(new ActivityInfo(telegramId, Activity.GENERATE_SPREAD))
                ).doOnTerminate(() -> logger.debug("End side effects"));

        return result.publishOn(Schedulers.boundedElastic()).doOnSubscribe(__ -> sideEffect.subscribe());
    }
}
