package scenario.logger;

import lombok.Getter;
import lombok.Setter;

public class ServiceRqLoggerImpl<RQ,RS> {
    @Getter
    @Setter
    protected static ServiceRqLoggerImpl instanceDefault = new ServiceRqLoggerImpl();

}
