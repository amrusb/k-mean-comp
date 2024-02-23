package pl.amrusb.algs.seg.imp;

import lombok.Getter;
import pl.amrusb.util.Calculations;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.ui.panels.BottomPanel;

import java.util.ArrayList;
import java.util.Arrays;

class HamerlySegmentation {
    private static final int MAX_ITERATIONS = 100;
    private final Integer clusterNum;
    @Getter
    private final ArrayList<Cluster> clusters;
    private final ArrayList<Pixel> image;
    private final Integer size;
    private final double[] upperBounds;
    private final double[] lowerBounds;
    private final double[] moves;
    @Getter
    private final int[] assignments;
    @Getter
    private Integer iteration;

    public HamerlySegmentation(
            Integer clusterNum,
            ArrayList<Pixel> image,
            ArrayList<Cluster> clusters
    ){
        this.clusterNum = clusterNum;
        this.image = image;
        this.clusters = clusters;

        size = image.size();
        upperBounds = new double[size];
        lowerBounds = new double[size];
        moves = new double[clusterNum];
        assignments = new int[size];
    }

    public ArrayList<Pixel> execute(){
        Arrays.fill(upperBounds, Double.MAX_VALUE);
        Arrays.fill(lowerBounds, 0.0);
        Arrays.fill(assignments, -1);

        BottomPanel.setProgress(0);
        BottomPanel.setProgressMaximum(MAX_ITERATIONS - 1);
        BottomPanel.setProgressLabel("K-means...");
        for (iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            BottomPanel.incrementProgress();
            for (int index = 0; index < size; index++) {

                var pixel = image.get(index);
                if(iteration != 0){
                    upperBounds[index] += moves[assignments[index]];
                    lowerBounds[index] -= Arrays.stream(moves).max().getAsDouble();
                    lowerBounds[index] = Double.max(lowerBounds[index], 0.0);
                }
                if (upperBounds[index] > lowerBounds[index]) {
                    double minDistance = Double.MAX_VALUE;
                    double secondMinDistance = Double.MAX_VALUE;
                    int minIndex = 0;
                    for (int i = 0; i < clusterNum; i++) {
                        double distance = Calculations.calculateDistance(clusters.get(i), pixel);
                        if (distance < minDistance) {
                            secondMinDistance = minDistance;
                            minDistance = distance;
                            minIndex = i;
                        } else if (distance < secondMinDistance) {
                            secondMinDistance = distance;
                        }
                    }

                    upperBounds[index] = minDistance;
                    lowerBounds[index] = secondMinDistance;

                    if (assignments[index] != -1)
                        clusters.get(assignments[index]).decreaseSize();

                    assignments[index] = minIndex;
                    clusters.get(assignments[index]).increaseSize();

                }
            }

            int notChanged = 0;

            int[] R = new int[clusterNum];
            int[] G = new int[clusterNum];
            int[] B = new int[clusterNum];

            int index = 0;
            for (Pixel pixel : image) {
                R[assignments[index]] += pixel.getR();
                G[assignments[index]] += pixel.getG();
                B[assignments[index]] += pixel.getB();
                index++;
            }

            for (int i = 0; i < clusterNum; i++) {
                int cSize = clusters.get(i).getSize();

                if (cSize == 0)
                    continue;

                int new_x = R[i] / cSize;
                int new_y = G[i] / cSize;
                int new_z = B[i] / cSize;

                if (new_x != clusters.get(i).getX() || new_y != clusters.get(i).getY() || new_z != clusters.get(i).getZ()) {
                    int d_x = new_x - clusters.get(i).getX();
                    int d_y = new_y - clusters.get(i).getY();
                    int d_z = new_z - clusters.get(i).getZ();

                    double move = Math.sqrt(d_x * d_x + d_y * d_y + d_z * d_z);
                    moves[i] = move;

                    clusters.get(i).setX(new_x);
                    clusters.get(i).setY(new_y);
                    clusters.get(i).setZ(new_z);
                } else notChanged++;
            }

            if (notChanged == clusterNum)
                break;

        }
        BottomPanel.setProgress(MAX_ITERATIONS * size - 1);

        setPixelToClusterVal();

        return image;
    }

    private void setPixelToClusterVal(){
        int index = 0;
        for (Pixel pixel : image) {
            int clusterNumber = assignments[index];
            int R = clusters.get(clusterNumber).getX();
            int G = clusters.get(clusterNumber).getY();
            int B = clusters.get(clusterNumber).getZ();
            pixel.setPixelValue(R, G, B);
            index++;
        }
    }
}
