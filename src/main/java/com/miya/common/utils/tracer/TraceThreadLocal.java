package com.miya.common.utils.tracer;

public class TraceThreadLocal {

    private static final ThreadLocal<TracerEntry> TRACER = new ThreadLocal<>();

    public static void set(TracerEntry trace) {
        TRACER.set(trace);
    }

    public static TracerEntry get() {
        return TRACER.get();
    }

    public static void remove() {
        TRACER.remove();
    }

    private TraceThreadLocal() {

    }
}
