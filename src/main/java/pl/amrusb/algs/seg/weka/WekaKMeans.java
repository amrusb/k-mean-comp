package pl.amrusb.algs.seg.weka;

import pl.amrusb.algs.seg.IKMeans;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.img.ImageSaver;
import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.ui.panels.BottomPanel;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;

public class WekaKMeans implements IKMeans {
    private static final int MAX_ITERATIONS = 100;
    private static final String PREFIX = "pixel_data";
    private static final String SUFFIX = ".arff";
    private final ArrayList<Pixel> pixelArray;
    private final int width;
    private final int height;

    private final Integer clusterNum;
    private Instances data;

    public  WekaKMeans(int k, BufferedImage image){
        clusterNum = k;
        pixelArray = ImageReader.getPixelArray(image);
        width = image.getWidth();
        height = image.getHeight();
    }

    public void execute() {
        try {
            data = createDataSet();
            SimpleKMeans algorithm = new SimpleKMeans();
            algorithm.setMaxIterations(MAX_ITERATIONS);
            algorithm.setSeed(1232);
            algorithm.setPreserveInstancesOrder(true);
            algorithm.setNumClusters(clusterNum);

            BottomPanel.setProgress(0);
            BottomPanel.setProgressMaximum(1);
            BottomPanel.setProgressLabel("K-means...");
            algorithm.buildClusterer(data);
            BottomPanel.setProgress(1);

            algorithm.getOptions();

            setPixelToClusterVal(algorithm.getAssignments(), algorithm.getClusterCentroids());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }

    }
    private void setPixelToClusterVal(int[] assignments, Instances centroids){
        int index = 0;
        for(Pixel pixel: pixelArray){
            BottomPanel.incrementProgress();
            int k = assignments[index];
            int R, G, B;

            Instance centroid = centroids.get(k);
            double[] val = centroid.toDoubleArray();

            R = Double.valueOf(val[0]).intValue();
            G = Double.valueOf(val[1]).intValue();
            B = Double.valueOf(val[2]).intValue();

            pixel.setPixelValue(R,G,B);
            index++;
        }
    }

    private Instances createDataSet() throws Exception {
        BottomPanel.setProgress(0);
        BottomPanel.setProgressMaximum(pixelArray.size());
        BottomPanel.setProgressLabel("Tworzenie zbioru danych...");
        BufferedReader dataSource;
        File tempDataFile = File.createTempFile(PREFIX, SUFFIX);
        FileWriter writer = new FileWriter(tempDataFile);

        writer.write("@RELATION image\n");
        writer.write("@ATTRIBUTE r NUMERIC\n");
        writer.write("@ATTRIBUTE g NUMERIC\n");
        writer.write("@ATTRIBUTE b NUMERIC\n");
        writer.write("@DATA\n");

        for (Pixel pixel: pixelArray) {
            BottomPanel.incrementProgress();
            String line = "{0},{1},{2}\n";

            writer.write(
                    MessageFormat.format(
                            line,
                            pixel.getR(),
                            pixel.getG(),
                            pixel.getB())
            );
        }

        writer.close();
        dataSource = new BufferedReader(new FileReader(tempDataFile));

        return new Instances(dataSource);
    }
    public BufferedImage getOutputImage(){
        return ImageSaver.convertToBufferedImage(pixelArray, width, height);
    }
}
