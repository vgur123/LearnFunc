package scenario.logger;

import lombok.Getter;
import lombok.Setter;

public class FlowRqLoggerImpl {
    @Getter
    @Setter
    protected static FlowRqLoggerImpl instanceDefault = new FlowRqLoggerImpl();

}
