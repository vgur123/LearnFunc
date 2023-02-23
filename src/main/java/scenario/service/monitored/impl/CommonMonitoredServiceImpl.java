package scenario.service.monitored.impl;

import org.slf4j.Logger;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;
import scenario.exceptions.RequestCanceledExceptin;
import scenario.logger.ExtServiceContextMetricImpl;
import scenario.logger.ServiceRqLoggerImpl;
import scenario.logger.ServiceRsLoggerImpl;
import scenario.service.flow.sub.MonoFunction;
import scenario.service.monitored.MonitoredService;
import utils.ServiceNameFunction;
import utils.SupplierConstant;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CommonMonitoredServiceImpl<RQ, RS> implements MonitoredService<RQ, RS> {
    protected Consumer<RS> validate = this::validate;
    protected Function <RQ, String> rqUID = this::rqUID;
    protected Supplier<String> serviceName = new SupplierConstant<>(
            ServiceNameFunction.getInstanceDefault().apply(getClass()));

    protected Consumer<ExtServiceContext<RQ, RS>> rqLogger = ServiceRqLoggerImpl.getInstanceDefaul();
    protected Consumer<ExtServiceContext<RQ, RS>> rsLogger = ServiceRsLoggerImpl.getInstanceDefaul();
    protected Consumer<ExtServiceContext<RQ, RS>> metric = ExtServiceContextMetricImpl.getInstanceDefaul();
    protected Function <RQ, ExtServiceContext<RQ, RS>> createCtx = this::createCtx;
    protected MonoFunction<RQ, RS> service;
    protected Logger loggerCtx = org.slf4j.LoggerFactory.getLogger((getClass()));

    private ExtServiceContext<RQ,RS> createCtx(RQ rq) {
        ExtServiceContext<RQ,RS> ctx = new ExtServiceContext<>();
        ctx.setLogger(loggerCtx);
        ctx.setRq(rq);
        ctx.setRqUID(rqUID.apply(rq));
        ctx.setMapMdc(MDC.getCopyOfContextMap());
        ctx.setServiceName(serviceName.get());
        return ctx;
    }

    @Override
    public Mono<RS> apply(RQ rq) {
        ExtServiceContext<RQ,RS> ctx = createCtx.apply(rq);
        rqLogger.accept(ctx);

        return service.apply(rq)
                .doOnCancel(()-> {
                    if (ctx.getRs()!=null || ctx.getThrowable()!=null) return;
                    MDC.setContextMap(ctx.getMapMdc());
                    ctx.setTime(System.currentTimeMillis() - ctx.getStartTime());
                    ctx.setThrowable(RequestCanceledExceptin.instance);
                    rsLogger.accept(ctx);
                    metric.accept(ctx);
                        })
                .doOnNext(rs -> {
                    ctx.setTime(System.currentTimeMillis() - ctx.getStartTime());
                    ctx.setRs(rs);
                    validate.accept(rs);
                    rsLogger.accept(ctx);
                    metric.accept(ctx);
                })
                .doOnError(throwable -> {
                    ctx.setTime(System.currentTimeMillis() - ctx.getStartTime());
                    ctx.setThrowable(throwable);
                    rsLogger.accept(ctx);
                    metric.accept(ctx);
                });
    }

    protected void validate(RS rs){}

    protected String rqUID(RQ rq){
        return  null;
    }
}
