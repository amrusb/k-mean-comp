package pl.amrusb.algs.seg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.amrusb.util.img.ImageSaver;
import pl.amrusb.util.models.Pixel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public abstract class AKMeans implements IKMeans{
    @Getter
    private Map<KMeansStats, Object> statistics;
    private Integer clusterNum;
    private ArrayList<Pixel> pixelArray;

    private final int width;
    private final int height;

    public enum KMeansStats{
        INITIAL_START_POINTS,
        CLUSTER_CENTROIDS,
        ITERATIONS,
        TIME,
        ASSIGNMENTS
    }

    /**
     * Zwraca obraz wyjściowy po segmentowaniu obrazu algorytmem k-means.
     * @return obraz wyjściowy po segmentacji
     */
    public BufferedImage getOutputImage(){
        return ImageSaver.convertToBufferedImage(getPixelArray(), width, height);
    }
}
