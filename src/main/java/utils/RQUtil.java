package utils;

import org.slf4j.MDC;
import reactor.util.context.Context;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class RQUtil {
    public static final String MDC_RQUID="x-request-id";

    public static String getMdcRquid() {
        return MDC.get(MDC_RQUID);
    }

    public static Map<String, String> mdc(String rqUID) {
        Map<String, String> mapMdc = Collections.singletonMap(MDC_RQUID, rqUID);
        MDC.setContextMap(mapMdc);
        return mapMdc;
    }
}
