package pl.amrusb.util.actions;

import org.jfree.chart.JFreeChart;
import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.algs.seg.imp.AdaptiveKMeans;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.Calculations;
import pl.amrusb.util.Metrics;
import pl.amrusb.util.charts.ClusterSizesBarChart;
import pl.amrusb.util.charts.MetricsBarChart;
import pl.amrusb.util.charts.RGBHistogram;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.models.Point3D;
import pl.amrusb.util.ui.ClusterInputDialog;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;
import pl.amrusb.util.ui.panels.BottomPanel;
import pl.amrusb.util.ui.panels.ComparePanel;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

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


        ExecutorService segmentationExecutor = Executors.newFixedThreadPool(3);
        if (clusterNum != null) {
            current.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            AtomicReference<IKMeans> impKMeansAlg = new AtomicReference<>();
            AtomicReference<IKMeans> wekaKMeansAlg = new AtomicReference<>();
            AtomicReference<IKMeans> adaptKMeansAlg = new AtomicReference<>();

            Future<?> impKMeans = segmentationExecutor.submit(() -> {
                impKMeansAlg.set(new KMeans(clusterNum, current.getOriginalImage()));
                impKMeansAlg.get().execute();
            });

            Future<?> adaptKMeans = segmentationExecutor.submit(()->{
                adaptKMeansAlg.set(new AdaptiveKMeans(clusterNum, current.getOriginalImage()));
                adaptKMeansAlg.get().execute();
            });

            Future<?> wekaKMeans = segmentationExecutor.submit(() -> {
                wekaKMeansAlg.set(new WekaKMeans(clusterNum, current.getOriginalImage()));
                wekaKMeansAlg.get().execute();
            });


            try{
                impKMeans.get();
                wekaKMeans.get();
                adaptKMeans.get();

                BufferedImage leftImage = impKMeansAlg.get().getOutputImage();
                BufferedImage centerImage = adaptKMeansAlg.get().getOutputImage();
                BufferedImage rightImage = wekaKMeansAlg.get().getOutputImage();

                current.getComparePanel().setImageLabel(
                        leftImage,
                        ComparePanel.Position.LEFT
                );
                current.getComparePanel().setImageLabel(
                        centerImage,
                        ComparePanel.Position.CENTER
                );
                current.getComparePanel().setImageLabel(
                        rightImage,
                        ComparePanel.Position.RIGHT
                );

                Map<AKMeans.KMeansStats, Object> ownStats = impKMeansAlg.get().getStatistics();
                Map<AKMeans.KMeansStats, Object> wekaStats = wekaKMeansAlg.get().getStatistics();
                Map<AKMeans.KMeansStats, Object> adaptStats = adaptKMeansAlg.get().getStatistics();

                ArrayList<Double> jaccardIdx = calculateJaccardIndex(ownStats, adaptStats, wekaStats);
                ArrayList<Double> sorenDiceCoef = calculateDice(ownStats, adaptStats, wekaStats);
                ArrayList<Double> silhouetteScore = calculateSilhouette(ownStats, adaptStats, wekaStats, current.getOriginalImage());

                current.getComparePanel().setJaccardValues(jaccardIdx);
                current.getComparePanel().setDiceValues(sorenDiceCoef);
                current.getComparePanel().setSilhouetteValues(silhouetteScore);

                double[] jaccardIdxs = Metrics.JaccardIndex(
                        (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        clusterNum
                );
                double[] sorenDiceCoefs = Metrics.SorensenDiceCoefficient(
                        (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        clusterNum
                );

                BottomPanel.setProgressBarVisible(false);

                current.getComparePanel().setPropertiesValues(
                        current.getFileName(),
                        current.getWidth() * current.getHeight(),
                        clusterNum,
                        (Integer) ownStats.get(AKMeans.KMeansStats.ITERATIONS),
                        (Integer) adaptStats.get(AKMeans.KMeansStats.ITERATIONS),
                        (Integer) wekaStats.get(AKMeans.KMeansStats.ITERATIONS),
                        (Float) ownStats.get(AKMeans.KMeansStats.TIME),
                        (Float) adaptStats.get(AKMeans.KMeansStats.TIME),
                        (Float) wekaStats.get(AKMeans.KMeansStats.TIME)
                );


                ArrayList<Cluster> impClusters  = (ArrayList<Cluster>) ownStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS);
                ArrayList<Cluster> wekaClusters = (ArrayList<Cluster>)wekaStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS);
                ArrayList<Cluster> adaptClusters = (ArrayList<Cluster>) adaptStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS);
                current.getComparePanel().fillClustersTable(
                        impClusters,
                        adaptClusters,
                        wekaClusters
                );
                current.getComparePanel().fillInitialsTable(
                        (ArrayList<Point3D>) ownStats.get(AKMeans.KMeansStats.INITIAL_START_POINTS),
                        (ArrayList<Point3D>) adaptStats.get(AKMeans.KMeansStats.INITIAL_START_POINTS),
                        (ArrayList<Point3D>) wekaStats.get(AKMeans.KMeansStats.INITIAL_START_POINTS)
                );
                current.getComparePanel().fillClustersMetricsTable(jaccardIdxs, sorenDiceCoefs);

                JFreeChart metricsChart = MetricsBarChart.create(
                        jaccardIdxs,
                        sorenDiceCoefs,
                        impClusters,
                        wekaClusters
                );
                current.getComparePanel().setMetricsChart(metricsChart);

                JFreeChart sizesChart = ClusterSizesBarChart.create(impClusters, adaptClusters, wekaClusters);
                current.getComparePanel().setSizesChart(sizesChart);

                JFreeChart chMertrics = MetricsBarChart.create(jaccardIdx, sorenDiceCoef);
                current.getComparePanel().setChMetrics(chMertrics);
                JFreeChart chSilhouette = MetricsBarChart.create(silhouetteScore);
                current.getComparePanel().setChSilhouette(chSilhouette);
            }
            catch (InterruptedException | ExecutionException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            segmentationExecutor.shutdown();


            JFreeChart histogram = RGBHistogram.create(current.getOriginalImage());
            current.getComparePanel().setHistogram(histogram);

            current.changePanel(ImagePanel.COMPARE_PANEL);
            current.setCursor(Cursor.getDefaultCursor());
        }
    }
    private ArrayList<Double> calculateJaccardIndex(
            Map<AKMeans.KMeansStats, Object> impStats,
            Map<AKMeans.KMeansStats, Object> adaptStats,
            Map<AKMeans.KMeansStats, Object> wekaStats
    ){
        Double jaccardIdx = null;
        ArrayList<Double> jaccardIndex = new ArrayList<>();
        jaccardIdx = Metrics.JaccardIndex(
                (int[]) impStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[])  adaptStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        jaccardIdx = Calculations.round(jaccardIdx, 4);
        jaccardIndex.add(jaccardIdx);
        jaccardIdx = Metrics.JaccardIndex(
                (int[]) impStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        jaccardIdx = Calculations.round(jaccardIdx, 4);
        jaccardIndex.add(jaccardIdx);
        jaccardIdx = Metrics.JaccardIndex(
                (int[]) adaptStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[]) wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        jaccardIdx = Calculations.round(jaccardIdx, 4);
        jaccardIndex.add(jaccardIdx);

        return jaccardIndex;
    }
    private ArrayList<Double> calculateDice(
            Map<AKMeans.KMeansStats, Object> impStats,
            Map<AKMeans.KMeansStats, Object> adaptStats,
            Map<AKMeans.KMeansStats, Object> wekaStats
    ){
        Double dice = null;
        ArrayList<Double> diceCoefs = new ArrayList<>();
        dice = Metrics.SorensenDiceCoefficient(
                (int[]) impStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[])  adaptStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        dice = Calculations.round(dice, 4);
        diceCoefs.add(dice);
        dice = Metrics.SorensenDiceCoefficient(
                (int[]) impStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        dice = Calculations.round(dice, 4);
        diceCoefs.add(dice);
        dice = Metrics.SorensenDiceCoefficient(
                (int[]) adaptStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[]) wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        dice = Calculations.round(dice, 4);
        diceCoefs.add(dice);

        return diceCoefs;
    }

    private ArrayList<Double> calculateSilhouette(
            Map<AKMeans.KMeansStats, Object> impStats,
            Map<AKMeans.KMeansStats, Object> adaptStats,
            Map<AKMeans.KMeansStats, Object> wekaStats,
            BufferedImage image
    ){
        Double silhouette = null;
        ArrayList<Double> silhouetteScores = new ArrayList<>();

        silhouette = Metrics.SilhouetteScore(
                (ArrayList<Cluster>)impStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS),
                ImageReader.getPixelArray(image),
                (int[]) impStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        silhouette = Calculations.round(silhouette, 4);
        silhouetteScores.add(silhouette);

        silhouette = Metrics.SilhouetteScore(
                (ArrayList<Cluster>)adaptStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS),
                ImageReader.getPixelArray(image),
                (int[]) adaptStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        silhouette = Calculations.round(silhouette, 4);
        silhouetteScores.add(silhouette);

        silhouette = Metrics.SilhouetteScore(
                (ArrayList<Cluster>)wekaStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS),
                ImageReader.getPixelArray(image),
                (int[]) wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        silhouette = Calculations.round(silhouette, 4);
        silhouetteScores.add(silhouette);

        return silhouetteScores;
    }
}
