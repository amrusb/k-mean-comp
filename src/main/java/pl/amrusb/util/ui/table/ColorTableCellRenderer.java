package pl.amrusb.util.ui.table;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import java.awt.Color;

public class ColorTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column
    ) {
        Component component = super.getTableCellRendererComponent(
                table, value,
                isSelected, hasFocus,
                row, column
        );

        if (value instanceof Color) {
            Color color = (Color) value;
            component.setBackground(color);
            setText("");
        }
        else if (isSelected) {
            component.setBackground(table.getSelectionBackground());
            component.setForeground(table.getSelectionForeground());
        } else {
            component.setBackground(table.getBackground());
            component.setForeground(table.getForeground());
            setText((value == null) ? "" : value.toString());
        }

        return component;
    }
}

