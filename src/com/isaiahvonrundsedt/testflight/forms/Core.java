package com.isaiahvonrundsedt.testflight.forms;

import com.isaiahvonrundsedt.testflight.core.AppTableModel;
import com.isaiahvonrundsedt.testflight.core.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Core implements ActionListener {
    private JPanel root;
    private JTextField requestField;
    private JButton requestButton;
    private JTable requestsTable;
    private JTable statusTable;

    private int method;
    private int frameSize;
    private int requestsAmount;

    private int tracker = 0;
    private int requestDone = 0;
    private Request[] frames;

    private static final int METHOD_TYPE_LRU = 0;
    private static final int METHOD_TYPE_FIFO = 1;

    Core(int frameSize, int requestAmount, int method) {
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
                    updateTable(r.getStatus(), requestDone);
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
                    updateTable(r.getStatus(), requestDone);
                    tracker++;
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
                    updateTable(r.getStatus(), requestDone);

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
                    updateTable(r.getStatus(), requestDone);
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

    private void updateTable(int status, int column) {
        char stat;
        if (status == Request.STATUS_PAGE_FAULT)
            stat = '*';
        else
            stat = '-';

        statusTable.setValueAt(stat, 0, column);
        statusTable.updateUI();
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

    JPanel getRoot() {
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
        root.setLayout(new BorderLayout(16, 16));
        root.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16), null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        root.add(panel1, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Slate For OnePlus", Font.PLAIN, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Enter Request:");
        panel1.add(label1);
        requestField = new JTextField();
        requestField.setColumns(10);
        Font requestFieldFont = this.$$$getFont$$$("Slate For OnePlus", Font.PLAIN, 14, requestField.getFont());
        if (requestFieldFont != null) requestField.setFont(requestFieldFont);
        panel1.add(requestField);
        requestButton = new JButton();
        Font requestButtonFont = this.$$$getFont$$$("Slate For OnePlus", Font.PLAIN, 14, requestButton.getFont());
        if (requestButtonFont != null) requestButton.setFont(requestButtonFont);
        requestButton.setText("OK");
        panel1.add(requestButton);
        Font requestsTableFont = this.$$$getFont$$$("Slate For OnePlus", Font.PLAIN, 14, requestsTable.getFont());
        if (requestsTableFont != null) requestsTable.setFont(requestsTableFont);
        root.add(requestsTable, BorderLayout.CENTER);
        Font statusTableFont = this.$$$getFont$$$("Slate For OnePlus", Font.PLAIN, 14, statusTable.getFont());
        if (statusTableFont != null) statusTable.setFont(statusTableFont);
        statusTable.setShowHorizontalLines(false);
        statusTable.setShowVerticalLines(false);
        root.add(statusTable, BorderLayout.SOUTH);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    private void createUIComponents() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        int defaultCellSize = 20;

        requestsTable = new JTable(new AppTableModel(frameSize, requestsAmount));
        requestsTable.setRowHeight(defaultCellSize);

        TableColumnModel requestModel = requestsTable.getColumnModel();
        for (int i = 0; i < requestsTable.getColumnModel().getColumnCount(); i++) {
            requestModel.getColumn(i).setPreferredWidth(defaultCellSize);
            requestModel.getColumn(i).setCellRenderer(renderer);
        }

        statusTable = new JTable(new AppTableModel(1, requestsAmount));
        statusTable.setRowHeight(defaultCellSize);

        TableColumnModel statusModel = statusTable.getColumnModel();
        for (int i = 0; i < statusTable.getColumnModel().getColumnCount(); i++) {
            statusModel.getColumn(i).setPreferredWidth(defaultCellSize);
            statusModel.getColumn(i).setCellRenderer(renderer);
        }
    }
}
