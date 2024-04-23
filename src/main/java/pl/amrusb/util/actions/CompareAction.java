package pl.amrusb.util.actions;

import org.jfree.chart.JFreeChart;
import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.algs.seg.imp.AdaptiveKMeans;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.Calculations;
import pl.amrusb.util.Metrics;
import pl.amrusb.util.charts.ClusterSizesBarChart;
import pl.amrusb.util.charts.MetricsBarChart;
import pl.amrusb.util.charts.RGBHistogram;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.constants.KMeansStats;
import pl.amrusb.util.constants.MetricsTypes;
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
import java.util.HashMap;
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

                Map<KMeansStats, Object> ownStats = impKMeansAlg.get().getStatistics();
                Map<KMeansStats, Object> wekaStats = wekaKMeansAlg.get().getStatistics();
                Map<KMeansStats, Object> adaptStats = adaptKMeansAlg.get().getStatistics();

                ArrayList<Double> jaccardIdx = calculateJaccardIndex(ownStats, adaptStats, wekaStats);
                ArrayList<Double> sorenDiceCoef = calculateDice(ownStats, adaptStats, wekaStats);
                ArrayList<Double> silhouetteScore = calculateSilhouette(ownStats, adaptStats, wekaStats, current.getOriginalImage());

                current.getComparePanel().setJaccardValues(jaccardIdx);
                current.getComparePanel().setDiceValues(sorenDiceCoef);
                current.getComparePanel().setSilhouetteValues(silhouetteScore);

                BottomPanel.setProgressBarVisible(false);

                current.getComparePanel().setPropertiesValues(
                        current.getFileName(),
                        current.getWidth() * current.getHeight(),
                        clusterNum,
                        (Integer) ownStats.get(KMeansStats.ITERATIONS),
                        (Integer) adaptStats.get(KMeansStats.ITERATIONS),
                        (Integer) wekaStats.get(KMeansStats.ITERATIONS),
                        (Float) ownStats.get(KMeansStats.TIME),
                        (Float) adaptStats.get(KMeansStats.TIME),
                        (Float) wekaStats.get(KMeansStats.TIME)
                );


                ArrayList<Cluster> impClusters  = (ArrayList<Cluster>) ownStats.get(KMeansStats.CLUSTER_CENTROIDS);
                ArrayList<Cluster> wekaClusters = (ArrayList<Cluster>)wekaStats.get(KMeansStats.CLUSTER_CENTROIDS);
                ArrayList<Cluster> adaptClusters = (ArrayList<Cluster>) adaptStats.get(KMeansStats.CLUSTER_CENTROIDS);
                current.getComparePanel().fillClustersTable(
                        impClusters,
                        adaptClusters,
                        wekaClusters
                );
                current.getComparePanel().fillInitialsTable(
                        (ArrayList<Point3D>) ownStats.get(KMeansStats.INITIAL_START_POINTS),
                        (ArrayList<Point3D>) adaptStats.get(KMeansStats.INITIAL_START_POINTS),
                        (ArrayList<Point3D>) wekaStats.get(KMeansStats.INITIAL_START_POINTS)
                );
                Map<MetricsTypes, Object> metrics = getClusterMetrics(ownStats, adaptStats,wekaStats, current.getOriginalImage());
                current.getComparePanel().fillClustersMetricTable(metrics);

                JFreeChart sizesChart = ClusterSizesBarChart.create(impClusters, adaptClusters, wekaClusters);
                current.getComparePanel().setSizesChart(sizesChart);

                JFreeChart chMertrics = MetricsBarChart.create(jaccardIdx, sorenDiceCoef);
                current.getComparePanel().setChMetrics(chMertrics);
                JFreeChart chSilhouette = MetricsBarChart.create(silhouetteScore);
                current.getComparePanel().setChSilhouette(chSilhouette);

                JFreeChart chClustersSilhouette = MetricsBarChart.create((Map<AlgorithmsMetrics, ArrayList<Double>>)metrics.get(MetricsTypes.SIHLOUETTE), MetricsTypes.SIHLOUETTE.getValue(), impClusters);

                JFreeChart chClustersJaccard = MetricsBarChart.create((Map<AlgorithmsMetrics, ArrayList<Double>>)metrics.get(MetricsTypes.JACCARD), MetricsTypes.JACCARD.getValue(),impClusters);
                JFreeChart chClustersDice = MetricsBarChart.create((Map<AlgorithmsMetrics, ArrayList<Double>>)metrics.get(MetricsTypes.DICE), MetricsTypes.DICE.getValue(),impClusters);

                current.getComparePanel().setChClustersMetrics(chClustersSilhouette,chClustersJaccard,chClustersDice);

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
            Map<KMeansStats, Object> impStats,
            Map<KMeansStats, Object> adaptStats,
            Map<KMeansStats, Object> wekaStats
    ){
        Double jaccardIdx = null;
        ArrayList<Double> jaccardIndex = new ArrayList<>();
        jaccardIdx = Metrics.JaccardIndex(
                (int[]) impStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  adaptStats.get(KMeansStats.ASSIGNMENTS)
        );
        jaccardIdx = Calculations.round(jaccardIdx, 4);
        jaccardIndex.add(jaccardIdx);
        jaccardIdx = Metrics.JaccardIndex(
                (int[]) impStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(KMeansStats.ASSIGNMENTS)
        );
        jaccardIdx = Calculations.round(jaccardIdx, 4);
        jaccardIndex.add(jaccardIdx);
        jaccardIdx = Metrics.JaccardIndex(
                (int[]) adaptStats.get(KMeansStats.ASSIGNMENTS),
                (int[]) wekaStats.get(KMeansStats.ASSIGNMENTS)
        );
        jaccardIdx = Calculations.round(jaccardIdx, 4);
        jaccardIndex.add(jaccardIdx);

        return jaccardIndex;
    }
    private ArrayList<Double> calculateDice(
            Map<KMeansStats, Object> impStats,
            Map<KMeansStats, Object> adaptStats,
            Map<KMeansStats, Object> wekaStats
    ){
        Double dice = null;
        ArrayList<Double> diceCoefs = new ArrayList<>();
        dice = Metrics.SorensenDiceCoefficient(
                (int[]) impStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  adaptStats.get(KMeansStats.ASSIGNMENTS)
        );
        dice = Calculations.round(dice, 4);
        diceCoefs.add(dice);
        dice = Metrics.SorensenDiceCoefficient(
                (int[]) impStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(KMeansStats.ASSIGNMENTS)
        );
        dice = Calculations.round(dice, 4);
        diceCoefs.add(dice);
        dice = Metrics.SorensenDiceCoefficient(
                (int[]) adaptStats.get(KMeansStats.ASSIGNMENTS),
                (int[]) wekaStats.get(KMeansStats.ASSIGNMENTS)
        );
        dice = Calculations.round(dice, 4);
        diceCoefs.add(dice);

        return diceCoefs;
    }

    private ArrayList<Double> calculateSilhouette(
            Map<KMeansStats, Object> impStats,
            Map<KMeansStats, Object> adaptStats,
            Map<KMeansStats, Object> wekaStats,
            BufferedImage image
    ){
        Double silhouette = null;
        ArrayList<Double> silhouetteScores = new ArrayList<>();

        silhouette = Metrics.SilhouetteScore(
                (ArrayList<Cluster>)impStats.get(KMeansStats.CLUSTER_CENTROIDS),
                ImageReader.getPixelArray(image),
                (int[]) impStats.get(KMeansStats.ASSIGNMENTS)
        );
        silhouette = Calculations.round(silhouette, 4);
        silhouetteScores.add(silhouette);

        silhouette = Metrics.SilhouetteScore(
                (ArrayList<Cluster>)adaptStats.get(KMeansStats.CLUSTER_CENTROIDS),
                ImageReader.getPixelArray(image),
                (int[]) adaptStats.get(KMeansStats.ASSIGNMENTS)
        );
        silhouette = Calculations.round(silhouette, 4);
        silhouetteScores.add(silhouette);

        silhouette = Metrics.SilhouetteScore(
                (ArrayList<Cluster>)wekaStats.get(KMeansStats.CLUSTER_CENTROIDS),
                ImageReader.getPixelArray(image),
                (int[]) wekaStats.get(KMeansStats.ASSIGNMENTS)
        );
        silhouette = Calculations.round(silhouette, 4);
        silhouetteScores.add(silhouette);

        return silhouetteScores;
    }

    private Map<MetricsTypes, Object> getClusterMetrics(
            Map<KMeansStats, Object> impStats,
            Map<KMeansStats, Object> adaptStats,
            Map<KMeansStats, Object> wekaStats,
            BufferedImage image
    ){
        ArrayList<Pixel> pixels = ImageReader.getPixelArray(image);

        Map<AlgorithmsMetrics, ArrayList<Double>> JaccardMetrics = new HashMap<>();
        Map<AlgorithmsMetrics, ArrayList<Double>> DiceMetrics = new HashMap<>();
        Map<AlgorithmsMetrics, ArrayList<Double>> SihlouetteMetrics = new HashMap<>();

        int clusterNum =
                ((ArrayList<Object>) impStats.get(KMeansStats.CLUSTER_CENTROIDS)).size();

        ArrayList<Double> jaccard= Metrics.JaccardIndex(
                (int[])  impStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  adaptStats.get(KMeansStats.ASSIGNMENTS),
                clusterNum
        );


        ArrayList<Double> dice = Metrics.SorensenDiceCoefficient(
                (int[]) impStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  adaptStats.get(KMeansStats.ASSIGNMENTS),
                clusterNum
        );
        JaccardMetrics.put(AlgorithmsMetrics.IMP_ADAPT, jaccard);
        DiceMetrics.put(AlgorithmsMetrics.IMP_ADAPT, dice);

        jaccard= Metrics.JaccardIndex(
                (int[]) impStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(KMeansStats.ASSIGNMENTS),
                clusterNum
        );
        dice = Metrics.SorensenDiceCoefficient(
                (int[]) impStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(KMeansStats.ASSIGNMENTS),
                clusterNum
        );

        JaccardMetrics.put(AlgorithmsMetrics.IMP_WEKA, jaccard);
        DiceMetrics.put(AlgorithmsMetrics.IMP_WEKA, dice);

        jaccard= Metrics.JaccardIndex(
                (int[]) adaptStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(KMeansStats.ASSIGNMENTS),
                clusterNum
        );
        dice = Metrics.SorensenDiceCoefficient(
                (int[]) adaptStats.get(KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(KMeansStats.ASSIGNMENTS),
                clusterNum
        );

        JaccardMetrics.put(AlgorithmsMetrics.ADAPT_WEKA, jaccard);
        DiceMetrics.put(AlgorithmsMetrics.ADAPT_WEKA, dice);

        ArrayList<Double> impSihlouette = new ArrayList<>();
        ArrayList<Double> adaptSihlouette = new ArrayList<>();
        ArrayList<Double> wekaSihlouette = new ArrayList<>();

        for (int i = 0; i < clusterNum; i++) {
            double silhouette = Metrics.SilhouetteScore(i,
                    (ArrayList<Cluster>) impStats.get(KMeansStats.CLUSTER_CENTROIDS),
                    pixels,
                    (int[]) impStats.get(KMeansStats.ASSIGNMENTS)
            );
            impSihlouette.add(Calculations.round(silhouette, 4));

            silhouette = Metrics.SilhouetteScore(i,
                    (ArrayList<Cluster>) adaptStats.get(KMeansStats.CLUSTER_CENTROIDS),
                    pixels,
                    (int[]) adaptStats.get(KMeansStats.ASSIGNMENTS)
            );
            adaptSihlouette.add(Calculations.round(silhouette,4 ));

            silhouette = Metrics.SilhouetteScore(i,
                    (ArrayList<Cluster>) wekaStats.get(KMeansStats.CLUSTER_CENTROIDS),
                    pixels,
                    (int[]) wekaStats.get(KMeansStats.ASSIGNMENTS)
            );
            wekaSihlouette.add(Calculations.round(silhouette,4));
        }

        SihlouetteMetrics.put(AlgorithmsMetrics.IMP, impSihlouette);
        SihlouetteMetrics.put(AlgorithmsMetrics.ADAPT, adaptSihlouette);
        SihlouetteMetrics.put(AlgorithmsMetrics.WEKA, wekaSihlouette);

        Map<MetricsTypes, Object> result = new HashMap<>();
        result.put(MetricsTypes.JACCARD, JaccardMetrics);
        result.put(MetricsTypes.DICE, DiceMetrics);
        result.put(MetricsTypes.SIHLOUETTE, SihlouetteMetrics);

        return result;
    }
}
