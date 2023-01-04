package scenario.service.flow.impl;



import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import scenario.MDCUtils;
import scenario.context.FlowContext;
import scenario.service.flow.FlowFunction;
import scenario.service.flow.sub.SubFlowFunction;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public abstract class FlowFunctionImpl  <RQ, RS, CTX extends FlowContext<RQ, RS>> implements FlowFunction <RQ, RS>{
    protected SubFlowFunction<CTX> subFlow = SubFlowFunction.empty();
    protected Function<RQ, CTX> createContext = this::createContext;
    protected Consumer<CTX> rqLogger = this::rqLogger;
    protected Consumer<CTX> rsLogger = this::rsLogger;

    protected void rqLogger(CTX ctx) {
        log.info("Request is {}", ctx.getRequest() );
    }

    protected void rsLogger(CTX ctx) {
        log.info("Response is {}", ctx.getResponse() );
    }

    @Override
    public Mono<RS> apply(RQ rq){
        CTX ctx = start(rq);
        return subFlow
                .apply(ctx)
                .doOnCancel(()->onCancel(ctx))
                .onErrorResume(throwable -> onErrorResume(ctx, throwable))
                .map(this::onFinish)
                .contextWrite(context -> MDCUtils.contextWriteMdc(context, ctx.getMapMdc()));
    }


    private CTX start(RQ rq) {
        CTX ctx = createContext.apply(rq);
        rqLogger.accept(ctx);
        return ctx;
    }

    private CTX createContext(RQ rq) {
        return null;
    }

    private void onCancel(CTX ctx) {
    }

    private Mono<? extends CTX> onErrorResume(CTX ctx, Throwable throwable) {
        return null;
    }

    protected RS onFinish(CTX ctx) {
        ctx.setTime(System.currentTimeMillis()-ctx.getStart());
        rsLogger.accept(ctx);
        return ctx.getResponse();
    }
}
