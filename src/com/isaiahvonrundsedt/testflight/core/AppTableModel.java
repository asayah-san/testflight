package com.isaiahvonrundsedt.testflight.core;

import javax.swing.table.DefaultTableModel;

public class AppTableModel extends DefaultTableModel {

    public AppTableModel(int f, int r){
        super(f, r);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
