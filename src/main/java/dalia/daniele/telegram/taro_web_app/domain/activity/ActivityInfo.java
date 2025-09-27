package dalia.daniele.telegram.taro_web_app.domain.activity;

import dalia.daniele.telegram.taro_web_app.domain.activity.entity.ActivityEntity;

public record ActivityInfo(Long telegramId, Activity activity) {
    public ActivityEntity toEntity() {
        return new ActivityEntity(null, telegramId, activity.name());
    }
}
