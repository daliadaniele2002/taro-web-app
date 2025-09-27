package dalia.daniele.telegram.taro_web_app.controller;

import dalia.daniele.telegram.taro_web_app.command.GenerateSpreadCommand;
import dalia.daniele.telegram.taro_web_app.command.InterpretSpreadCommand;
import dalia.daniele.telegram.taro_web_app.command.InterpretSpreadStreamCommand;
import dalia.daniele.telegram.taro_web_app.domain.interpret.dto.InterpretResponse;
import dalia.daniele.telegram.taro_web_app.domain.spread.Spread;
import dalia.daniele.telegram.taro_web_app.domain.spread.dto.SpreadRequest;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/v1/spreads")
public class SpreadController {

    private final BeanFactory beanFactory;

    public SpreadController(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Spread> create(@RequestBody Mono<SpreadRequest> spreadRequest) {
        var command = beanFactory.getBean(GenerateSpreadCommand.class);

        return spreadRequest.flatMap(command::execute);
    }

    @GetMapping("/{spreadId}/interpret")
    @ResponseStatus(HttpStatus.OK)
    public Mono<InterpretResponse> interpret(@PathVariable UUID spreadId) {
        var command = beanFactory.getBean(InterpretSpreadCommand.class);

        return command.execute(spreadId);
    }

//    @GetMapping(path = "/{spreadId}/interpret/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<String> interpretStream(@PathVariable UUID spreadId) {
        var command = beanFactory.getBean(InterpretSpreadStreamCommand.class);

        return command.execute(spreadId);
    }
}
