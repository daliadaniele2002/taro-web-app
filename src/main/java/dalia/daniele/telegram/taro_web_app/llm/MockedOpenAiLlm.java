package dalia.daniele.telegram.taro_web_app.llm;

import dalia.daniele.telegram.taro_web_app.domain.AiRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@ConditionalOnProperty(name = "ai.openai.mocked", havingValue = "true")
public class MockedOpenAiLlm implements LlmClient<AiRequest> {

    @Override
    public Mono<String> complete(AiRequest aiRequest) {
        return Mono.delay(Duration.ofSeconds(2))
                .then(Mono.defer(() -> Mono.just(Constants.CONSTANT_RESULT)));
    }

    @Override
    public Flux<String> completeStream(AiRequest aiRequest) {
        String[] split = Constants.CONSTANT_RESULT.split("\n");

        return Flux.fromArray(split)
                .delayElements(Duration.ofMillis(200));
    }

    private static class Constants {
        private static final String CONSTANT_RESULT = """
                ### Il Rituale dell'Amore: Riflessioni sul Tuo Cuore\\n\\nAlessandra, nata il 10 settembre 1998, l’energia che circonda la tua vita sentimentale si rivela attraverso le carte che oggi si sono manifestate: Giudizio, Sei di Pentacoli e Regina di Bastoni. Queste carte, unite, tracciano un percorso profondo e rivelatore.\\n\\n**Giudizio**: Questa carta suggerisce un momento di risveglio e di trasformazione. Ti invita a riflettere su relazioni passate e su come queste esperienze ti abbiano plasmato. È tempo di liberarti di vecchi schemi e di accogliere una nuova visione dell’amore. Il Giudizio ti esorta a perdonare te stessa e gli altri, così da poter aprire il tuo cuore a nuove possibilità.\\n\\n**Sei di Pentacoli**: Questa carta simboleggia l'equilibrio e la generosità. In amore, indica che è fondamentale trovare un reciproco scambio di energia e affetto. La tua capacità di dare e ricevere amore sarà messa alla prova; ricorda che una relazione sana si basa su un equilibrio. Potresti anche scoprire che offrendo il tuo supporto agli altri, riceverai amore e gratitudine inaspettati.\\n\\n**Regina di Bastoni**: Infine, la Regina di Bastoni rappresenta la passione, la creatività e la forza interiore. Essa ti invita a esprimere la tua vera essenza e a non temere di mostrare il tuo lato più ardente. La tua autoconfidenza può attrarre verso di te persone speciali. Non dimenticare di seguire i tuoi desideri e di coltivare le tue passioni, poiché ciò ti aprirà le porte a nuove e belle relazioni.\\n\\n### Sintesi Finale e Consiglio\\n\\nAlessandra, il tuo cammino amoroso è un viaggio di risveglio e di equilibrio. Abbraccia il potere del perdono e della generosità, mentre lasci emergere la Regina di Bastoni che è in te. Ricorda di essere autentica e di seguire il tuo cuore. Abbi fiducia nel tuo potere personale e non esitare a condividere i tuoi doni con il mondo. L’amore che desideri è già in cammino verso di te; apri le braccia e accoglilo.
                """;
    }
}
