package dalia.daniele.telegram.taro_web_app.controller;

import dalia.daniele.telegram.taro_web_app.domain.user.RegistrationStatus;
import dalia.daniele.telegram.taro_web_app.domain.user.dto.NewUser;
import dalia.daniele.telegram.taro_web_app.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@Valid @RequestBody Mono<NewUser> user) {
        return user.flatMap(userService::save).then();
    }

    @GetMapping("/{telegramId}/registrationStatus")
    @ResponseStatus(HttpStatus.OK)
    public Mono<RegistrationStatus> registrationStatus(@PathVariable @NotNull Long telegramId) {
        return userService.checkRegistration(telegramId);
    }
}
