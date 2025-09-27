package dalia.daniele.telegram.taro_web_app.domain.spread.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("spreads")
public record SpreadEntity(
        @Id
        @Column("spread_id")
        UUID spreadId,
        @Column("telegram_id")
        Long telegramId,
        String topic,
        @Column("cards_id")
        List<Integer> cardsId
) {
}
