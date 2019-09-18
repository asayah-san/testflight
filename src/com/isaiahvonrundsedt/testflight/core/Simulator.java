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
    private int rCounter = 0;
    private int hitCount = 0;
    private int faultCount = 0;

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

    private void notifyStatusChanged(int status){
        char x = status == Request.STATUS_PAGE_FAULT ? '*' : '-';

        statusTable.setValueAt(x, 0, rCounter);
        statusTable.updateUI();
    }

    public void setOutputTable(JTable table){
        this.mainTable = table;
    }

    public void setStatusTable(JTable table){
        this.statusTable = table;
    }

    public void newRequest(int value){
        Request request = new Request();
        request.setValue(value);

        if (hasFreeIndex()){
            if (contains(value)){
                int index = getRequestIndex(value);

                hitCount++;
                request.setStatus(Request.STATUS_PAGE_HIT);
                if (method == METHOD_TYPE_LRU)
                    request.setRequestTime(tracker);

                frames[index] = request;
                notifyDataSetChanged();
                notifyStatusChanged(request.getStatus());

                if (method == METHOD_TYPE_LRU)
                    tracker++;

                rCounter++;
            } else {
                int index = getFreeIndex();

                faultCount++;
                request.setStatus(Request.STATUS_PAGE_FAULT);
                request.setRequestTime(tracker);

                frames[index] = request;
                notifyDataSetChanged();
                notifyStatusChanged(request.getStatus());

                tracker++;
                rCounter++;
            }
        } else {
            if (contains(value)){
                int index = getRequestIndex(value);
                request = getRequest(value);

                request.setStatus(Request.STATUS_PAGE_HIT);
                hitCount++;

                if (method == METHOD_TYPE_LRU)
                    request.setRequestTime(tracker);

                frames[index] = request;
                notifyDataSetChanged();
                notifyStatusChanged(request.getStatus());

                if (method == METHOD_TYPE_LRU)
                    tracker++;

                rCounter++;
            } else {
                int index = getIndexForReallocation();

                faultCount++;
                request.setStatus(Request.STATUS_PAGE_FAULT);
                request.setRequestTime(tracker);
                request.setValue(value);

                frames[index] = request;
                notifyDataSetChanged();
                notifyStatusChanged(request.getStatus());

                tracker++;
                rCounter++;
            }
        }
    }

    private boolean hasFreeIndex(){
        if (frames.length > 0){
            for (Request request: frames)
                if (request == null) return true;
            return false;
        }
        return false;
    }

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

    private int getIndexForReallocation(){
        if (frames.length > 0){
            int index = getFreeIndex();

            if (index == -1){
                Request request = frames[0];

                for (Request currentRequest: frames){
                    if (request.getRequestTime() > currentRequest.getRequestTime())
                        request = currentRequest;
                }
                return getRequestIndex(request.getValue());
            } else
                return index;
        }
        return -1;
    }

    private Request getRequest(int value){
        Request request = null;

        if (frames.length > 0){
            for (Request r : frames)
                if (r.getValue() == value) request = r;
        }
        return request;
    }

}
