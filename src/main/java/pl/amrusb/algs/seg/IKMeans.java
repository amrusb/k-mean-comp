package pl.amrusb.algs.seg;

import pl.amrusb.util.constants.KMeansStats;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface IKMeans {
    void execute();
    BufferedImage getOutputImage();
    Map<KMeansStats, Object> getStatistics();
}
