package com.isaiahvonrundsedt.testflight;

import com.isaiahvonrundsedt.testflight.core.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static final int METHOD_TYPE_LRU = 1;
    private static final int METHOD_TYPE_FIFO = 2;

    public static void main(String[] args) {
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

        Request[] requests =  new Request[requestAmount];
        int requestIndexer = 0;

        do {
            System.out.print("Enter request value: ");
            int requestValue = scanner.nextInt();

            Request request = new Request();
            request.setStatus(Request.STATUS_PAGE_FAULT);
            request.setTimestamp(new Date().getTime());
            request.setFrameLocation(requestIndexer);
            request.setValue(requestValue);

            requests[requestIndexer] = request;
            requestIndexer++;
        } while (requestIndexer < requestAmount);

        ArrayList<Request[]> frames = new ArrayList<>();
        for (int i = 0; i < requestAmount; i++){
            Request[] currentFrame = new Request[i + 1];
            for (int j = 0; j < (i + 1); j++){
                Request request = requests[j];
                currentFrame[j] = request;
            }
            frames.add(currentFrame);
        }

        for (Request[] currentFrame: frames){
            System.out.println("REQUEST");
            for (Request request: currentFrame){
                System.out.println("Frame " + request.getFrameLocation() + ": " + request.getValue());
            }
            System.out.println();
        }
    }

    private static boolean exists(int value, Request[] requests){
        if (requests.length > 0){
            for (Request request: requests){
                if (request != null && request.getValue() == value)
                    return true;
            }
            return false;
        }
        return false;
    }

    private static int getUsableIndex(Request[] requests){
        if (requests.length > 0){
            int index = getFreeIndex(requests);
            if (index == -1)
                index = getReallocationIndex(requests);
            return index;
        }
        return 0;
    }

    private static int getFreeIndex(Request[] requests){
        if (requests.length > 0){
            for (int i = 0; i < requests.length; i++){
                if (requests[i] == null)
                    return i;
            }
        }
        return -1;
    }

    private static int getReallocationIndex(Request[] requests){
        int index = 0;
        for (int i = 1; i < requests.length; i++){
            Request currentRequest = requests[i];
            if (currentRequest.getTimestamp() < requests[index].getTimestamp())
                index = i;
        }
        return index;
    }

    private static int getReallocationFrame(Request[] requests){
        Request request = requests[getReallocationIndex(requests)];
        return request.getFrameLocation();
    }
}
