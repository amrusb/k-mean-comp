package pl.amrusb.segm.comp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.JFreeChart;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.constants.MetricsTypes;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Point3D;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.util.ui.panels.CChartPanel;
import pl.amrusb.util.ui.panels.ImageViewPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CompareWindow extends JPanel {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JPanel bottomPanel;

    private ImageViewPanel ivpImp;
    private ImageViewPanel ivpAdapt;
    private ImageViewPanel ivpWeka;

    private JPanel chartsPanel;
    private CardLayout chartsCardLayout;
    private ImageViewPanel ivpOriginal;
    private CChartPanel histogramPanel;
    private CChartPanel sizesChartPanel;
    private CChartPanel chpSilhouette;
    private CChartPanel chpMetrics;
    private CChartPanel chpClustersSilhouette;
    private CChartPanel chpClustersJaccard;
    private CChartPanel chpClustersDice;


    private JPanel statsPanel;
    private CardLayout statsCardLayout;
    private MetricsPanel pMetrics;
    private PropertiesPanel pProperties;
    private InitialCentroidPanel pInitCentroid;
    private ClusterPanel pClusters;
    private ClusterMetricsPanel pClustersMetrics;

    public CompareWindow(){
        setLayout(new BorderLayout());

        createComponents();
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(1,3));
        imagePanel.setPreferredSize(
                new Dimension(
                        MainFrame.getFrameWidth(),
                        MainFrame.getFrameHeight() * 2 / 3
                )
        );

        imagePanel.add(ivpImp);
        imagePanel.add(ivpAdapt);
        imagePanel.add(ivpWeka);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, imagePanel, bottomPanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerSize(5);

        this.add(splitPane, BorderLayout.CENTER);
    }

    private void createComponents(){
        ivpImp = new ImageViewPanel(AlgorithmsMetrics.IMP);
        ivpAdapt = new ImageViewPanel(AlgorithmsMetrics.ADAPT);
        ivpWeka = new ImageViewPanel(AlgorithmsMetrics.WEKA);
        createBottomPanel();
    }

    private void createBottomPanel(){
        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(
                new Dimension(
                        MainFrame.getFrameWidth(),
                        MainFrame.getFrameHeight() / 3
                )
        );
        bottomPanel.setLayout(new GridLayout(1,2));
        createLeftBottomPanel();

        JPanel cbRow = new JPanel();
        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setLayout(new BorderLayout());

        cbRow.add(new JLabel("Pokaż statystyki:"));

        JComboBox<String> comboBox =  StatsComboBox.getJComboBox();
        comboBox.addItemListener(e-> {
            statsCardLayout.show(statsPanel, e.getItem().toString());
            chartsCardLayout.show(chartsPanel, e.getItem().toString());
        });
        cbRow.add(comboBox);
        rightBottomPanel.add(cbRow, BorderLayout.NORTH);

        createStatsPanel();
        rightBottomPanel.add(statsPanel, BorderLayout.CENTER);


        bottomPanel.add(chartsPanel);
        bottomPanel.add(rightBottomPanel);
    }
    public void setOriginalImage(BufferedImage image){
        double frameWidth = MainFrame.getFrameWidth() / 4.0;
        double frameHeight = MainFrame.getFrameHeight() * 0.3;

        int width = image.getWidth();
        int height = image.getHeight();
        String htmlString = "<html><img src=\"file:%s\" width=\"%s\" height=\"%s\"></html>";

        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min(frameWidth / (width), frameHeight / (height));
            width = (int)(width * scale);
            height = (int)(height * scale);
        }

        File stream;
        try {
            stream = File.createTempFile("show-img", ".jpg");
            ImageIO.write(image, "jpg", stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        htmlString = String.format(htmlString, stream , width, height);

        ivpOriginal.setImage(htmlString);
    }
    private void createLeftBottomPanel(){
        chartsCardLayout = new CardLayout();
        chartsPanel = new JPanel();
        chartsPanel.setLayout(chartsCardLayout);

        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new GridLayout(0,2));
        histogramPanel = new CChartPanel();
        ivpOriginal = new ImageViewPanel("Oryginalny");

        propertiesPanel.add(histogramPanel);
        propertiesPanel.add(ivpOriginal);
        JPanel clustersPanel = new JPanel();
        clustersPanel.setLayout(new BorderLayout());

        sizesChartPanel = new CChartPanel();

        clustersPanel.add(sizesChartPanel, BorderLayout.CENTER);

        JPanel metricsPanel = new JPanel();
        metricsPanel.setLayout(new GridLayout(0,2));

        chpSilhouette = new CChartPanel();
        chpMetrics = new CChartPanel();
        metricsPanel.add(chpSilhouette);
        metricsPanel.add(chpMetrics);

        JPanel clusterMetricsPanel = new JPanel();
        clusterMetricsPanel.setLayout(new GridLayout(0,3));

        chpClustersSilhouette = new CChartPanel();
        chpClustersJaccard = new CChartPanel();
        chpClustersDice = new CChartPanel();

        clusterMetricsPanel.add(chpClustersSilhouette);
        clusterMetricsPanel.add(chpClustersJaccard);
        clusterMetricsPanel.add(chpClustersDice);

        chartsPanel.add(propertiesPanel, StatsComboBox.PROPERTIES.value);
        chartsPanel.add(clustersPanel, StatsComboBox.CLUSTERS.value);
        chartsPanel.add(metricsPanel, StatsComboBox.METRICS.value);
        chartsPanel.add(clusterMetricsPanel, StatsComboBox.CLUSTER_METRICS.value);


    }
    private void createStatsPanel(){
        statsCardLayout = new CardLayout();
        statsPanel = new JPanel();
        statsPanel.setLayout(statsCardLayout);

        pMetrics = new MetricsPanel();
        pProperties = new PropertiesPanel();
        pClustersMetrics = new ClusterMetricsPanel();
        pClusters = new ClusterPanel();
        pInitCentroid = new InitialCentroidPanel();

        statsPanel.add(pProperties, StatsComboBox.PROPERTIES.value);
        statsPanel.add(new JScrollPane(pClusters), StatsComboBox.CLUSTERS.value);
        statsPanel.add(pMetrics, StatsComboBox.METRICS.value);
        statsPanel.add(new JScrollPane(pClustersMetrics), StatsComboBox.CLUSTER_METRICS.value);
        statsPanel.add(new JScrollPane(pInitCentroid), StatsComboBox.INITIAL.value);

    }
    public void fillInitialsTable(ArrayList<Point3D> dataSet1, ArrayList<Point3D> dataSet2, ArrayList<Point3D> dataSet3){
        pInitCentroid.fillTables(dataSet1,dataSet2,dataSet3);
    }
    public void fillClustersTable(ArrayList<Cluster> dataSet1, ArrayList<Cluster> dataSet2, ArrayList<Cluster> dataSet3){
        pClusters.fillTables(dataSet1,dataSet2,dataSet3);
    }
    public void fillClustersMetricTable(Map<MetricsTypes, Object> metrics){
        Map<AlgorithmsMetrics, ArrayList<Double>> JaccardMetrics = (Map<AlgorithmsMetrics, ArrayList<Double>>) metrics.get(MetricsTypes.JACCARD);
        Map<AlgorithmsMetrics, ArrayList<Double>> DiceMetrics = (Map<AlgorithmsMetrics, ArrayList<Double>>) metrics.get(MetricsTypes.DICE);
        Map<AlgorithmsMetrics, ArrayList<Double>> SihlouetteMetrics = (Map<AlgorithmsMetrics, ArrayList<Double>>) metrics.get(MetricsTypes.SIHLOUETTE);

        pClustersMetrics.fillTables(JaccardMetrics,DiceMetrics,SihlouetteMetrics);
    }
    public void setPropertiesValues(
            String fileName,
            Integer pixelCount,
            Integer clusterCount,
            Integer maxIterations,
            Integer impIterCount,
            Integer adaptIterCount,
            Integer wekaIterCount,
            Float impTime,
            Float adaptTime,
            Float wekaTime
    ){
        pProperties.setValues(fileName,pixelCount,clusterCount,maxIterations,impIterCount,adaptIterCount,wekaIterCount,impTime,adaptTime,wekaTime);
    }

    public void setJaccardValues(ArrayList<Double> jaccardIndex){
        pMetrics.setJaccardValues(jaccardIndex.get(0),jaccardIndex.get(1),jaccardIndex.get(2));
    }
    public void setDiceValues(ArrayList<Double> diceScore){
        pMetrics.setDiceValues(diceScore.get(0),diceScore.get(1),diceScore.get(2));
    }
    public void setSilhouetteValues(ArrayList<Double>  silhouette){
        pMetrics.setSilhouetteValues(silhouette.get(0),silhouette.get(1),silhouette.get(2));
    }
    public void setImageLabel(BufferedImage image, AlgorithmsMetrics position){
        double frameWidth = MainFrame.getFrameWidth() / 3.0;
        double frameHeight = MainFrame.getFrameHeight() * 2.0 / 3;

        int width = image.getWidth();
        int height = image.getHeight();
        String htmlString = "<html><img src=\"file:%s\" width=\"%s\" height=\"%s\"></html>";

        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min(frameWidth / (width), frameHeight / (height));
            width = (int)(width * scale);
            height = (int)(height * scale);
        }

        File stream;
        try {
            stream = File.createTempFile("show-img", ".jpg");
            ImageIO.write(image, "jpg", stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        htmlString = String.format(htmlString, stream , width, height);

        switch(position){
            case IMP -> {
                ivpImp.setImage(image);
                ivpImp.setImage(htmlString);
            }
            case ADAPT -> {
                ivpAdapt.setImage(image);
                ivpAdapt.setImage(htmlString);
            }
            case WEKA -> {
                ivpWeka.setImage(image);
                ivpWeka.setImage(htmlString);
            }
        }
    }

    public void setHistogram(JFreeChart chart){
        histogramPanel.setChart(chart);
    }

    public void setSizesChart(JFreeChart chart){
        sizesChartPanel.setChart(chart);
    }

    public void setChMetrics(JFreeChart chart){
        chpMetrics.setChart(chart);
    }

    public void setChSilhouette(JFreeChart chart){
        chpSilhouette.setChart(chart);
    }
    public void setChClustersMetrics(JFreeChart chart1, JFreeChart chart2, JFreeChart chart3){
        chpClustersSilhouette.setChart(chart1);
        chpClustersJaccard.setChart(chart2);
        chpClustersDice.setChart(chart3);
    }

    public Map<AlgorithmsMetrics, BufferedImage> getOutputImages(){
        Map<AlgorithmsMetrics, BufferedImage> output = new HashMap<>();

        output.put(AlgorithmsMetrics.IMP,ivpImp.getImage());
        output.put(AlgorithmsMetrics.ADAPT, ivpAdapt.getImage());
        output.put(AlgorithmsMetrics.WEKA, ivpWeka.getImage());

        return output;
    }
    private enum StatsComboBox{
        PROPERTIES("Właściwości"),
        CLUSTERS("Klastry"),
        METRICS("Metryki"),
        INITIAL("Punkty początkowe"),
        CLUSTER_METRICS("Metryki poszczególnych klastrów");

        final String value;

        StatsComboBox(String value){
            this.value = value;
        }

        public static JComboBox<String> getJComboBox(){
            return new JComboBox<>(new String[]{PROPERTIES.value, METRICS.value,CLUSTER_METRICS.value,CLUSTERS.value,INITIAL.value});
        }
    }
}
