package dalia.daniele.telegram.taro_web_app.service;

import dalia.daniele.telegram.taro_web_app.domain.activity.ActivityInfo;
import dalia.daniele.telegram.taro_web_app.repo.ActivityRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ActivitiesService {

    private final ActivityRepo activityRepo;

    public ActivitiesService(ActivityRepo activityRepo) {
        this.activityRepo = activityRepo;
    }

    public Mono<Void> save(ActivityInfo activityInfo) {
        return activityRepo.save(activityInfo.toEntity()).then();
    }
}
