package dalia.daniele.telegram.taro_web_app.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewUser(@NotNull Long id, @NotEmpty String name, @NotEmpty String birthDate) {
}
