package pl.amrusb.algs.seg.imp;

import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.img.ImageReader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class KMeans extends AKMeans {

    public KMeans(int k, BufferedImage image){
        super(null,k,ImageReader.getPixelArray(image),image.getWidth(), image.getHeight());
    }

    public void execute(){
        Map<KMeansStats, Object> stats = new HashMap<>();
        KMeansPP init = new KMeansPP(getClusterNum(), getPixelArray());
        ArrayList<Cluster> clusters = init.execute();
        setClusterNum(clusters.size());
        ArrayList<Cluster> initialClusters = new ArrayList<>(clusters.size());
        clusters.forEach(e ->{
                initialClusters.add( e.clone());
        });

        stats.put(KMeansStats.INITIAL_START_POINTS, initialClusters);

        HamerlySegmentation alg = new HamerlySegmentation(
                getClusterNum(),
                getPixelArray(),
                clusters);
        setPixelArray(alg.execute());

        stats.put(KMeansStats.CLUSTER_CENTROIDS, clusters);
        stats.put(KMeansStats.ASSIGNMENTS, alg.getAssignments());
        stats.put(KMeansStats.ITERATIONS, alg.getIteration());

        this.setStatistics(stats);
    }
}
