package pl.amrusb.util.actions;

import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.algs.seg.imp.AdaptiveKMeans;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.ui.ClusterInputDialog;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;
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

public class KMeansAction implements ActionListener {
    private  KMeansAction.Types type;

    public KMeansAction(KMeansAction.Types type){
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
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
                    segmentation.set(getAlgorithm(clusterNum, current.getOriginalImage(), type));
                }
                else{
                    segmentation.set(getAlgorithm(clusterNum, current.getRescaledImage(), type));
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

    private AKMeans getAlgorithm(int k, BufferedImage image, KMeansAction.Types type){
        switch (type){
            case K_MEANS -> {
                return new KMeans(k, image);
            }
            case WEKA -> {
                return new WekaKMeans(k, image);
            }
            case ADAPTIVE -> {
                return new AdaptiveKMeans(k, image);
            }
        }
        return null;
    }
    public enum Types{
        K_MEANS,
        WEKA,
        ADAPTIVE;
    }
}
