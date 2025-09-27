package dalia.daniele.telegram.taro_web_app.domain.spread;

import dalia.daniele.telegram.taro_web_app.domain.card.Card;

import java.util.List;
import java.util.UUID;

public record Spread(
        UUID spreadId,
        List<Card> cards
) {
}
