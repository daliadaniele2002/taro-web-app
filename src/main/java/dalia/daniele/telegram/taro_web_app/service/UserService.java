package dalia.daniele.telegram.taro_web_app.service;

import dalia.daniele.telegram.taro_web_app.domain.exceptions.UserNotFountException;
import dalia.daniele.telegram.taro_web_app.domain.user.PersonData;
import dalia.daniele.telegram.taro_web_app.domain.user.RegistrationStatus;
import dalia.daniele.telegram.taro_web_app.domain.user.dto.NewUser;
import dalia.daniele.telegram.taro_web_app.domain.user.entity.UserEntity;
import dalia.daniele.telegram.taro_web_app.repo.UserRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Mono<Void> save(NewUser newUser) {
        UserEntity userEntity = new UserEntity(
                null,
                newUser.id(),
                newUser.name(),
                newUser.birthDate()
        );

        return userRepo.save(userEntity).then();
    }

    public Mono<PersonData> getPersonData(Long telegramId) {
        return userRepo.findByTelegramId(telegramId)
                .map(PersonData::fromUserEntity)
                .switchIfEmpty(Mono.error(new UserNotFountException()));
    }

    public Mono<RegistrationStatus> checkRegistration(Long telegramId) {
        return userRepo.existsByTelegramId(telegramId)
                .map(result -> result ? RegistrationStatus.REGISTERED : RegistrationStatus.NOT_REGISTERED);
    }
}
