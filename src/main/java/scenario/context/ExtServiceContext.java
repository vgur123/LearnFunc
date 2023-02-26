package scenario.context;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class ExtServiceContext<RQ, RS> implements Context<RQ, RS> {
    private final long startTime = System.currentTimeMillis();
    private Logger logger;
    private RQ rq;
    private String rqUID;
    private Map<String, String> mapMdc;
    private String serviceName;
    private RS rs;
    private long time;
    private Throwable throwable;
}
