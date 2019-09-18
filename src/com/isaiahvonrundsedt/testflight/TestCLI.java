package com.isaiahvonrundsedt.testflight;

import com.isaiahvonrundsedt.testflight.core.Simulator;

import java.util.*;

/**
 *   TestCLI class is used to test the algorithm of the
 *   simulator. Do not call or instantiate this class
 *   on any other classes
 */

class TestCLI {

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

        Simulator simulator = new Simulator(method, frameSize);
        for (int iterator = 0; iterator < requestAmount; iterator++){
            System.out.print("Enter request value: ");
            int requestValue = scanner.nextInt();

            simulator.newRequest(requestValue);
        }
    }

}
