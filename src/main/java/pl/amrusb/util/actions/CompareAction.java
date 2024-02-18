package pl.amrusb.util.actions;

import org.jfree.chart.JFreeChart;
import pl.amrusb.util.charts.RGBHistogram;
import pl.amrusb.util.ui.ClusterInputDialog;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;
import pl.amrusb.util.ui.panels.ComparePanel;
import pl.amrusb.util.ui.panels.ImagePanel;

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
        current.getComparePanel().setClusterNum(clusterNum);

        current.getComparePanel()
                .setImageLabel(
                        current.getOriginalImage(),
                        ComparePanel.Position.LEFT
                );
        current.getComparePanel()
                .setImageLabel(
                        current.getOriginalImage(),
                        ComparePanel.Position.RIGHT
                );

        JFreeChart chart = RGBHistogram.create(current.getOriginalImage());
        current.getComparePanel().setImageChart(chart);

        current.getComparePanel().setImageName(current.getFileName());
        current.changePanel(ImagePanel.COMPARE_PANEL);
    }
}
