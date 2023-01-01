package scenario.service.flow.sub;

import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public interface SubFlowFunction<CTX> extends MonoFunction<CTX, CTX>{
    SubFlowFunction<Object> EMPTY = Mono::just;

    /*
        subFlow - is next step.
        Request to remote service.
     */
    default SubFlowFunction<CTX> next (SubFlowFunction<CTX> subFlow){
        if (this == EMPTY) return subFlow;
        return  ctx -> apply(ctx).flatMap(subFlow);
    }

    /*
        Set action (Validate request for example)
    */
    default SubFlowFunction<CTX> nextConsumer (Consumer<CTX> consumer){
        return ctx -> apply(ctx).doOnNext(consumer);
    }

    static <CTX> SubFlowFunction<CTX> empty(){
        return (SubFlowFunction<CTX>) EMPTY;
    }
}
