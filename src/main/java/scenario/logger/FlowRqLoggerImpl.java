package scenario.logger;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import scenario.context.FlowContext;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
@Slf4j
public class FlowRqLoggerImpl <RQ, CTX extends FlowContext<RQ, ?>> implements Consumer<CTX> {
    protected Function<RQ, String> rqFormat =MaskObjectImpl.getInstanceDefault();
    protected String text = "{} Request: {}";
    protected Function<CTX, Object[]> args = ctx -> new Object[]{ctx.getServiceName(), rqFormat.apply(ctx.getRg())};

    protected static FlowRqLoggerImpl<Object, FlowContext<Object, ?>> instanceDefault =
            new FlowRqLoggerImpl<>();

    public static <RQ, CTX extends FlowContext<RQ, ?>> FlowRqLoggerImpl<RQ, CTX> getInstanceDefault(){
        return (FlowRqLoggerImpl<RQ, CTX>) instanceDefault;
    }

    @Override
    public void accept(CTX ctx) {
        try{
            ctx.getLogger().info(text, args.apply(ctx));
        } catch (Exception e){
            log.warn(e.getMessage(), e);
        }
    }
}
