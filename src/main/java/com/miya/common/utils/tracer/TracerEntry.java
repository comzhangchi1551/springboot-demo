package com.miya.common.utils.tracer;

import lombok.Data;

/**
 * 跟踪entry
 *
 * @author lmx
 */
@Data
public class TracerEntry {

    private long tracerId;

    private String path;

    private long start;

    private TracerEntry() {
        setTracerId(System.nanoTime());
        setStart(System.currentTimeMillis());
    }

    public static TracerEntry acquire() {
        return new TracerEntry();
    }

    public long release() {
        return System.currentTimeMillis() - start;
    }

    public String getTracerId() {
        return String.valueOf(tracerId);
    }

    public void setTracerId(long tracerId) {
        this.tracerId = tracerId;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

}
