package scenario.logger;

import lombok.Data;
import lombok.Getter;
import scenario.context.ExtServiceContext;
import scenario.context.FlowContext;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class ServiceRsLoggerImpl<RS, CTX extends ExtServiceContext<?, RS>> implements Consumer<CTX> {
    protected Function<RS, String> rsFormat =MaskObjectImpl.getInstanceDefault();
    protected String text = "Response: rqUid {} time: {} response {} error {}";
    protected Function<CTX, Object[]> ctxToArgsArray = ctx -> new Object[]{ctx.getServiceName(),
            ctx.getRqUID(), ctx.getTime(), rsFormat.apply(ctx.getRs())};
    protected Function<CTX, Object[]> ctxToArgsArrayError = ctx -> new Object[]{ctx.getServiceName(),
            ctx.getRqUID(), ctx.getTime(), rsFormat.apply(ctx.getRs())};
    protected static FlowRsLoggerImpl<Object, FlowContext<Object, Object>> instanceDefault =
            new FlowRsLoggerImpl<>();

    public static <RS, CTX extends FlowContext<?, RS>> FlowRsLoggerImpl<RS, CTX> getInstanceDefault(){
        return (FlowRsLoggerImpl<RS, CTX>) instanceDefault;
    }

    @Override
    public void accept(CTX ctx) {
        if (ctx.getThrowable() == null) {
            ctx.getLogger().info(text, ctxToArgsArray.apply(ctx));
        } else {
            ctx.getLogger().warn(text, ctxToArgsArrayError.apply(ctx));
        }
    }

}
