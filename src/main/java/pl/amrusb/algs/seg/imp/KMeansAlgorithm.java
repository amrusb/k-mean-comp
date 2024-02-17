package pl.amrusb.algs.seg.imp;

import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.img.ImageSaver;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class KMeansAlgorithm {
    private Integer clusterNum;
    private ArrayList<Cluster> clusters;
    private ArrayList<Pixel> pixelArray;

    private final int width;
    private final int height;
    public KMeansAlgorithm(int k, BufferedImage image){
        clusterNum = k;
        this.pixelArray = ImageReader.getPixelArray(image);
        width = image.getWidth();
        height = image.getHeight();
    }

    public void execute(){
        KMeansPP init = new KMeansPP(clusterNum, pixelArray);
        clusters = init.execute();
        clusterNum = clusters.size();

        HamerlySegmentation alg = new HamerlySegmentation(clusterNum, pixelArray, clusters);
        pixelArray = alg.execute();
    }

    /**
     * Zwraca obraz wyjściowy po segmentowaniu obrazu algorytmem k-means.
     * @return obraz wyjściowy po segmentacji
     */
    public BufferedImage getOutputImage(){
        return ImageSaver.convertToBufferedImage(pixelArray, width, height);
    }
}
