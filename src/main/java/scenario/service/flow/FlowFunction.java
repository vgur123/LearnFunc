package scenario.service.flow;

import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface FlowFunction <RQ, RS> extends Function<RQ, Mono<RS>> {
}
