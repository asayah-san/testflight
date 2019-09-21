package com.isaiahvonrundsedt.testflight;

import com.isaiahvonrundsedt.testflight.forms.Configurator;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            // Try to make the Application Interface native as possible
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("TestFlight Configurator");
        frame.setContentPane(new Configurator(frame).getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
