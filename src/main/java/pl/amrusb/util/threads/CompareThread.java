package pl.amrusb.util.threads;

import lombok.AllArgsConstructor;
import pl.amrusb.algs.seg.*;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;

import pl.amrusb.util.ui.panels.BottomPanel;
import pl.amrusb.util.ui.panels.ComparePanel;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor
public class CompareThread extends Thread{
    private final Integer clusterNum;
    private final JMenuItem action;

    @Override
    public void run(){
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
        BufferedImage leftImage, rightImage;
        IKMeans segmentation;

        BottomPanel.setProgressBarVisible(true);
        MainMenuBar.getOwner().setCursor(
                Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        );

        segmentation = new KMeans(clusterNum, current.getOriginalImage());
        segmentation.execute();

        leftImage = segmentation.getOutputImage();



        segmentation = new WekaKMeans(clusterNum, current.getOriginalImage());
        segmentation.execute();

        rightImage = segmentation.getOutputImage();


        current.getComparePanel().setImageLabel(
                leftImage,
                ComparePanel.Position.LEFT
        );
        current.getComparePanel().setImageLabel(
                rightImage,
                ComparePanel.Position.RIGHT
        );

        action.setEnabled(true);
        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }
}
