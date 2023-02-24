package utils;

import org.slf4j.MDC;
import reactor.util.context.Context;

import java.util.Map;

public class MDCUtil {
    public static final String CONTEXT_WRITE_MDC="MDC";
    public static Context contextWriteMdc(Context context, Map<String, String> mdc) {
        if (mdc==null) return context;
        return context.put( CONTEXT_WRITE_MDC, mdc);
    }

    public static void setContextMap(Map<String, String> mdc) {
        if (mdc!=null){
            MDC.setContextMap(mdc);
        } else {
            MDC.clear();
        }
    }
}
