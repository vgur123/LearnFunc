package scenario.logger;

import lombok.Getter;

public class ServiceRsLoggerImpl<RQ,RS> {
    @Getter
    public static ServiceRsLoggerImpl instanceDefault = new ServiceRsLoggerImpl();
}
