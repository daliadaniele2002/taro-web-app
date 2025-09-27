package dalia.daniele.telegram.taro_web_app.llm;

import dalia.daniele.telegram.taro_web_app.domain.AiRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiLlm implements LlmClient<AiRequest> {
    private static final Logger logger = LoggerFactory.getLogger(OpenAiLlm.class);

    private final WebClient openAiClient;

    public OpenAiLlm(WebClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    @Override
    public Mono<String> complete(AiRequest aiRequest) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini", // или выноси в конфиг
                "n", 1,
                "messages", List.of(
                        Map.of("role", "system", "content", aiRequest.system()),
                        Map.of("role", "user", "content", aiRequest.user())
                ),
                "temperature", 0.7
        );

        return openAiClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(json -> {
                    // вытаскиваем первый choice.message.content
                    var choices = (List<Map<String, Object>>) json.get("choices");
                    if (choices == null || choices.isEmpty()) return "";
                    var message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                })
                .timeout(Duration.ofSeconds(20))
                .doOnError(t -> logger.error("Error", t))
                .doOnSubscribe(__ -> logger.debug("Start generation at: {}", Instant.now()))
                .doOnTerminate(() -> logger.debug("End generation at: {}", Instant.now()));
    }

    @Override
    public Flux<String> completeStream(AiRequest aiRequest) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini", // или выноси в конфиг
                "n", 1,
                "messages", List.of(
                        Map.of("role", "system", "content", aiRequest.system()),
                        Map.of("role", "user", "content", aiRequest.user())
                ),
                "temperature", 0.7
        );

        return openAiClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.ACCEPT, "text/event-stream")
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(String.class)
                .onBackpressureBuffer();
    }
}
