package scenario.context;

import lombok.Data;

import java.util.Map;

@Data
public class FlowContext <RQ, RS>{
    private final long start = System.currentTimeMillis();
    private long time;
    private RQ request;
    private RS response;
    private Map<String, String> mapMdc;
}
