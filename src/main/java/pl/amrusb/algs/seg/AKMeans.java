package pl.amrusb.algs.seg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.amrusb.util.Statistics;
import pl.amrusb.util.img.ImageSaver;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public abstract class AKMeans implements IKMeans{
    private final int maxIter;
    private Integer clusterNum;
    @Getter
    private Statistics statistics;
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
}
