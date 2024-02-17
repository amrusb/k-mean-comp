package pl.amrusb.algs.seg.imp;

import pl.amrusb.algs.rand.LCGenerator;
import pl.amrusb.util.Calculations;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.ui.BottomPanel;

import java.util.ArrayList;
import java.util.Arrays;

class KMeansPP {
    private Integer clusterNum;
    private final ArrayList<Pixel> image;
    private final ArrayList<Cluster> clusters;

    public KMeansPP(Integer clusterNum, ArrayList<Pixel> image){
        this.image = image;
        this.clusterNum = clusterNum;

        clusters = new ArrayList<>(clusterNum);
    }

    public ArrayList<Cluster> execute(){
        int pixelArraySize = image.size();
        LCGenerator random = new LCGenerator(System.nanoTime());
        int x = (int)(random.nextDouble() * 255);
        var temp_pixel = image.get(x);

        //Pierwszy centroid randomowy
        clusters.add(new Cluster(temp_pixel));


        double[] distances = new double[pixelArraySize];
        Arrays.fill(distances, Double.MAX_VALUE);

        BottomPanel.setProgressMaximum(clusterNum - 1);
        BottomPanel.setProgressLabel("Inicjalizajca klastrów...");
        main_for:
        for (int i = 1; i < clusterNum; i++) {
            BottomPanel.incrementProgress();
            double sum = 0.0;

            for (int j = 0; j < pixelArraySize; j++) {
                temp_pixel = image.get(j);

                double distance = Calculations.calculateDistanceSquared(clusters.get(i-1), temp_pixel);

                distances[j] = Math.min(distances[j], distance);
                sum+= distances[j];
            }

            double d = (random.nextDouble() * sum);
            double pxl_value_sum = 0.0;
            int cluster_index = -1;

            while(pxl_value_sum <= d){
                if(++cluster_index >= pixelArraySize) {
                    clusterNum = i-1;
                    break main_for;
                }
                pxl_value_sum+=distances[cluster_index];

            }
            temp_pixel = image.get(cluster_index);
            clusters.add(new Cluster(temp_pixel));
        }

        return clusters;
    }
}
