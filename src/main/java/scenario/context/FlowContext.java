package scenario.context;

import lombok.Data;
import org.slf4j.Logger;

import java.util.Map;

@Data
public class FlowContext <RQ, RS>{
    private final long start = System.currentTimeMillis();
    private long startTime;
    private long time;
    private RQ rq;
    private RS rs;
    private Map<String, String> mapMdc;
    String ServiceName;
    Logger logger;
}
