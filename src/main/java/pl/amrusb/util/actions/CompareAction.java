package pl.amrusb.util.actions;

import org.jfree.chart.JFreeChart;
import pl.amrusb.util.charts.RGBHistogram;
import pl.amrusb.util.threads.CompareThread;
import pl.amrusb.util.ui.ClusterInputDialog;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompareAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
        ClusterInputDialog dialog = new ClusterInputDialog(
                MainMenuBar.getOwner(),
                false
        );
        dialog.setVisible(true);
        Integer clusterNum = dialog.getClusterCount();

        new CompareThread(clusterNum, (JMenuItem) e.getSource() ).start();

        JFreeChart histogram = RGBHistogram.create(current.getOriginalImage());
        current.getComparePanel().setHistogram(histogram);

        current.changePanel(ImagePanel.COMPARE_PANEL);
    }
}
