package pl.amrusb.algs.seg.imp;

import lombok.Getter;
import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.util.math.Calculations;
import pl.amrusb.util.models.Statistics;
import pl.amrusb.util.constants.KMeansStats;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pair;
import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.timer.Timer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class AdaptiveKMeans extends AKMeans {
    private final Integer size;
    @Getter
    private ArrayList<Cluster> clusters;
    @Getter
    private final int[] assignments;
    @Getter
    private Integer iteration;
    private double[][] cDistMtx;
    private ArrayList<Pair<Double, Integer>> cMinTbl;

    public AdaptiveKMeans(int k, int maxIter, BufferedImage image){
        super(maxIter,k, null, ImageReader.getPixelArray(image),image.getWidth(), image.getHeight());
        size = super.getPixelArray().size();
        assignments = new int[size];

        Arrays.fill(assignments, -1);
    }

    @Override
    public void execute() {
        try {
            Timer timer = new Timer();
            timer.start();

            Statistics stats = new Statistics();
            KMeansPP init = new KMeansPP(getClusterNum(), getPixelArray());
            clusters = init.execute();
            setClusterNum(clusters.size());
            ArrayList<Cluster> initialClusters = new ArrayList<>(clusters.size());
            clusters.forEach(e -> {
                initialClusters.add(e.clone());
            });

            stats.put(KMeansStats.INITIAL_START_POINTS, initialClusters);

            cDistMtx = getDistanceMatrix(clusters);
            cMinTbl = getMinDistanceTable(cDistMtx);

            segmentation();

            Float time = timer.stop();

            stats.put(KMeansStats.TIME, time);
            stats.put(KMeansStats.CLUSTER_CENTROIDS, clusters);
            stats.put(KMeansStats.ASSIGNMENTS, assignments);
            stats.put(KMeansStats.ITERATIONS, iteration);

            super.setStatistics(stats);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    private double[][] getDistanceMatrix(ArrayList<Cluster> clusters){
        double[][] result = new double[clusters.size()][clusters.size()];

        for (int i = 0; i < clusters.size(); i++) {
            Cluster c1 = clusters.get(i);
            for (int j = i+ 1; j < clusters.size(); j++) {
                Cluster c2 = clusters.get(j);
                double distance = Calculations.calculateDistance(c1, c2);
                result[i][j] = distance;
                result[j][i] = distance;
            }
        }

        return result;
    }

    private ArrayList<Pair<Double, Integer>> getMinDistanceTable(double[][] distances){
        ArrayList<Pair<Double,Integer>> minTable = new ArrayList<>();
        for (int i = 0; i < distances.length; i++) {
            double cMin = Double.MAX_VALUE;
            int index = 0;
            for (int j = 0; j < distances.length; j++) {
                if(i == j)
                    continue;
                if(cMin > distances[i][j]){
                    cMin = distances[i][j];
                    index = j;
                }
            }

            minTable.add(new Pair<>(cMin, index));
        }

        return minTable;
    }

    private void segmentation(){
        for(iteration = 0; iteration < super.getMaxIter(); iteration++) {
            boolean flag = false;
            for (int index = 0; index < size; index++) {
                flag = false;
                Pixel pixel = super.getPixelArray().get(index);
                double minDistance = Calculations.calculateDistance(clusters.get(0), pixel);
                int minIndex = 0;

                for (int i = 1; i < super.getClusterNum(); i++) {
                    double distance = Calculations.calculateDistance(clusters.get(i), pixel);
                    if (distance < minDistance) {
                        minDistance = distance;
                        minIndex = i;
                    }
                }

                if (minDistance == 0) {
                    if (assignments[index] != -1) {
                        clusters.get(assignments[index]).decreaseSize();
                    }
                    assignments[index] = minIndex;
                    clusters.get(assignments[index]).increaseSize();
                } else if (minDistance <= cMinTbl.get(minIndex).getFirst()) {
                    if (assignments[index] != -1) {
                        clusters.get(assignments[index]).decreaseSize();
                    }
                    assignments[index] = minIndex;
                    clusters.get(assignments[index]).increaseSize();
                } else if (minDistance > cMinTbl.get(minIndex).getFirst()) {
                    flag = true;
                    if (assignments[index] != -1) {
                        clusters.get(assignments[index]).decreaseSize();
                    }
                    int closestCIdx = cMinTbl.get(minIndex).getSecond();

                    merge(minIndex, closestCIdx, pixel);

                    assignments[index] = closestCIdx;
                    clusters.get(closestCIdx).increaseSize();

                    recomputeCentroid(minIndex);

                    cDistMtx = getDistanceMatrix(clusters);
                    cMinTbl = getMinDistanceTable(cDistMtx);
                }
            }

            if(recomputeCentroid() && !flag){
                break;
            }
        }

        setPixelToClusterVal();
    }

    private boolean recomputeCentroid(){
        int notChanged = 0;

        int[] R = new int[super.getClusterNum()];
        int[] G = new int[super.getClusterNum()];
        int[] B = new int[super.getClusterNum()];

        int index = 0;
        for (Pixel pixel : super.getPixelArray()) {
            R[assignments[index]] += pixel.getR();
            G[assignments[index]] += pixel.getG();
            B[assignments[index]] += pixel.getB();
            index++;
        }

        for (int i = 0; i < super.getClusterNum(); i++) {
            int cSize = clusters.get(i).getSize();

            if (cSize == 0)
                continue;

            int new_x = R[i] / cSize;
            int new_y = G[i] / cSize;
            int new_z = B[i] / cSize;

            if (new_x != clusters.get(i).getX() || new_y != clusters.get(i).getY() || new_z != clusters.get(i).getZ()) {
                clusters.get(i).setX(new_x);
                clusters.get(i).setY(new_y);
                clusters.get(i).setZ(new_z);
            } else notChanged++;
        }

        if (notChanged == super.getClusterNum())
            return true;
        else {
            cDistMtx = getDistanceMatrix(clusters);
            cMinTbl = getMinDistanceTable(cDistMtx);
            return false;
        }
    }

    private void recomputeCentroid(int cIndex){
        int R = 0;
        int G = 0;
        int B = 0;
        int cSize = clusters.get(cIndex).getSize();

        if(cSize != 0){
            int index = 0;
            for (Pixel pixel : super.getPixelArray()) {
                if(assignments[index] == cIndex){
                    R += pixel.getR();
                    G += pixel.getG();
                    B += pixel.getB();
                }
                index++;
            }
            clusters.get(cIndex).setX(R / cSize);
            clusters.get(cIndex).setY(G / cSize);
            clusters.get(cIndex).setZ(B / cSize);

            cDistMtx = getDistanceMatrix(clusters);
            cMinTbl = getMinDistanceTable(cDistMtx);
        }
    }
    /**
     * Metoda łączy dwa klastry jeden
     * @param c1 klaster docelowy
     * @param c2 klaster do połączenia
     */
    private void merge(Integer c1, Integer c2, Pixel p){
        if(clusters.get(c2).getSize() != 0){
            for (int i = 0; i < size; i++) {
                if(assignments[i] == c2){
                    assignments[i] = c1;
                    clusters.get(c1).increaseSize();
                }
            }
        }
        int ordinal = clusters.get(c2).getOrdinal();
        clusters.set(c2, new Cluster(p.getR(), p.getG(), p.getB(), 0, ordinal));
    }

    private void setPixelToClusterVal(){
        int index = 0;
        for (Pixel pixel : super.getPixelArray()) {
            int clusterNumber = assignments[index];
            int R = clusters.get(clusterNumber).getX();
            int G = clusters.get(clusterNumber).getY();
            int B = clusters.get(clusterNumber).getZ();
            pixel.setPixelValue(R, G, B);
            index++;
        }
    }
}
