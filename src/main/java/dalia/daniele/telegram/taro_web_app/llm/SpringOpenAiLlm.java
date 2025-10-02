package dalia.daniele.telegram.taro_web_app.llm;

import dalia.daniele.telegram.taro_web_app.domain.AiRequest;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(name = "ai.openai.mocked", havingValue = "false")
public class SpringOpenAiLlm implements LlmClient<AiRequest> {

    private final OpenAiChatModel chatModel;

    public SpringOpenAiLlm(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public Mono<String> complete(AiRequest aiRequest) {
        var prompt = new Prompt(new SystemMessage(aiRequest.system()), new UserMessage(aiRequest.user()));

        return this.chatModel.stream(prompt)
                .mapNotNull(r -> r.getResult().getOutput().getText())
                .collectList()
                .map(parts -> String.join("", parts));
    }

    @Override
    public Flux<String> completeStream(AiRequest aiRequest) {
        var prompt = new Prompt(new SystemMessage(aiRequest.system()), new UserMessage(aiRequest.user()));

        return this.chatModel.stream(prompt)
                .mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getText());
    }
}
