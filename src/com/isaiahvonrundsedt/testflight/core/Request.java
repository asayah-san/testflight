package com.isaiahvonrundsedt.testflight.core;

import java.util.Date;

public class Request implements Comparable<Request> {

    private int value;
    /**
     * @param value object represents the actual value for
     *              the request that the memory simulator
     *              is being made.
     */
    private int status;
    /**
     * @param status determines the status of the request that has been or
     *               will be made. The value will be either STATUS_PAGE_HIT
     *               or STATUS_PAGE_FAULT depending if the specified request
     *               object is already on the memory block simulation.
     */
    private int frameLocation;
    /**
     * @param frameLocation is the current or destination index of the request
     *                      object.
     */
    private long timestamp;
    /**
     * @param timestamp is the value that will be determined if the request object
     *                  inside the memory block is old enough that its location in
     *                  the block can be reallocated by another request object.
     */

    public static final int STATUS_PAGE_HIT = 0;
    public static final int STATUS_PAGE_FAULT = 1;

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(Date date){
        this.timestamp = date.getTime();
    }

    public int getValue() { return value; }

    public void setValue(int value) {
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (status == STATUS_PAGE_HIT || status == STATUS_PAGE_FAULT)
            this.status = status;
        else
            throw new IllegalArgumentException("Request status value must inherit from STATUS_PAGE_FAULT or STATUS_PAGE_HIT");
    }

    public int getFrameLocation() {
        return frameLocation;
    }

    public void setFrameLocation(int frameLocation) {
        this.frameLocation = frameLocation;
    }

    @Override
    public int compareTo(Request otherRequest) {
        return Long.compare(otherRequest.timestamp, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Request)
            return ((Request) obj).getValue() == value;
        else return false;
    }
}
