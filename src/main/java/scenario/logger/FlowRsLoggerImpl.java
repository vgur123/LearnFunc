package scenario.logger;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import scenario.context.FlowContext;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class FlowRsLoggerImpl<RS, CTX extends FlowContext<?, RS>> implements Consumer<CTX> {
    protected Function<RS, String> rsFormat =MaskObjectImpl.getInstanceDefault();
    protected String text = "{} Response: time: {} response {}";
    protected Function<CTX, Object[]> ctxToArgsArray = ctx -> new Object[]{ctx.getServiceName(), ctx.getTime(), rsFormat.apply(ctx.getRs())};

    protected static FlowRsLoggerImpl<Object, FlowContext<Object, Object>> instanceDefault =
            new FlowRsLoggerImpl<>();

    public static <RS, CTX extends FlowContext<?, RS>> FlowRsLoggerImpl<RS, CTX> getInstanceDefault(){
        return (FlowRsLoggerImpl<RS, CTX>) instanceDefault;
    }

    @Override
    public void accept(CTX ctx) {
            ctx.getLogger().info(text, ctxToArgsArray.apply(ctx));
    }
}
