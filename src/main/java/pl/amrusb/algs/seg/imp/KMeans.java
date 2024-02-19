package pl.amrusb.algs.seg.imp;

import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.img.ImageSaver;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class KMeans extends AKMeans {

    public KMeans(int k, BufferedImage image){
        super(null,k,ImageReader.getPixelArray(image),image.getWidth(), image.getHeight());
    }

    public void execute(){
        KMeansPP init = new KMeansPP(getClusterNum(), getPixelArray());
        ArrayList<Cluster> clusters = init.execute();
        setClusterNum(clusters.size());

        HamerlySegmentation alg = new HamerlySegmentation(
                getClusterNum(),
                getPixelArray(),
                clusters);
        setPixelArray(alg.execute());
    }
}
