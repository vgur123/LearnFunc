package scenario.service.flow.impl;



import lombok.extern.slf4j.Slf4j;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import scenario.MDCUtils;
import scenario.context.FlowContext;
import scenario.service.flow.FlowFunction;
import scenario.service.flow.sub.SubFlowFunction;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public abstract class FlowFunctionImpl  <RQ, RS, CTX extends FlowContext<RQ, RS>> implements FlowFunction <RQ, RS>{
    protected Logger loggerCtx =  LoggerFactory.getLogger(getClass());
    protected Supplier<RS> rsConstructor = new ClassConstructorSupplier<>(GenericUtils.resolveTypeArgument(getClass(), FlowFunctionImpl.class, 1));
    protected Supplier<RS> ctxConstructor = new ClassConstructorSupplier<>(GenericUtils.resolveTypeArgument(getClass(), FlowFunctionImpl.class, 2));
    protected Consumer<CTX> fillContextDefault = this::fillContextDefault;
    protected Function<RQ, CTX> createContext = this::createContext;
    protected Consumer<CTX> rqLogger = FlowRqLoggerImpl.getInstanceDefauly();
    protected Consumer<CTX> rsLogger = FlowRsLoggerImpl.getInstanceDefauly();
    protected SubFlowFunction<CTX> subFlow = SubFlowFunction.empty();
    protected BiConsumer<CTX, Throwable> applyError = this::applyError;
    protected JsonWriter<Object> jsonWriter = new JSONWriter();



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

    private void fillContextDefault(CTX ctx) {
    }

    private void applyError(CTX ctx, Throwable throwable) {
    }

    private class JsonWriter<T> {
    }
}
