package pl.amrusb.util.threads;

import lombok.AllArgsConstructor;
import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.img.ImageRescaler;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;
import pl.amrusb.util.ui.panels.BottomPanel;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor
public class WekaKMeansThread extends Thread{
    private final Boolean original;
    private final Integer clusterNum;
    private final JMenuItem action;

    @Override
    public void run(){
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();

        MainMenuBar.getOwner().setCursor(
                Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        );
        BottomPanel.setProgressBarVisible(true);

        IKMeans segmentation;
        Long start = System.currentTimeMillis();

        if (original) {
            segmentation = new WekaKMeans(clusterNum, current.getOriginalImage());
        } else {
            segmentation = new WekaKMeans(clusterNum, current.getRescaledImage());
        }

        segmentation.execute();

        Long elapsedTimeMillis = System.currentTimeMillis() - start;
        Float elapsedTimeSec = elapsedTimeMillis / 1000F;
        BottomPanel.setDurationTime(elapsedTimeSec);

        action.setEnabled(true);
        BufferedImage output = segmentation.getOutputImage();

        current.setImageLabel(output);
        if (!original) {
            Double scale = (double) current.getOriginalImage().getWidth() /  output.getWidth();
            output = ImageRescaler.rescaleImage(output, scale);
        }
        current.setSegmentedImage(output);

        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());

        BottomPanel.setProgressBarVisible(false);
        BottomPanel.setDurationInfoVisible(true);
    }
}
