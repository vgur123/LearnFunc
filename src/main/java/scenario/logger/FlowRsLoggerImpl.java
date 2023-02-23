package scenario.logger;

import lombok.Getter;
import lombok.Setter;

public class FlowRsLoggerImpl {
    @Getter
    @Setter
    protected static FlowRsLoggerImpl instanceDefault = new FlowRsLoggerImpl();

}
