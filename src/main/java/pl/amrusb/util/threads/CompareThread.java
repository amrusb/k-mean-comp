package pl.amrusb.util.threads;

import lombok.AllArgsConstructor;
import pl.amrusb.algs.seg.*;
import pl.amrusb.algs.seg.imp.KMeans;
import pl.amrusb.algs.seg.weka.WekaKMeans;
import pl.amrusb.util.Calculations;
import pl.amrusb.util.Metrics;
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
        double[] jaccardIdxs = Metrics.JaccardIndex(
                (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                clusterNum
        );
        Double sorenDiceCoef = Metrics.SorensenDiceCoefficient(
                (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS)
        );
        double[] sorenDiceCoefs = Metrics.SorensenDiceCoefficient(
                (int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                (int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS),
                clusterNum
        );
        Double mse = Metrics.MeanSquareError((int[]) ownStats.get(AKMeans.KMeansStats.ASSIGNMENTS),(int[])  wekaStats.get(AKMeans.KMeansStats.ASSIGNMENTS));
        Double rmse = Math.sqrt(mse);

        action.setEnabled(true);

        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
        BottomPanel.setProgressBarVisible(false);


        current.getComparePanel().addPropTableRow("<html><b>Indeks Jaccard'a</b></html>", jaccardIdx, jaccardIdx);
        current.getComparePanel().addPropTableRow("<html><b>Współczynnik Dice’a</b></html>", sorenDiceCoef, sorenDiceCoef);
        current.getComparePanel().addPropTableRow("<html><b>MSE</b></html>", mse, mse);
        current.getComparePanel().addPropTableRow("<html><b>RMSE</b></html>", rmse, rmse);
        current.getComparePanel().addPropTableRow(
                "<html><b>Liczba iteracji</b></html>",
                ownStats.get(AKMeans.KMeansStats.ITERATIONS),
                wekaStats.get(AKMeans.KMeansStats.ITERATIONS));
        current.getComparePanel().addPropTableRow(
                "<html><b>Czas trwania</b></html>",
                ownStats.get(AKMeans.KMeansStats.TIME),
                wekaStats.get(AKMeans.KMeansStats.TIME));
        current.getComparePanel().fillClustersTable(
                (ArrayList<Cluster>) ownStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS),
                (ArrayList<Cluster>)wekaStats.get(AKMeans.KMeansStats.CLUSTER_CENTROIDS)
        );
        current.getComparePanel().fillInitialsTable(
                (ArrayList<Point3D>) ownStats.get(AKMeans.KMeansStats.INITIAL_START_POINTS),
                (ArrayList<Point3D>) wekaStats.get(AKMeans.KMeansStats.INITIAL_START_POINTS)
        );
        current.getComparePanel().fillClustersMetricsTable(jaccardIdxs, sorenDiceCoefs);
    }
}
