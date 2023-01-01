package scenario.service.flow.sub;

import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface MonoFunction<RQ, RS> extends Function<RQ, Mono<RS>> {
}
