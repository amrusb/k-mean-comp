package pl.amrusb.util.actions;

import org.jfree.chart.JFreeChart;
import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.Calculations;
import pl.amrusb.util.Metrics;
import pl.amrusb.util.charts.ClusterSizesBarChart;
import pl.amrusb.util.charts.MetricsBarChart;
import pl.amrusb.util.charts.RGBHistogram;
import pl.amrusb.util.models.Cluster;
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
    private BufferedImage leftImage, rightImage;
    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
        ClusterInputDialog dialog = new ClusterInputDialog(
                MainMenuBar.getOwner(),
                false
        );
        dialog.setVisible(true);
        Integer clusterNum = dialog.getClusterCount();


        ExecutorService segmentationExecutor = Executors.newFixedThreadPool(2);
        if (clusterNum != null) {
            current.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            AtomicReference<IKMeans> impKMeansAlg = new AtomicReference<>();
            AtomicReference<IKMeans> wekaKMeansAlg = new AtomicReference<>();
            Future<?> impKMeans = segmentationExecutor.submit(() -> {
                impKMeansAlg.set(new KMeans(clusterNum, current.getOriginalImage()));
                impKMeansAlg.get().execute();
            });

            Future<?> wekaKMeans = segmentationExecutor.submit(() -> {
                wekaKMeansAlg.set(new WekaKMeans(clusterNum, current.getOriginalImage()));
                wekaKMeansAlg.get().execute();
            });


            try{
                impKMeans.get();
                wekaKMeans.get();

                leftImage = impKMeansAlg.get().getOutputImage();
                rightImage = wekaKMeansAlg.get().getOutputImage();

                current.getComparePanel().setImageLabel(
                        leftImage,
                        ComparePanel.Position.LEFT
                );
                current.getComparePanel().setImageLabel(
                        rightImage,
                        ComparePanel.Position.RIGHT
                );

                Map<AKMeans.KMeansStats, Object> ownStats = impKMeansAlg.get().getStatistics();
                Map<AKMeans.KMeansStats, Object> wekaStats = wekaKMeansAlg.get().getStatistics();

                Double jaccardIdx = Metrics.JaccardIndex(
                        (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
                );
                jaccardIdx = Calculations.round(jaccardIdx, 4);

                double[] jaccardIdxs = Metrics.JaccardIndex(
                        (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        clusterNum
                );
                Double sorenDiceCoef = Metrics.SorensenDiceCoefficient(
                        (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
                );
                sorenDiceCoef = Calculations.round(sorenDiceCoef, 4);
                double[] sorenDiceCoefs = Metrics.SorensenDiceCoefficient(
                        (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                        clusterNum
                );
                Double mse = Metrics.MeanSquareError((int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),(int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS));
                Double rmse = Math.sqrt(mse);
                mse = Calculations.round(mse, 4);
                rmse = Calculations.round(rmse, 4);

                MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
                BottomPanel.setProgressBarVisible(false);

                current.getComparePanel().setPropertiesValues(
                        current.getFileName(),
                        current.getWidth() * current.getHeight(),
                        clusterNum,
                        (Integer)ownStats.get(AKMeans.KMeansStats.ITERATIONS),
                        (Integer) wekaStats.get(AKMeans.KMeansStats.ITERATIONS),
                        (Float) ownStats.get(AKMeans.KMeansStats.TIME),
                        (Float) wekaStats.get(AKMeans.KMeansStats.TIME)
                );

                current.getComparePanel().setMetricsValues(jaccardIdx,sorenDiceCoef,mse,rmse);

                ArrayList<Cluster> impClusters  = (ArrayList<Cluster>) ownStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS);
                ArrayList<Cluster> wekaClusters = (ArrayList<Cluster>)wekaStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS);
                current.getComparePanel().fillClustersTable(
                        impClusters,
                        wekaClusters
                );
                current.getComparePanel().fillInitialsTable(
                        (ArrayList<Point3D>) ownStats.get(AKMeans.KMeansStats.INITIAL_START_POINTS),
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

                JFreeChart sizesChart = ClusterSizesBarChart.create(impClusters, wekaClusters);
                current.getComparePanel().setSizesChart(sizesChart);

                JFreeChart mainMetrics = MetricsBarChart.create(jaccardIdx, sorenDiceCoef, mse, rmse);
                current.getComparePanel().setMainMetriscChart(mainMetrics);
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
}
