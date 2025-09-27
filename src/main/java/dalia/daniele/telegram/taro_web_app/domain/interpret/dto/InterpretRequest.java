package dalia.daniele.telegram.taro_web_app.domain.interpret.dto;

import java.util.UUID;

public record InterpretRequest(UUID spreadId, Long telegramId) {
}
