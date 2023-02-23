package scenario.logger;

import java.util.function.Consumer;

public class ServiceRqLoggerImpl<RQ,RS> {
    public static Consumer<ExterviceContext<RQ,RS>> getInstanceDefaul() {
        return  null;
    }
}
