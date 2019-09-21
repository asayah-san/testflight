package com.isaiahvonrundsedt.testflight.core;

import com.isaiahvonrundsedt.testflight.core.Request;

import javax.swing.*;

public class Simulator {

    private int method;

    public static final int METHOD_TYPE_LRU = 0;
    public static final int METHOD_TYPE_FIFO = 1;

    private JTable mainTable;
    private JTable statusTable;

    private Request[] frames;

    private int tracker = 0;
    /**
     *  This serves as the 'timestamps' for the request objects,
     *  the lower the value, the older that is requested by the
     *  algorithm.
     */

    private int rCounter = 0;
    /**
     *  This variable defines how many requests have been made
     */

    private int hitCount = 0;
    private int faultCount = 0;
    // This variables holds how many hits and faults that has occurred
    // both values will be passed to the next JFrame object.

    /**
     *
     * @param method defines which Page Reallocation Scheme will be used,
     *               this will be either be LRU (Least Recently Used) or
     *               FIFO (First-In, First-Out)
     * @param frameSize defines how many page frames (capacity of the internal
     *                  request array)
     */
    public Simulator (int method, int frameSize){
        this.method = method;

        frames = new Request[frameSize];
    }

    public int getHitCount() {
        return hitCount;
    }

    public int getFaultCount() {
        return faultCount;
    }

    /**
     *  This method should be called to update the GUI or
     *  print the results.
     */
    private void notifyDataSetChanged(){
        for (int i = 0; i < frames.length; i++) {
            Request request = frames[i];
            if (request != null) {
                if (statusTable != null && mainTable != null){

                    mainTable.setValueAt(request.getValue(), i, rCounter);

                    mainTable.updateUI();
                } else
                    System.out.println("FRAME " + i + ": " + request.getValue());
            }
        }
    }

    /**
     *
     * @param status defines what is the status of the request
     *               this must be a value from the Request class
     */
    private void notifyStatusChanged(int status){
        char x = status == Request.STATUS_PAGE_FAULT ? '*' : '-';

        statusTable.setValueAt(x, 0, rCounter);
        statusTable.updateUI();
    }

    /**
     *
     * @param table sets what JTable will be used for the output
     *              of the algorithm.
     */
    public void setOutputTable(JTable table){
        this.mainTable = table;
    }

    /**
     *
     * @param table sets what JTable will be used for the output of
     *              the status of the requests in the algorithm, which
     *              is the values inherited from the Request class.
     */
    public void setStatusTable(JTable table){
        this.statusTable = table;
    }


    public void newRequest(int value){
        // Instantiates a new Request object and also defines the value
        Request request = new Request();
        request.setValue(value);

        // Check if the current frames has indexes that are currently
        // unallocated
        if (hasFreeIndex()){
            // Check if the current frames has the value already
            if (contains(value)){

                // Get the current index of the value that is already
                // inside the frames.
                int index = getRequestIndex(value);

                // Increment the hit count
                hitCount++;

                // Set the status to PAGE_HIT and check if the current
                // method if TYPE_LRU, if yes, then update the request
                // time of the Request object
                request.setStatus(Request.STATUS_PAGE_HIT);
                if (method == METHOD_TYPE_LRU)
                    request.setRequestTime(tracker);

                // Using the index of the value existing in the frames,
                // replace the instance of the Request object with the
                // new attributes.
                frames[index] = request;

                // Notify the JTables about the new changes in the data
                // set/
                notifyDataSetChanged();
                notifyStatusChanged(request.getStatus());

                // If the method is LRU, then update the tracker
                if (method == METHOD_TYPE_LRU)
                    tracker++;

                // Increment to know how many requests have been made.
                rCounter++;
            } else {
                // The request value doesn't exist from the current frames
                // so, get what indexes are unallocated.
                int index = getFreeIndex();

                // Increment the PAGE_FAULT counter
                faultCount++;

                // Set the appropriate attributes for the Request object
                request.setStatus(Request.STATUS_PAGE_FAULT);
                request.setRequestTime(tracker);

                // Insert the Request Object to the frames with the index
                // fetched by the method above.
                frames[index] = request;

                // Notify the JTables about the changes in the data set.
                notifyDataSetChanged();
                notifyStatusChanged(request.getStatus());

                // Increment the tracker and the request counter
                tracker++;
                rCounter++;
            }
        } else {
            // The current frames has no free indexes

            // Check if the frames has the value already existing
            if (contains(value)){

                // Get the index of the same value in the frames
                int index = getRequestIndex(value);
                // Fetch the request object in the frames to update
                // its attributes later.
                request = getRequest(value);

                // Update the status of the request object and increment
                // the hit counter
                request.setStatus(Request.STATUS_PAGE_HIT);
                hitCount++;

                // Check the method if LRU, then update the request time, in
                // the object
                if (method == METHOD_TYPE_LRU)
                    request.setRequestTime(tracker);

                // Using the fetched index value, update the request object
                // in the frames.
                frames[index] = request;

                // Notify the JTable about the changes in the data set (frames).
                notifyDataSetChanged();
                notifyStatusChanged(request.getStatus());

                if (method == METHOD_TYPE_LRU)
                    tracker++;

                rCounter++;
            } else {

                // Check the frames for indexes that can be reallocated
                int index = getIndexForReallocation();

                // Increment fault counter
                faultCount++;

                // Set the attributes of request object
                request.setStatus(Request.STATUS_PAGE_FAULT);
                request.setRequestTime(tracker);
                request.setValue(value);

                // Replace the request object inside the frames
                frames[index] = request;

                // Notify the JTables about the changes in the data set
                notifyDataSetChanged();
                notifyStatusChanged(request.getStatus());

                tracker++;
                rCounter++;
            }
        }
    }

    /**
     *
     * @return true if there are null indexes or free indexes
     * available in the current frames
     */
    private boolean hasFreeIndex(){
        if (frames.length > 0){
            for (Request request: frames)
                if (request == null) return true;
            return false;
        }
        return false;
    }

    /**
     *
     * @param value defines the actual value for the request
     * @return true if the value in the parameter is already
     * is current frames
     */
    private boolean contains(int value){
        if (frames.length > 0){
            for (Request request: frames){
                if (request != null && request.getValue() == value)
                    return true;
            }
            return false;
        }
        return false;
    }

    /**
     *
     * @param value defines the actual value for the request
     * @return the index of the value in current frames.
     */
    private int getRequestIndex(int value){
        if (frames.length > 0 && contains(value)){
            for (int i = 0; i < frames.length; i++){
                if (frames[i].getValue() == value)
                    return i;
            }
            return -1;
        }
        return -1;
    }

    /**
     *
     * @return index that is currently unallocated
     */
    private int getFreeIndex(){
        if (frames.length > 0){
            for (int i = 0; i < frames.length; i++){
                if (frames[i] == null)
                    return i;
            }
            return -1;
        }
        return -1;
    }

    /**
     *
     * @return index that can be reallocated
     */
    private int getIndexForReallocation(){
        if (frames.length > 0){
            // Check if the frames has still free indexes
            int index = getFreeIndex();

            // If the return value is -1 that means there are
            // no more free indexes
            if (index == -1){
                // Get the first request object in the frame.
                Request request = frames[0];

                // Iterates the current frames for possible
                // indexes that can be reallocated
                for (Request currentRequest: frames){
                    // If the current request object is older than the other request object
                    if (request.getRequestTime() > currentRequest.getRequestTime())
                        request = currentRequest;
                }

                // Fetches and returns the index of the request object that will be reallocated
                return getRequestIndex(request.getValue());
            } else

                // Simply return the index that is unallocated
                return index;
        }
        return -1;
    }

    /**
     *
     * @param value the actual value of the request object
     * @return the request object itself from the frames.
     */
    private Request getRequest(int value){
        Request request = null;

        if (frames.length > 0){
            for (Request r : frames)
                if (r.getValue() == value) request = r;
        }
        return request;
    }

}
