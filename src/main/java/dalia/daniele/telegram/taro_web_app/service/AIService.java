package dalia.daniele.telegram.taro_web_app.service;

import dalia.daniele.telegram.taro_web_app.domain.AiRequest;
import dalia.daniele.telegram.taro_web_app.domain.user.PersonData;
import dalia.daniele.telegram.taro_web_app.llm.LlmClient;
import dalia.daniele.telegram.taro_web_app.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private final LlmClient<AiRequest> llmClient;

    public AIService(LlmClient<AiRequest> llmClient) {
        this.llmClient = llmClient;
    }

    public Mono<String> interpretSpread(PersonData personData, List<String> cardTitles, String topic) {
        logger.debug("Generate spread");

        var userMessage = toUserMessage(personData, cardTitles, topic);

        var aiRequest = new AiRequest(userMessage, Constants.SYSTEM_PROMPT);

        return llmClient.complete(aiRequest);
    }

    public Flux<String> interpretSpreadStream(PersonData personData, List<String> cardTitles, String topic) {
        logger.debug("Generate spread stream");

        var userMessage = toUserMessage(personData, cardTitles, topic);

        var aiRequest = new AiRequest(userMessage, Constants.SYSTEM_PROMPT);

        return llmClient.completeStream(aiRequest);
    }

    private String toUserMessage(PersonData personData, List<String> cardTitles, String topic) {
        return "name: " + personData.name()
                + "\ndate of birth: " + personData.birthDate()
                + "\ntopic: " + topic
                + "\ncards: " + String.join(", ", cardTitles);
    }
}
