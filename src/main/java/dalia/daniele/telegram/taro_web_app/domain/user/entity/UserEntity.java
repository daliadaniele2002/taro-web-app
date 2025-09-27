package dalia.daniele.telegram.taro_web_app.domain.user.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("users")
public record UserEntity(
        @Id
        @Column("user_id")
        UUID userId,
        @Column("telegram_id")
        Long telegramId,
        @Column("name")
        String name,
        @Column("birth_date")
        String birthDate) {
}
