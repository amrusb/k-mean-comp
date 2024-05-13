package pl.amrusb.algs.seg;

import pl.amrusb.util.models.Statistics;

import java.awt.image.BufferedImage;

public interface IKMeans {
    void execute();
    BufferedImage getOutputImage();
    Statistics getStatistics();
}
