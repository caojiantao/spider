package cn.caojiantao.spider.util;

import java.util.UUID;

/**
 * @author caojiantao
 */
public class LogContext {

    private static ThreadLocal<String> traceIdLocal = new ThreadLocal<>();

    public static void setTraceId() {
        String traceId = UUID.randomUUID().toString().replaceAll("-", "");
        setTraceId(traceId);
    }

    public static void setTraceId(String traceId) {
        traceIdLocal.set(traceId);
    }

    public static String getTraceId() {
        return traceIdLocal.get();
    }

    public static void clear() {
        traceIdLocal.remove();
    }
}
