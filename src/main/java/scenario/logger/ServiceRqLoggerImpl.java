package scenario.logger;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import scenario.context.ExtServiceContext;
import scenario.context.FlowContext;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class ServiceRqLoggerImpl<RQ, CTX extends ExtServiceContext<RQ, ?>> implements Consumer<CTX> {
    protected Function<RQ, String> rqFormat =MaskObjectImpl.getInstanceDefault();
    protected String text = "Request: {} rqUid: {} body {}";
    protected Function<CTX, Object[]> args = ctx -> new Object[]{ctx.getServiceName(),
            ctx.getRqUID(), rqFormat.apply(ctx.getRg())};

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
