package scenario.logger;

import lombok.Getter;
import lombok.Setter;

public class ExtServiceContextMetricImpl<RQ,RS> {
    @Getter
    @Setter
    static ExtServiceContextMetricImpl instanceDefault = new ExtServiceContextMetricImpl();

}
