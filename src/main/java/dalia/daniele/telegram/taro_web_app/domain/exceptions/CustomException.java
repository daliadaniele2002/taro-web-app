package dalia.daniele.telegram.taro_web_app.domain.exceptions;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
    public abstract String getFrontendMessage();
}
