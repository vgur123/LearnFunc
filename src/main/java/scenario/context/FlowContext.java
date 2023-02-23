package scenario.context;

import lombok.Data;

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
}
