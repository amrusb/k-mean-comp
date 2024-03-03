package pl.amrusb.util.ui.table;

import javax.swing.table.DefaultTableModel;

public class CTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }
}
