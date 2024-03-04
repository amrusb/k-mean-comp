package pl.amrusb.util.threads;

import lombok.AllArgsConstructor;
import org.jfree.chart.JFreeChart;
import pl.amrusb.algs.seg.*;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.Calculations;
import pl.amrusb.util.Metrics;
import pl.amrusb.util.charts.ClusterSizesBarChart;
import pl.amrusb.util.charts.MetricsBarChart;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Point3D;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;

import pl.amrusb.util.ui.panels.BottomPanel;
import pl.amrusb.util.ui.panels.ComparePanel;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

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

        Map<AKMeans.KMeansStats, Object> ownStats = segmentation.getStatistics();

        leftImage = segmentation.getOutputImage();

        segmentation = new WekaKMeans(clusterNum, current.getOriginalImage());
        segmentation.execute();

        Map<AKMeans.KMeansStats, Object> wekaStats = segmentation.getStatistics();

        rightImage = segmentation.getOutputImage();


        current.getComparePanel().setImageLabel(
                leftImage,
                ComparePanel.Position.LEFT
        );
        current.getComparePanel().setImageLabel(
                rightImage,
                ComparePanel.Position.RIGHT
        );

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
        action.setEnabled(true);

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
    }
}
