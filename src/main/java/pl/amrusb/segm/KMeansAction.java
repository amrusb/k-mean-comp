package pl.amrusb.segm;

import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.algs.seg.imp.AdaptiveKMeans;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.ui.ClusterInputDialog;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.ui.MainMenuBar;

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
    private final AlgorithmsMetrics type;

    public KMeansAction(AlgorithmsMetrics type){
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        ImageWidow current = (ImageWidow) MainFrame.getTabbedPane().getSelectedComponent();
        if (current.hasSegmentedImage()) {
            current.setBfIOriginal(current.getBfISegmented());
        }
        ClusterInputDialog dialog = new ClusterInputDialog(
                MainMenuBar.getOwner(),
                "Parametry k-means ("+ type.getValue() +")",
                current.hasRescaledImage()
        );
        dialog.setVisible(true);

        Integer clusterNum = dialog.getClusterCount();
        Integer maxIter = dialog.getMaxIter();
        Boolean original = dialog.checkImageSource();

        if (clusterNum != null) {
            current.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            AtomicReference<IKMeans> segmentation = new AtomicReference<>();
            Future<?> segmenationExec = executor.submit(()->{
                if(original){
                    segmentation.set(getAlgorithm(clusterNum,maxIter, current.getBfIOriginal(), type));
                }
                else{
                    segmentation.set(getAlgorithm(clusterNum,maxIter, current.getBfIRescaled(), type));
                }
                segmentation.get().execute();
            });

            executor.shutdown();

            try {
                segmenationExec.get();
                BufferedImage output = segmentation.get().getOutputImage();
                current.setlImage(output);
                current.setBfISegmented(output);
                current.setIsEdited(true);
                MainFrame.setTabTitle(current, true);
                current.setCursor(Cursor.getDefaultCursor());
                MainMenuBar.enableUndo(true);
                MainMenuBar.enableAlgorithms(false);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private AKMeans getAlgorithm(int k,int maxIter, BufferedImage image, AlgorithmsMetrics type){
        switch (type){
            case IMP -> {
                return new KMeans(k,maxIter, image);
            }
            case WEKA -> {
                return new WekaKMeans(k,maxIter, image);
            }
            case ADAPT -> {
                return new AdaptiveKMeans(k,maxIter, image);
            }
        }
        return null;
    }
}
