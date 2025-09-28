package dalia.daniele.telegram.taro_web_app.domain.card.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("cards")
public record CardEntity(
        @Id
        @Column("card_id")
        Long cardId,
        String title,
        @Column("file_name")
        String fileName
) {
}
