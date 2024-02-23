package pl.amrusb.algs.seg;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface IKMeans {
    void execute();
    BufferedImage getOutputImage();
    Map<AKMeans.KMeansStats, Object> getStatistics();
}
