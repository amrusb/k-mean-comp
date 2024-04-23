package pl.amrusb.algs.seg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.amrusb.util.constants.KMeansStats;
import pl.amrusb.util.img.ImageSaver;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public abstract class AKMeans implements IKMeans{
    private final int maxIter;
    private Integer clusterNum;
    @Getter
    private Map<KMeansStats, Object> statistics;
    private ArrayList<Pixel> pixelArray;

    private final int width;
    private final int height;

    /**
     * Zwraca obraz wyjściowy po segmentowaniu obrazu algorytmem k-means.
     * @return obraz wyjściowy po segmentacji
     */
    public BufferedImage getOutputImage(){
        return ImageSaver.convertToBufferedImage(getPixelArray(), width, height);
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
