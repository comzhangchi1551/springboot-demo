package com.miya.common.utils.tracer;

import lombok.Data;

/**
 * 跟踪entry:
 * 每个请求进来之后，都会创建一个此对象的ThreadLocal进行追踪，主要是追踪开始时间和结束时间，从而计算接口处理时间；
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
