package scenario.context;

import lombok.Data;
import scenario.logger.Logger;

import java.util.Map;

@Data
public class FlowContext<RQ, RS> implements Context<RQ,RS>{
    private final long start = System.currentTimeMillis();
    private Logger logger;
    private long time;
    private RQ request;
    private RS response;
    private Map<String, String> mapMdc;
    private String serviceName;
}
