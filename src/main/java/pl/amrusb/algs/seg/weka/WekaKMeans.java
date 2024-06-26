package pl.amrusb.algs.seg.weka;

import pl.amrusb.algs.seg.AKMeans;
import pl.amrusb.util.models.Statistics;
import pl.amrusb.util.constants.KMeansStats;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.models.Point3D;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import pl.amrusb.util.timer.Timer;

public class WekaKMeans extends AKMeans {
    private static final String PREFIX = "pixel_data";
    private static final String SUFFIX = ".arff";
    private final Timer timer;

    public  WekaKMeans(int k, int maxIter, BufferedImage image){
        super(maxIter,k,null, ImageReader.getPixelArray(image),image.getWidth(), image.getHeight());
        timer = new Timer();
    }

    @Override
    public void execute() {
        try {
            Instances data = createDataSet();
            SimpleKMeans algorithm = new SimpleKMeans();
            algorithm.setInitializationMethod(
                    new SelectedTag(
                            SimpleKMeans.KMEANS_PLUS_PLUS,
                            SimpleKMeans.TAGS_SELECTION
                    )
            );
            algorithm.setDebug(false);
            algorithm.setMaxIterations(super.getMaxIter());
            algorithm.setPreserveInstancesOrder(true);
            algorithm.setNumClusters(getClusterNum());

            timer.start();

            algorithm.buildClusterer(data);

            timer.stop();

            createStats(algorithm);

            setPixelToClusterVal(algorithm.getAssignments(), algorithm.getClusterCentroids());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    private void createStats(SimpleKMeans algorithm){
        try{
            Statistics stats = new Statistics();
            ArrayList<Cluster> clusters = new ArrayList<>();
            Instances centroids = algorithm.getClusterCentroids();
            double[] sizes = algorithm.getClusterSizes();
            Integer iterations = null;
            ArrayList<Point3D> initialClusters = new ArrayList<>();

            for (int i = 0; i < centroids.size(); i++) {
                Instance centroid = centroids.get(i);
                double[] val = centroid.toDoubleArray();
                Cluster cluster = new Cluster(
                        (int) val[0],
                        (int) val[1],
                        (int) val[2],
                        (int) sizes[i],
                        i
                );

                clusters.add(cluster);
            }


            String[] info = algorithm.toString().split("\n");

            for (String line : info) {
                if (line.contains("Number of iterations")) {
                    String[] parts = line.split(":");
                    iterations = Integer.parseInt(parts[1].trim());
                } else if (line.startsWith("Cluster")) {
                    String[] clusterParts = line.split(":");
                    String[] clusterCoordinates = clusterParts[1].split(",");
                    int x = Integer.parseInt(clusterCoordinates[0].trim());
                    int y = Integer.parseInt(clusterCoordinates[1].trim());
                    int z = Integer.parseInt(clusterCoordinates[2].trim());

                    Point3D cluster = new Point3D(x,y,z);
                    initialClusters.add(cluster);
                }
            }

            stats.put(KMeansStats.TIME, timer.getResult());
            stats.put(KMeansStats.INITIAL_START_POINTS, initialClusters);
            stats.put(KMeansStats.ITERATIONS, iterations);
            stats.put(KMeansStats.CLUSTER_CENTROIDS, clusters);
            stats.put(KMeansStats.ASSIGNMENTS, algorithm.getAssignments());

            this.setStatistics(stats);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void setPixelToClusterVal(int[] assignments, Instances centroids){
        int index = 0;
        for(Pixel pixel: getPixelArray()){
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
        BufferedReader dataSource;
        File tempDataFile = File.createTempFile(PREFIX, SUFFIX);
        FileWriter writer = new FileWriter(tempDataFile);

        writer.write("@RELATION image\n");
        writer.write("@ATTRIBUTE r NUMERIC\n");
        writer.write("@ATTRIBUTE g NUMERIC\n");
        writer.write("@ATTRIBUTE b NUMERIC\n");
        writer.write("@DATA\n");

        for (Pixel pixel: getPixelArray()) {
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
}
