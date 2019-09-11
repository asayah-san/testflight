package com.isaiahvonrundsedt.testflight.ui;

import com.isaiahvonrundsedt.testflight.core.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener {
    private JPanel root;
    private JTextField requestField;
    private JButton requestButton;
    private JTable requestsTable;
    private JLabel label;

    private int method;
    private int frameSize;
    private int requestsAmount;

    private int tracker = 0;
    private int requestDone = 0;
    private Request[] frames;

    private static final int METHOD_TYPE_LRU = 0;
    private static final int METHOD_TYPE_FIFO = 1;

    Main(int frameSize, int requestAmount, int method) {
        this.method = method;
        this.frameSize = frameSize;
        this.requestsAmount = requestAmount;

        frames = new Request[frameSize];
        $$$setupUI$$$();

        requestButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int requestValue = Integer.parseInt(requestField.getText());

        if (requestDone < requestsAmount) {
            Request r = new Request();
            r.setValue(requestValue);

            if (hasFreeIndex(frames)) {
                if (contains(requestValue, frames)) {
                    int index = getRequestIndex(requestValue, frames);

                    r.setStatus(Request.STATUS_PAGE_HIT);
                    if (method == METHOD_TYPE_LRU)
                        r.setRequestTime(tracker);

                    frames[index] = r;

                    for (int i = 0; i < frames.length; i++) {
                        Request request = frames[i];
                        if (request != null)
                            requestsTable.setValueAt(request.getValue(), i, requestDone);
                    }

                    if (method == METHOD_TYPE_LRU)
                        tracker++;

                    requestDone++;
                } else {
                    int index = getFreeIndex(frames);

                    r.setStatus(Request.STATUS_PAGE_FAULT);
                    r.setRequestTime(tracker);

                    frames[index] = r;
                    for (int i = 0; i < frames.length; i++) {
                        Request request = frames[i];
                        if (request != null)
                            requestsTable.setValueAt(request.getValue(), i, requestDone);
                    }

                    requestDone++;
                }
            } else {
                if (contains(requestValue, frames)) {
                    int index = getRequestIndex(requestValue, frames);

                    r = getRequest(requestValue, frames);
                    r.setStatus(Request.STATUS_PAGE_HIT);

                    if (method == METHOD_TYPE_LRU)
                        r.setRequestTime(tracker);

                    frames[index] = r;

                    for (int i = 0; i < frames.length; i++) {
                        Request request = frames[i];
                        if (request != null)
                            requestsTable.setValueAt(request.getValue(), i, requestDone);
                    }

                    if (method == METHOD_TYPE_LRU)
                        tracker++;

                    requestDone++;
                } else {
                    int index = getIndexForReallocation(frames);

                    r.setStatus(Request.STATUS_PAGE_FAULT);
                    r.setRequestTime(tracker);
                    r.setValue(requestValue);

                    frames[index] = r;
                    for (int i = 0; i < frames.length; i++) {
                        Request request = frames[i];
                        if (request != null)
                            requestsTable.setValueAt(request.getValue(), i, requestDone);
                    }
                    tracker++;
                    requestDone++;
                }
            }
            requestsTable.updateUI();
        }
    }

    private static boolean hasFreeIndex(Request[] set) {
        if (set.length > 0) {
            for (Request request : set)
                if (request == null) return true;
            return false;
        }
        return false;
    }

    private static boolean contains(int r, Request[] set) {
        if (set.length > 0) {
            for (Request request : set) {
                if (request != null && request.getValue() == r)
                    return true;
            }
            return false;
        }
        return false;
    }

    private static int getRequestIndex(int v, Request[] set) {
        if (set.length > 0 && contains(v, set)) {
            for (int i = 0; i < set.length; i++) {
                if (set[i].getValue() == v)
                    return i;
            }
            return -1;
        }
        return -1;
    }

    private static int getFreeIndex(Request[] set) {
        if (set.length > 0) {
            for (int i = 0; i < set.length; i++) {
                if (set[i] == null)
                    return i;
            }
            return -1;
        }
        return -1;
    }

    private static int getIndexForReallocation(Request[] frames) {
        if (frames.length > 0) {
            int index = getFreeIndex(frames);
            if (index == -1) {
                Request request = frames[0];
                for (Request currentRequest : frames) {
                    if (request.getRequestTime() > currentRequest.getRequestTime())
                        request = currentRequest;
                }
                return getRequestIndex(request.getValue(), frames);
            } else
                return index;
        }
        return -1;
    }

    private static Request getRequest(int r, Request[] set) {
        Request request = null;

        if (set.length > 0) {
            for (Request _r : set)
                if (_r.getValue() == r) request = _r;
        }
        return request;
    }

    public JPanel getRoot() {
        return root;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        root = new JPanel();
        root.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        root.add(panel1, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        label1.setText("Enter Request:");
        panel1.add(label1);
        requestField = new JTextField();
        requestField.setColumns(10);
        panel1.add(requestField);
        requestButton = new JButton();
        requestButton.setText("OK");
        panel1.add(requestButton);
        root.add(requestsTable, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    private void createUIComponents() {
        requestsTable = new JTable(frameSize, requestsAmount);
    }
}
