package dalia.daniele.telegram.taro_web_app.llm;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LlmClient<Request> {
    Mono<String> complete(Request request);
    Flux<String> completeStream(Request request);
}
