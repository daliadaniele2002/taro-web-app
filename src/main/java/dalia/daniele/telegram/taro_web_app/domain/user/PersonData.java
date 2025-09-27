package dalia.daniele.telegram.taro_web_app.domain.user;

import dalia.daniele.telegram.taro_web_app.domain.user.entity.UserEntity;

public record PersonData(String name, String birthDate) {
    public static PersonData fromUserEntity(UserEntity entity) {
        return new PersonData(entity.name(), entity.birthDate());
    }
}
