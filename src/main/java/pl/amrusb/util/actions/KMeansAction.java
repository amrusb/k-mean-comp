package pl.amrusb.util.actions;

import pl.amrusb.util.threads.KMeansThread;
import pl.amrusb.util.ui.BottomPanel;
import pl.amrusb.util.ui.ClusterInputDialog;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KMeansAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        BottomPanel.setDurationInfoVisible(false);
        if (MainFrame.hasSegmentedImage()) {
            MainFrame.setImage(MainFrame.getSegmentedImage());
        }
        ClusterInputDialog dialog = new ClusterInputDialog(
                MainMenuBar.getOwner(),
                MainFrame.hasRescaledImage()
        );
        dialog.setVisible(true);

        Integer clusterNum = dialog.getClusterCount();
        Boolean original = dialog.checkImageSource();

        if (clusterNum > 0) {
            new KMeansThread(
                    original,
                    clusterNum,
                    (JMenuItem) e.getSource()
            ).start();
        }
    }
}
