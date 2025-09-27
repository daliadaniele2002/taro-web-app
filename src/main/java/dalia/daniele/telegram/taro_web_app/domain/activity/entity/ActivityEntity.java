package dalia.daniele.telegram.taro_web_app.domain.activity.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("activities")
public record ActivityEntity(
        @Id
        @Column("activity_id")
        UUID activityId,
        @Column("telegram_id")
        Long telegramId,
        String action
) {
}
