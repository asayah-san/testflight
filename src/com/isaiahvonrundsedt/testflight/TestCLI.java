package com.isaiahvonrundsedt.testflight;

import com.isaiahvonrundsedt.testflight.core.Request;

import javax.swing.*;
import java.util.*;

/**
 *   TestCLI class is used to test the algorithm of the
 *   simulator. Do not call or instantiate this class
 *   on any other classes
 */

class TestCLI {

    private static final int METHOD_TYPE_LRU = 1;
    private static final int METHOD_TYPE_FIFO = 2;

    public static void main(String[] args) {
        int tracker = 0;

        System.out.println("TestFlight");
        System.out.println("======================================");
        System.out.println("Instruction: Input only NUMBERS with no");
        System.out.println("decimals whatsoever.\n");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Memory Management Method");
        System.out.println("[1] LRU - Least Recently Used");
        System.out.println("[2] FIFO - First-In, First-Out");
        int method = 0;
        do {
            System.out.print("Enter method: ");
            method = scanner.nextInt();
        } while (!(method == 1 || method == 2));

        System.out.print("Enter Page Frame: ");
        int frameSize = scanner.nextInt();

        System.out.print("Enter Request Amount: ");
        int requestAmount = scanner.nextInt();

        HashMap<Integer, Request[]> frameSet = new HashMap<>();
        Request[] frames = new Request[frameSize];
        for (int iterator = 0; iterator < requestAmount; iterator++){
            System.out.print("Enter request value: ");
            int requestValue = scanner.nextInt();

            Request r = new Request();
            r.setValue(requestValue);

            if (hasFreeIndex(frames)){
                if (contains(requestValue, frames)) {
                    int index = getRequestIndex(requestValue, frames);

                    r.setStatus(Request.STATUS_PAGE_HIT);
                    if (method == METHOD_TYPE_LRU)
                        r.setRequestTime(tracker);

                    frames[index] = r;
                    for (Request request: frames){
                        if (request != null)
                            System.out.println(request.getValue());
                    }
                    frameSet.put(tracker, frames);
                    if (method == METHOD_TYPE_LRU)
                        tracker++;

                } else {
                    int index = getFreeIndex(frames);

                    r.setStatus(Request.STATUS_PAGE_FAULT);
                    r.setRequestTime(tracker);

                    frames[index] = r;
                    for (Request request: frames){
                        if (request != null)
                            System.out.println(request.getValue());
                    }
                    frameSet.put(tracker, frames);
                    tracker++;
                }
            } else {
                if (contains(requestValue, frames)){
                    int index = getRequestIndex(requestValue, frames);

                    r = getRequest(requestValue, frames);
                    r.setStatus(Request.STATUS_PAGE_HIT);

                    if (method == METHOD_TYPE_LRU)
                        r.setRequestTime(tracker);

                    frames[index] = r;

                    for (Request request: frames){
                        if (request != null)
                            System.out.println(request.getValue());
                    }
                    frameSet.put(tracker, frames);

                    if (method == METHOD_TYPE_LRU)
                        tracker++;

                } else {
                    int index = getIndexForReallocation(frames);

                    r.setStatus(Request.STATUS_PAGE_FAULT);
                    r.setRequestTime(tracker);
                    r.setValue(requestValue);

                    frames[index] = r;
                    for (Request request: frames){
                        if (request != null)
                            System.out.println(request.getValue());
                    }
                    frameSet.put(tracker, frames);
                    tracker++;
                }
            }
        }




    }

    private static boolean hasFreeIndex(Request[] set){
        if (set.length > 0){
            for (Request request : set)
                if (request == null) return true;
            return false;
        }
        return false;
    }

    private static boolean contains(int r, Request[] set){
        if (set.length > 0){
            for (Request request : set) {
                if (request != null && request.getValue() == r)
                    return true;
            }
            return false;
        }
        return false;
    }
    private static int getRequestIndex(int v, Request[] set){
        if (set.length > 0 && contains(v, set)){
            for (int i = 0; i < set.length; i++){
                if (set[i].getValue() == v)
                    return i;
            }
            return -1;
        }
        return -1;
    }
    private static int getFreeIndex(Request[] set){
        if (set.length > 0){
            for (int i = 0; i < set.length; i++){
                if (set[i] == null)
                    return i;
            }
            return -1;
        }
        return -1;
    }
    private static int getIndexForReallocation(Request[] frames){
        if (frames.length > 0){
            int index = getFreeIndex(frames);
            if (index == -1){
                Request request = frames[0];
                for (Request currentRequest: frames){
                    if (request.getRequestTime() > currentRequest.getRequestTime())
                        request = currentRequest;
                }
                return getRequestIndex(request.getValue(), frames);
            } else
                return index;
        }
        return -1;
    }
    private static Request getRequest(int r, Request[] set){
        Request request = null;

        if (set.length > 0){
            for (Request _r: set)
                if (_r.getValue() == r) request = _r;
        }
        return request;
    }

}
