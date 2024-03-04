package pl.amrusb.algs.seg.imp;

import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.util.ClusterComparator;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.timer.Timer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;


public class KMeans extends AKMeans {

    public KMeans(int k, BufferedImage image){
        super(null,k,ImageReader.getPixelArray(image),image.getWidth(), image.getHeight());
    }

    public void execute(){
        try {
            Timer timer = new Timer();
            timer.start();

            Map<KMeansStats, Object> stats = new HashMap<>();
            KMeansPP init = new KMeansPP(getClusterNum(), getPixelArray());
            ArrayList<Cluster> clusters = init.execute();
            setClusterNum(clusters.size());
            ArrayList<Cluster> initialClusters = new ArrayList<>(clusters.size());
            clusters.forEach(e -> {
                initialClusters.add(e.clone());
            });

            stats.put(KMeansStats.INITIAL_START_POINTS, initialClusters);

            HamerlySegmentation alg = new HamerlySegmentation(
                    getClusterNum(),
                    getPixelArray(),
                    clusters);
            setPixelArray(alg.execute());

            Float time = timer.stop();

            clusters.sort(new ClusterComparator());
            int[] assignments = reassignment(alg.getAssignments(), clusters);

            stats.put(KMeansStats.TIME, time);
            stats.put(KMeansStats.CLUSTER_CENTROIDS, clusters);
            stats.put(KMeansStats.ASSIGNMENTS, assignments);
            stats.put(KMeansStats.ITERATIONS, alg.getIteration());

            this.setStatistics(stats);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
