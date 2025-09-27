package dalia.daniele.telegram.taro_web_app.command;

import dalia.daniele.telegram.taro_web_app.domain.activity.Activity;
import dalia.daniele.telegram.taro_web_app.domain.activity.ActivityInfo;
import dalia.daniele.telegram.taro_web_app.domain.spread.entity.SpreadEntity;
import dalia.daniele.telegram.taro_web_app.domain.user.PersonData;
import dalia.daniele.telegram.taro_web_app.service.ActivitiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public abstract class SpreadCommand<Request, Response> implements Command<Request, Response> {
    private static final Logger logger = LoggerFactory.getLogger(SpreadCommand.class);

    private final ActivitiesService activitiesService;

    protected SpreadCommand(ActivitiesService activitiesService) {
        this.activitiesService = activitiesService;
    }

    protected Mono<Void> getSideEffect(Mono<Tuple2<SpreadEntity, PersonData>> spreadUserData) {
        return spreadUserData
                .doOnSubscribe(__ -> logger.debug("Start side effects"))
                .flatMap(sud ->
                        activitiesService.save(new ActivityInfo(sud.getT1().telegramId(), Activity.INTERPRET_SPREAD)))
                .doOnTerminate(() -> logger.debug("End side effects"));
    }
}
