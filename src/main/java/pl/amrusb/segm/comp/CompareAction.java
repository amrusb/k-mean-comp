package pl.amrusb.segm.comp;

import lombok.SneakyThrows;
import org.jfree.chart.JFreeChart;
import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.algs.seg.imp.AdaptiveKMeans;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.math.Calculations;
import pl.amrusb.util.math.ClusterSorter;
import pl.amrusb.util.math.Metrics;
import pl.amrusb.util.models.*;
import pl.amrusb.util.charts.ClusterSizesBarChart;
import pl.amrusb.util.charts.MetricsBarChart;
import pl.amrusb.util.charts.RGBHistogram;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.constants.KMeansStats;
import pl.amrusb.util.constants.MetricsTypes;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.ui.ClusterInputDialog;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.ui.MainMenuBar;
import pl.amrusb.segm.ImageWidow;

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
    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        ImageWidow current = (ImageWidow) MainFrame.getTabbedPane().getSelectedComponent();
        ClusterInputDialog dialog = new ClusterInputDialog(
                MainMenuBar.getOwner(),
                "Parametry porównania algorytmów k-means",
                false
        );
        dialog.setVisible(true);
        Integer clusterNum = dialog.getClusterCount();
        Integer maxIter = dialog.getMaxIter();


        ExecutorService segmentationExecutor = Executors.newFixedThreadPool(3);
        if (clusterNum != null) {
            current.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            AtomicReference<IKMeans> impKMeansAlg = new AtomicReference<>();
            AtomicReference<IKMeans> wekaKMeansAlg = new AtomicReference<>();
            AtomicReference<IKMeans> adaptKMeansAlg = new AtomicReference<>();

            Future<?> impKMeans = segmentationExecutor.submit(() -> {
                impKMeansAlg.set(new KMeans(clusterNum,maxIter, current.getBfIOriginal()));
                impKMeansAlg.get().execute();
            });

            Future<?> adaptKMeans = segmentationExecutor.submit(()->{
                adaptKMeansAlg.set(new AdaptiveKMeans(clusterNum,maxIter, current.getBfIOriginal()));
                adaptKMeansAlg.get().execute();
            });

            Future<?> wekaKMeans = segmentationExecutor.submit(() -> {
                wekaKMeansAlg.set(new WekaKMeans(clusterNum,maxIter, current.getBfIOriginal()));
                wekaKMeansAlg.get().execute();
            });


            try{
                impKMeans.get();
                wekaKMeans.get();
                adaptKMeans.get();

                BufferedImage leftImage = impKMeansAlg.get().getOutputImage();
                BufferedImage centerImage = adaptKMeansAlg.get().getOutputImage();
                BufferedImage rightImage = wekaKMeansAlg.get().getOutputImage();

                current.getCwCompare().setImageLabel(
                        leftImage,
                        AlgorithmsMetrics.IMP
                );
                current.getCwCompare().setImageLabel(
                        centerImage,
                        AlgorithmsMetrics.ADAPT
                );
                current.getCwCompare().setImageLabel(
                        rightImage,
                        AlgorithmsMetrics.WEKA
                );

                Map<AlgorithmsMetrics, Statistics> stats = new HashMap<>();
                stats.put(AlgorithmsMetrics.IMP, impKMeansAlg.get().getStatistics());
                stats.put(AlgorithmsMetrics.ADAPT, adaptKMeansAlg.get().getStatistics());
                stats.put(AlgorithmsMetrics.WEKA, wekaKMeansAlg.get().getStatistics());

                Map<AlgorithmsMetrics, ArrayList<Cluster>> sortedClusters  = new HashMap<>();

                sortedClusters.put(AlgorithmsMetrics.IMP,(ArrayList<Cluster>) stats.get(AlgorithmsMetrics.IMP).get(KMeansStats.CLUSTER_CENTROIDS));
                sortedClusters.put(AlgorithmsMetrics.WEKA,(ArrayList<Cluster>)stats.get(AlgorithmsMetrics.WEKA).get(KMeansStats.CLUSTER_CENTROIDS));
                sortedClusters.put(AlgorithmsMetrics.ADAPT,(ArrayList<Cluster>) stats.get(AlgorithmsMetrics.ADAPT).get(KMeansStats.CLUSTER_CENTROIDS));

                sortedClusters = ClusterSorter.hungarianAlgorithm(sortedClusters);

                ArrayList<Cluster> impClusters = sortedClusters.get(AlgorithmsMetrics.IMP);
                ArrayList<Cluster> wekaClusters = sortedClusters.get(AlgorithmsMetrics.WEKA);
                ArrayList<Cluster> adaptClusters = sortedClusters.get(AlgorithmsMetrics.ADAPT);

                int[] impAssign = reassignment((int[]) stats.get(AlgorithmsMetrics.IMP).get(KMeansStats.ASSIGNMENTS), impClusters);
                int[] adaptAssign = reassignment((int[]) stats.get(AlgorithmsMetrics.ADAPT).get(KMeansStats.ASSIGNMENTS), adaptClusters);
                int[] wekaAssign = reassignment((int[]) stats.get(AlgorithmsMetrics.WEKA).get(KMeansStats.ASSIGNMENTS), wekaClusters);

                stats.get(AlgorithmsMetrics.IMP).put(KMeansStats.ASSIGNMENTS, impAssign);
                stats.get(AlgorithmsMetrics.IMP).put(KMeansStats.CLUSTER_CENTROIDS, impClusters);
                stats.get(AlgorithmsMetrics.ADAPT).put(KMeansStats.ASSIGNMENTS, adaptAssign);
                stats.get(AlgorithmsMetrics.ADAPT).put(KMeansStats.CLUSTER_CENTROIDS, adaptClusters);
                stats.get(AlgorithmsMetrics.WEKA).put(KMeansStats.ASSIGNMENTS, wekaAssign);
                stats.get(AlgorithmsMetrics.WEKA).put(KMeansStats.CLUSTER_CENTROIDS, wekaClusters);

                ArrayList<Double> jaccardIdx = calculateJaccardIndex(
                        stats.get(AlgorithmsMetrics.IMP),
                        stats.get(AlgorithmsMetrics.ADAPT),
                        stats.get(AlgorithmsMetrics.WEKA)
                );
                ArrayList<Double> sorenDiceCoef = calculateDice(
                        stats.get(AlgorithmsMetrics.IMP),
                        stats.get(AlgorithmsMetrics.ADAPT),
                        stats.get(AlgorithmsMetrics.WEKA)
                );
                ArrayList<Double> silhouetteScore = calculateSilhouette(
                        stats.get(AlgorithmsMetrics.IMP),
                        stats.get(AlgorithmsMetrics.ADAPT),
                        stats.get(AlgorithmsMetrics.WEKA),
                        current.getBfIOriginal()
                );

                current.getCwCompare().setJaccardValues(jaccardIdx);
                current.getCwCompare().setDiceValues(sorenDiceCoef);
                current.getCwCompare().setSilhouetteValues(silhouetteScore);

                current.getCwCompare().setPropertiesValues(
                        current.getFileName(),
                        impAssign.length,
                        clusterNum,
                        maxIter,
                        (Integer) stats.get(AlgorithmsMetrics.IMP).get(KMeansStats.ITERATIONS),
                        (Integer) stats.get(AlgorithmsMetrics.ADAPT).get(KMeansStats.ITERATIONS),
                        (Integer) stats.get(AlgorithmsMetrics.WEKA).get(KMeansStats.ITERATIONS),
                        (Float) stats.get(AlgorithmsMetrics.IMP).get(KMeansStats.TIME),
                        (Float) stats.get(AlgorithmsMetrics.ADAPT).get(KMeansStats.TIME),
                        (Float) stats.get(AlgorithmsMetrics.WEKA).get(KMeansStats.TIME)
                );



                current.getCwCompare().fillClustersTable(
                        impClusters,
                        adaptClusters,
                        wekaClusters
                );
                current.getCwCompare().fillInitialsTable(
                        (ArrayList<Point3D>) stats.get(AlgorithmsMetrics.IMP).get(KMeansStats.INITIAL_START_POINTS),
                        (ArrayList<Point3D>) stats.get(AlgorithmsMetrics.ADAPT).get(KMeansStats.INITIAL_START_POINTS),
                        (ArrayList<Point3D>) stats.get(AlgorithmsMetrics.WEKA).get(KMeansStats.INITIAL_START_POINTS)
                );
                Map<MetricsTypes, Object> metrics = getClusterMetrics(
                        stats.get(AlgorithmsMetrics.IMP),
                        stats.get(AlgorithmsMetrics.ADAPT),
                        stats.get(AlgorithmsMetrics.WEKA),
                        current.getBfIOriginal());
                current.getCwCompare().fillClustersMetricTable(metrics);

                JFreeChart sizesChart = ClusterSizesBarChart.create(impClusters, adaptClusters, wekaClusters);
                current.getCwCompare().setSizesChart(sizesChart);

                JFreeChart chMertrics = MetricsBarChart.create(jaccardIdx, sorenDiceCoef);
                current.getCwCompare().setChMetrics(chMertrics);
                JFreeChart chSilhouette = MetricsBarChart.create(silhouetteScore);
                current.getCwCompare().setChSilhouette(chSilhouette);

                JFreeChart chClustersSilhouette = MetricsBarChart.create(
                        (Map<AlgorithmsMetrics, ArrayList<Double>>)metrics.get(MetricsTypes.SIHLOUETTE),
                        MetricsTypes.SIHLOUETTE.getValue(),
                        impClusters);

                JFreeChart chClustersJaccard = MetricsBarChart.create(
                        (Map<AlgorithmsMetrics, ArrayList<Double>>)metrics.get(MetricsTypes.JACCARD),
                        MetricsTypes.JACCARD.getValue(),
                        impClusters);
                JFreeChart chClustersDice = MetricsBarChart.create(
                        (Map<AlgorithmsMetrics, ArrayList<Double>>)metrics.get(MetricsTypes.DICE),
                        MetricsTypes.DICE.getValue(),
                        impClusters);

                current.getCwCompare().setChClustersMetrics(chClustersSilhouette,chClustersJaccard,chClustersDice);
                MainMenuBar.enableUndo(true);
                MainMenuBar.enableAlgorithms(false);
            }
            catch (InterruptedException | ExecutionException ex) {
                throw ex;
            }

            segmentationExecutor.shutdown();


            JFreeChart histogram = RGBHistogram.create(current.getBfIOriginal());
            current.getCwCompare().setHistogram(histogram);

            current.changePanel(ImageWidow.COMPARE_PANEL);
            current.setIsEdited(true);
            MainFrame.setTabTitle(current, true);
            current.setCursor(Cursor.getDefaultCursor());
        }
    }
    private ArrayList<Double> calculateJaccardIndex(
            Map<KMeansStats, Object> impStats,
            Map<KMeansStats, Object> adaptStats,
            Map<KMeansStats, Object> wekaStats
    ){
        double jaccardIdx;
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
        double dice;
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
        double silhouette;
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

    public int[] reassignment(int[] assignments, ArrayList<Cluster> clusters){
        int[] newAssignments = new int[assignments.length];
        int[] clustersPositions = new int[clusters.size()];

        int i = 0;
        for (Cluster cluster: clusters) {
            clustersPositions[i] = cluster.getOrdinal();
            i++;
        }

        for(i = 0; i < clustersPositions.length; i++){
            int old = clustersPositions[i];

            for (int j = 0; j < assignments.length; j++) {
                if(assignments[j] == old){
                    newAssignments[j] = i;
                }
            }
        }

        return newAssignments;
    }
}
