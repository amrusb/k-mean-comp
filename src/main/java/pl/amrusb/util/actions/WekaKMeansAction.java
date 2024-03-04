package pl.amrusb.util.actions;

import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.ui.ClusterInputDialog;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;
import pl.amrusb.util.ui.panels.BottomPanel;
import pl.amrusb.util.ui.panels.ImagePanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class WekaKMeansAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

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
            current.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            AtomicReference<IKMeans> segmentation = new AtomicReference<>();
            Future<?> segmenationExec = executor.submit(()->{
                if(original){
                    segmentation.set(new WekaKMeans(clusterNum, current.getOriginalImage()));
                }
                else{
                    segmentation.set(new WekaKMeans(clusterNum, current.getRescaledImage()));
                }
                segmentation.get().execute();
            });

            executor.shutdown();

            try {
                segmenationExec.get();
                BufferedImage output = segmentation.get().getOutputImage();
                current.setImageLabel(output);
                current.setSegmentedImage(output);

                current.setCursor(Cursor.getDefaultCursor());

            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
