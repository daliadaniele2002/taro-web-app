package dalia.daniele.telegram.taro_web_app.domain.exceptions;

import org.springframework.http.HttpStatus;

public class SpreadNotFountException extends CustomException {
    public static final String DEFAULT_MESSAGE = "Spread not found!";

    public SpreadNotFountException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getFrontendMessage() {
        return DEFAULT_MESSAGE;
    }
}
