package com.isaiahvonrundsedt.testflight;

import com.isaiahvonrundsedt.testflight.ui.Configurator;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("TestFlight Configurator");
        frame.setContentPane(new Configurator(frame).getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
