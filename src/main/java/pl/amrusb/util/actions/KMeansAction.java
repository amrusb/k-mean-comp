package pl.amrusb.util.actions;

import pl.amrusb.util.threads.KMeansThread;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.panels.BottomPanel;
import pl.amrusb.util.ui.ClusterInputDialog;
import pl.amrusb.util.ui.MainMenuBar;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KMeansAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
        BottomPanel.setDurationInfoVisible(false);
        if (current.hasSegmentedImage()) {
            current.setOriginalImage(current.getSegmentedImage());
        }
        ClusterInputDialog dialog = new ClusterInputDialog(
                MainMenuBar.getOwner(),
                current.hasRescaledImage()
        );
        dialog.setVisible(true);

        Integer clusterNum = dialog.getClusterCount();
        Boolean original = dialog.checkImageSource();

        if (clusterNum != null) {
            new KMeansThread(
                    original,
                    clusterNum,
                    (JMenuItem) e.getSource()
            ).start();
        }
    }
}
