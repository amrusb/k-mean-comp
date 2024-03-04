package pl.amrusb.util.ui.panels;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TablePane extends JPanel {

    public TablePane(String title, JTable table){
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        JLabel lTitle = new JLabel(title, SwingConstants.CENTER);
        Border border = BorderFactory.createEtchedBorder();
        lTitle.setBorder(border);
        JScrollPane spTable = new JScrollPane(table);

        this.add(lTitle, BorderLayout.NORTH);
        this.add(spTable, BorderLayout.CENTER);
    }
}
