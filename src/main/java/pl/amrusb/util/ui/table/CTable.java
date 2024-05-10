package pl.amrusb.util.ui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CTable extends JTable {
    private final DefaultTableModel model;
    private final DefaultTableCellRenderer renderer;

    public CTable(){
        model = new CTableModel();
        this.setModel(model);
        renderer = new ColorTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        this.setAutoCreateRowSorter(false);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setRowHeight(25);

        this.getTableHeader().setResizingAllowed(false);
        this.setDefaultRenderer(Color.class, renderer);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void addColumn(String...columnsNames){
        for (String columnName :
                columnsNames) {
            model.addColumn(columnName);
        }
    }

    public void setColumnWidth(String columnWidth){
        String[] widths = columnWidth.split(",");

        for (int i = 0; i < widths.length; i++) {
            this.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(Integer.parseInt(widths[i].trim()));
        }
    }

    public void addRow(Object[] data){
        model.addRow(data);
    }

    public void setRenderer(){
        for (int i = 0; i < this.getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        this.setPreferredScrollableViewportSize(this.getPreferredSize());
    }
}
