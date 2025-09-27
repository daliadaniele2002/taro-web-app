package dalia.daniele.telegram.taro_web_app.handlers;

import dalia.daniele.telegram.taro_web_app.domain.exceptions.CustomException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public Mono<ProblemDetail> handleAny(Throwable ex, ServerWebExchange exchange) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("Unhandled error: {} {} -> {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                ex.toString(), ex);

        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Internal Server Error");
        pd.setDetail(ex.getMessage());
        enrich(pd, exchange);
        return Mono.just(pd);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ProblemDetail> handleBind(WebExchangeBindException ex, ServerWebExchange exchange) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logger.warn("Validation failed: {} {} -> {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Validation failed");
        pd.setDetail("Request validation error");
        pd.setProperty("errors", ex.getFieldErrors().stream()
                .map(fe -> {
                    assert fe.getDefaultMessage() != null;
                    return Map.of("field", fe.getField(), "message", fe.getDefaultMessage());
                })
                .toList());
        enrich(pd, exchange);
        return Mono.just(pd);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ProblemDetail> handleConstraint(ConstraintViolationException ex, ServerWebExchange exchange) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logger.warn("Constraint violation: {} {} -> {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Constraint violation");
        pd.setDetail(ex.getMessage());
        enrich(pd, exchange);
        return Mono.just(pd);
    }

    @ExceptionHandler(CustomException.class)
    public Mono<ProblemDetail> handleRSE(CustomException ex, ServerWebExchange exchange) {
        logger.info("ResponseStatusException: {} {} -> {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                ex.getHttpStatus());
        ProblemDetail pd = ProblemDetail.forStatus(ex.getHttpStatus());
        pd.setTitle(ex.getHttpStatus().getReasonPhrase());
        pd.setDetail(ex.getFrontendMessage());
        enrich(pd, exchange);
        return Mono.just(pd);
    }

    private void enrich(ProblemDetail pd, ServerWebExchange exchange) {
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("path", exchange.getRequest().getPath().value());
        exchange.getRequest().getMethod();
        pd.setProperty("method", exchange.getRequest().getMethod().name());
        pd.setProperty("requestId", exchange.getRequest().getId());
    }
}
