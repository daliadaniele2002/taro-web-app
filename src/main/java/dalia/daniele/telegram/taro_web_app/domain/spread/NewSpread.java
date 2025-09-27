package dalia.daniele.telegram.taro_web_app.domain.spread;

import java.util.UUID;

public record NewSpread(
        Long telegramId,
        UUID spreadId,
        GeneratedSpread generatedSpread
) {
}
