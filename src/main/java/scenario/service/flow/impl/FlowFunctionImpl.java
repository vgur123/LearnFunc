package scenario.service.flow.impl;



import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;
import utils.MDCUtil;
import scenario.context.FlowContext;
import scenario.exceptions.RequestCanceledExceptin;
import scenario.logger.FlowRqLoggerImpl;
import scenario.logger.FlowRsLoggerImpl;
import scenario.service.flow.FlowFunction;
import scenario.service.flow.sub.SubFlowFunction;
import utils.GenericUtils;
import utils.ServiceNameFunction;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public abstract class FlowFunctionImpl  <RQ, RS, CTX extends FlowContext<RQ, RS>> implements FlowFunction <RQ, RS>{
    protected Logger loggerCtx = org.slf4j.LoggerFactory.getLogger((getClass()));
    protected Supplier<RS> rsConstructor = new ClassConstructorSupplier<>(GenericUtils.resolveTypeArgument(getClass(), FlowFunctionImpl.class, 1));
    protected Supplier<CTX> ctxConstructor = new ClassConstructorSupplier<>(GenericUtils.resolveTypeArgument(getClass(), FlowFunctionImpl.class, 2));
    protected Consumer<CTX> fillContextDefault = this::fillContextDefault;
    protected Function<RQ, CTX> createContext = this::createContext;
    protected Function<RQ, Map<String, String>> mdcContext = this::mdcContext;

    protected Consumer<CTX> rqLogger = FlowRqLoggerImpl.getInstanceDefauly();
    protected Consumer<CTX> rsLogger = FlowRsLoggerImpl.getInstanceDefauly();
    protected SubFlowFunction<CTX> subFlow = SubFlowFunction.empty();
    protected BiConsumer<CTX, Throwable> applyError = this::applyError;
    protected JsonWriter<Object> jsonWriter = new JsonWriter<>();
    protected Consumer<CTX> metric = FlowContextMetricImpl.getInstanceDefaul();
    protected String serviceName = ServiceNameFunction.getInstanceDefault().apply(getClass());

    public static final String METRIC_TIMER = "flow.income";

    @Override
    public Mono<RS> apply(RQ rq){
        CTX ctx = start(rq);
        return subFlow
                .apply(ctx)
                .doOnCancel(()->onCancel(ctx))
                .onErrorResume(throwable -> onErrorResume(ctx, throwable))
                .map(this::onFinish)
                .contextWrite(context -> MDCUtil.contextWriteMdc(context, ctx.getMapMdc()));
    }

    private CTX start(RQ rq) {
        CTX ctx = createContext.apply(rq);
        rqLogger.accept(ctx);
        return ctx;
    }

    private CTX createContext(RQ rq) {
        Map<String, String> mdc = mdcContext.apply(rq);
        MDCUtil.setContextMap(mdc);
        CTX ctx = ctxConstructor.get();
        ctx.setRq(rq);
        ctx.setRs(rsConstructor.get());
        ctx.setMapMdc(MDC.getCopyOfContextMap());
        ctx.setServiceName(serviceName.get());
        ctx.setLogger(loggerCtx);
        fillContextDefault.accept(ctx);
        return ctx;
    }

    protected Map<String, String> mdcContext(RQ rq) {
        return null;
    }

    protected void fillContextDefault(CTX ctx) {
    }

    protected void onCancel(CTX ctx) {
        MDC.setContextMap(ctx.getMapMdc());
        ctx.setTime(System.currentTimeMillis()-ctx.getStartTime());
        applyError.accept(ctx, RequestCanceledExceptin.instance);
        rsLogger.accept(ctx);
        metric.accept(ctx);
    }

    protected void validate(CTX ctx) {
    }

    protected void metric(CTX ctx) {
    }

    protected RS onFinish(CTX ctx) {
        ctx.setTime(System.currentTimeMillis()-ctx.getStartTime());
        rsLogger.accept(ctx);
        metric.accept(ctx);
        return ctx.getRs();
    }

    private void applyError(CTX ctx, Throwable throwable) {
    }


    protected Mono<CTX> onErrorResume (CTX ctx, Throwable throwable){
        applyError.accept(ctx, throwable);
        return Mono.just(ctx);
    }

    protected void subFlowNext(SubFlowFunction<CTX> ctx){
        subFlow = subFlow.next(ctx);
    }

    protected void subFlowNext(Consumer<CTX> ctx){
        subFlow = subFlow.nextConsumer(ctx);
    }

    protected void rqLogger(CTX ctx){
        log.info("Request {}", jsonWriter.writeSave(ctx.getRq()));
    }

    protected void rsLogger(CTX ctx){
        log.info("Response time {} body {} ", ctx.getTime(), jsonWriter.writeSave(ctx.getRs()));
    }
}
