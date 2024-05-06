package pl.amrusb.util.ui.panels;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.JFreeChart;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.constants.MetricsTypes;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Point3D;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.table.CTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ComparePanel extends JPanel {
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

    public ComparePanel(){
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
    @Getter
    public class MetricsPanel extends JPanel{
        JLabel lImpAdapt = new JLabel(AlgorithmsMetrics.IMP_ADAPT.getValue(), SwingConstants.CENTER);
        JLabel lImpWeka = new JLabel(AlgorithmsMetrics.IMP_WEKA.getValue(), SwingConstants.CENTER);
        JLabel lAdaptWeka = new JLabel(AlgorithmsMetrics.ADAPT_WEKA.getValue(), SwingConstants.CENTER);

        JLabel lJaccard = new JLabel(MetricsTypes.JACCARD.getValue(), SwingConstants.CENTER);
        JTextField tfJaccard1 = new JTextField();
        JTextField tfJaccard2 = new JTextField();
        JTextField tfJaccard3 = new JTextField();
        JLabel lDice = new JLabel(MetricsTypes.DICE.getValue(), SwingConstants.CENTER);
        JTextField tfDice1 = new JTextField();
        JTextField tfDice2 = new JTextField();
        JTextField tfDice3 = new JTextField();

        JLabel lImp = new JLabel(AlgorithmsMetrics.IMP.getValue(), SwingConstants.CENTER);
        JLabel lAdapt = new JLabel(AlgorithmsMetrics.ADAPT.getValue(), SwingConstants.CENTER);
        JLabel lWeka = new JLabel(AlgorithmsMetrics.WEKA.getValue(), SwingConstants.CENTER);


        JLabel lSil = new JLabel(MetricsTypes.SIHLOUETTE.getValue(), SwingConstants.CENTER);
        JTextField tfImpSil   = new JTextField();
        JTextField tfAdaptSil   = new JTextField();
        JTextField tfWekaSil   = new JTextField();


        public MetricsPanel(){
            Border etchedBorder = BorderFactory.createEtchedBorder();
            GridBagConstraints c = new GridBagConstraints();
            this.setLayout(new GridBagLayout());
            c.weightx = 100;
            c.weighty = 100;
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.BOTH;

            /*--JACCARD DICE--*/
            lImpAdapt.setBorder(etchedBorder);
            this.add(lImpAdapt, c);
            c.gridx = 2;
            lImpWeka.setBorder(etchedBorder);
            this.add(lImpWeka, c);
            c.gridx = 3;
            lAdaptWeka.setBorder(etchedBorder);
            this.add(lAdaptWeka, c);

            //JACCARD
            c.gridx = 0;
            c.gridy = 1;
            lJaccard.setBorder(etchedBorder);
            this.add(lJaccard, c);
            c.gridx = 1;
            tfJaccard1.setEditable(false);
            tfJaccard1.setBackground(Color.WHITE);
            this.add(tfJaccard1, c);
            c.gridx = 2;
            tfJaccard2.setEditable(false);
            tfJaccard2.setBackground(Color.WHITE);
            this.add(tfJaccard2, c);
            c.gridx = 3;
            tfJaccard3.setEditable(false);
            tfJaccard3.setBackground(Color.WHITE);
            this.add(tfJaccard3, c);

            //DICE
            c.gridx = 0;
            c.gridy = 2;
            lDice.setBorder(etchedBorder);
            this.add(lDice, c);
            c.gridx = 1;
            tfDice1.setEditable(false);
            tfDice1.setBackground(Color.WHITE);
            this.add(tfDice1, c);
            c.gridx = 2;
            tfDice2.setBackground(Color.WHITE);
            tfDice2.setEditable(false);
            this.add(tfDice2, c);
            c.gridx = 3;
            tfDice3.setEditable(false);
            tfDice3.setBackground(Color.WHITE);
            this.add(tfDice3, c);
            /*--Silhouette--*/
            c.gridy = 3;
            c.gridx = 1;
            lImp.setBorder(etchedBorder);
            this.add(lImp, c);
            c.gridx = 2;
            lAdapt.setBorder(etchedBorder);
            this.add(lAdapt, c);
            c.gridx = 3;
            lWeka.setBorder(etchedBorder);
            this.add(lWeka, c);
            c.gridx = 0;
            c.gridy = 4;
            lSil.setBorder(etchedBorder);
            this.add(lSil, c);
            c.gridx = 1;
            tfImpSil.setEditable(false);
            tfImpSil.setBackground(Color.WHITE);
            this.add(tfImpSil, c);
            c.gridx = 2;
            tfAdaptSil.setEditable(false);
            tfAdaptSil.setBackground(Color.WHITE);
            this.add(tfAdaptSil, c);
            c.gridx = 3;
            tfWekaSil.setEditable(false);
            tfWekaSil.setBackground(Color.WHITE);
            this.add(tfWekaSil, c);
        }

        public void setJaccardValues(Double jaccard1, Double jaccard2, Double jaccard3){
            tfJaccard1.setText(jaccard1.toString());
            tfJaccard2.setText(jaccard2.toString());
            tfJaccard3.setText(jaccard3.toString());
        }
        public void setDiceValues(Double dice1, Double dice2, Double dice3){
            tfDice1.setText(dice1.toString());
            tfDice2.setText(dice2.toString());
            tfDice3.setText(dice3.toString());
        }
        public void setSilhouetteValues(Double silhouette1,Double silhouette2,Double silhouette3){
            tfImpSil.setText(silhouette1.toString());
            tfAdaptSil.setText(silhouette2.toString());
            tfWekaSil.setText(silhouette3.toString());
        }
    }
    @Getter
    public class PropertiesPanel extends JPanel{
        JLabel lName = new JLabel("Nazwa pliku:");
        JLabel vlName = new JLabel();

        JLabel lPixelCount = new JLabel("Liczba pikseli:");
        JLabel vlPixelCount = new JLabel();

        JLabel lClusterCount = new JLabel("Liczba klastrów:");
        JLabel vlClusterCount = new JLabel();

        JLabel lMaxIter = new JLabel("Maksymalna liczba iteracji:");
        JLabel vlMaxIter = new JLabel();

        JLabel lImp = new JLabel(AlgorithmsMetrics.IMP.getValue(), SwingConstants.CENTER);
        JLabel lAdapt = new JLabel(AlgorithmsMetrics.ADAPT.getValue(), SwingConstants.CENTER);
        JLabel lWeka = new JLabel(AlgorithmsMetrics.WEKA.getValue(), SwingConstants.CENTER);


        JLabel lIterationCount = new JLabel("Liczba iteracji:");
        JLabel vlImpIterCount  = new JLabel();
        JLabel vlAdaptIterCount  = new JLabel();
        JLabel vlWekaIterCount  = new JLabel();


        JLabel lTime = new JLabel("<html>Czas trwania <small>(sec)</small>:</html>");
        JLabel vlImpTime = new JLabel();
        JLabel vlAdaptTime = new JLabel();
        JLabel vlWekaTime = new JLabel();

        public PropertiesPanel(){
            Border lineBorder = BorderFactory.createEtchedBorder();
            Border etchedBorder = BorderFactory.createEtchedBorder();
            GridBagConstraints c = new GridBagConstraints();
            this.setLayout(new GridBagLayout());
            c.weightx = 100;
            c.weighty = 100;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.BOTH;

            this.add(lName,c);
            lName.setBorder(lineBorder);
            c.gridx = 1;
            c.gridwidth = 3;
            this.add(vlName, c);
            vlName.setBorder(lineBorder);
            vlName.setOpaque(true);
            vlName.setBackground(Color.WHITE);

            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            this.add(lPixelCount,c);
            lPixelCount.setBorder(lineBorder);
            c.gridx = 1;
            c.gridwidth = 3;
            this.add(vlPixelCount, c);
            vlPixelCount.setBorder(lineBorder);
            vlPixelCount.setOpaque(true);
            vlPixelCount.setBackground(Color.WHITE);
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 1;
            this.add(lClusterCount,c);
            lClusterCount.setBorder(lineBorder);
            c.gridx = 1;
            c.gridwidth = 3;
            this.add(vlClusterCount, c);
            vlClusterCount.setBorder(lineBorder);
            vlClusterCount.setOpaque(true);
            vlClusterCount.setBackground(Color.WHITE);

            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 1;
            this.add(lMaxIter,c);
            lMaxIter.setBorder(lineBorder);
            c.gridx = 1;
            c.gridwidth = 3;
            this.add(vlMaxIter, c);
            vlMaxIter.setBorder(lineBorder);
            vlMaxIter.setOpaque(true);
            vlMaxIter.setBackground(Color.WHITE);

            c.gridx = 1;
            c.gridy = 4;
            c.gridwidth = 1;
            this.add(lImp,c);
            lImp.setBorder(etchedBorder);
            c.gridx = 2;
            this.add(lAdapt,c);
            lAdapt.setBorder(etchedBorder);
            c.gridx = 3;
            this.add(lWeka, c);
            lWeka.setBorder(etchedBorder);
            c.gridx = 0;
            c.gridy = 5;
            this.add(lIterationCount,c);
            lIterationCount.setBorder(lineBorder);
            c.gridx = 1;
            this.add(vlImpIterCount,c);
            vlImpIterCount.setBorder(lineBorder);
            vlImpIterCount.setOpaque(true);
            vlImpIterCount.setBackground(Color.WHITE);
            c.gridx = 2;
            this.add(vlAdaptIterCount,c);
            vlAdaptIterCount.setBorder(lineBorder);
            vlAdaptIterCount.setOpaque(true);
            vlAdaptIterCount.setBackground(Color.WHITE);
            c.gridx = 3;
            this.add(vlWekaIterCount,c);
            c.gridx = 0;
            c.gridy = 6;
            vlWekaIterCount.setBorder(lineBorder);
            vlWekaIterCount.setOpaque(true);
            vlWekaIterCount.setBackground(Color.WHITE);

            this.add(lTime,c);
            lTime.setBorder(lineBorder);
            c.gridx = 1;
            this.add(vlImpTime,c);
            vlImpTime.setBorder(lineBorder);
            vlImpTime.setOpaque(true);
            vlImpTime.setBackground(Color.WHITE);
            c.gridx = 2;
            this.add(vlAdaptTime,c);
            vlAdaptTime.setBorder(lineBorder);
            vlAdaptTime.setOpaque(true);
            vlAdaptTime.setBackground(Color.WHITE);
            c.gridx = 3;
            this.add(vlWekaTime,c);
            vlWekaTime.setBorder(lineBorder);
            vlWekaTime.setBackground(Color.WHITE);
            vlWekaTime.setOpaque(true);
        }

        public void setValues(
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
            vlName.setText(fileName);
            vlPixelCount.setText(pixelCount.toString());
            vlClusterCount.setText(clusterCount.toString());
            vlMaxIter.setText(maxIterations.toString());
            vlImpIterCount.setText(impIterCount.toString());
            vlAdaptIterCount.setText(adaptIterCount.toString());
            vlWekaIterCount.setText(wekaIterCount.toString());
            vlImpTime.setText(impTime.toString());
            vlAdaptTime.setText(adaptTime.toString());
            vlWekaTime.setText(wekaTime.toString());
        }
    }
    @Getter
    public class ClusterMetricsPanel extends JPanel{
        private CTable tbShiluette;
        private CTable tbImpAdapt;
        private CTable tbImpWeka;
        private CTable tbAdaptWeka;

        public ClusterMetricsPanel(){
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            tbShiluette = new CTable();
            tbShiluette.addColumn("Lp.", AlgorithmsMetrics.IMP.getValue(), AlgorithmsMetrics.ADAPT.getValue(), AlgorithmsMetrics.WEKA.getValue());
            tbShiluette.setColumnWidth("30,100,100,100");

            tbImpAdapt = new CTable();
            tbImpAdapt.addColumn("Lp.", MetricsTypes.JACCARD.getValue(), MetricsTypes.DICE.getValue());
            tbImpAdapt.setColumnWidth("30,100,100");

            tbImpWeka = new CTable();
            tbImpWeka.addColumn("Lp.", MetricsTypes.JACCARD.getValue(), MetricsTypes.DICE.getValue());
            tbImpWeka.setColumnWidth("30,100,100");

            tbAdaptWeka = new CTable();
            tbAdaptWeka.addColumn("Lp.", MetricsTypes.JACCARD.getValue(), MetricsTypes.DICE.getValue());
            tbAdaptWeka.setColumnWidth("30,100,100");


            this.add(new TablePane(MetricsTypes.SIHLOUETTE.getValue(), tbShiluette));
            this.add(new TablePane(AlgorithmsMetrics.IMP_ADAPT.getValue(), tbImpAdapt));
            this.add(new TablePane(AlgorithmsMetrics.IMP_WEKA.getValue(), tbImpWeka));
            this.add(new TablePane(AlgorithmsMetrics.ADAPT_WEKA.getValue(), tbAdaptWeka));
        }

        public void fillTables(Map<AlgorithmsMetrics, ArrayList<Double>> dataSet1, Map<AlgorithmsMetrics, ArrayList<Double>> dataSet2, Map<AlgorithmsMetrics, ArrayList<Double>> dataSet3) {

            ArrayList<Double> impAdaptJaccard = dataSet1.get(AlgorithmsMetrics.IMP_ADAPT);
            ArrayList<Double> impAdaptDice = dataSet2.get(AlgorithmsMetrics.IMP_ADAPT);
            ArrayList<Double> impWekaJaccard = dataSet1.get(AlgorithmsMetrics.IMP_WEKA);
            ArrayList<Double> impWekaDice = dataSet2.get(AlgorithmsMetrics.IMP_WEKA);
            ArrayList<Double> adaptWekaJaccard = dataSet1.get(AlgorithmsMetrics.ADAPT_WEKA);
            ArrayList<Double> adaptWekaDice = dataSet2.get(AlgorithmsMetrics.ADAPT_WEKA);
            ArrayList<Double> impSil = dataSet3.get(AlgorithmsMetrics.IMP);
            ArrayList<Double> adaptSil = dataSet3.get(AlgorithmsMetrics.ADAPT);
            ArrayList<Double> wekaSil = dataSet3.get(AlgorithmsMetrics.WEKA);
            int clusterNum = wekaSil.size();
            for (int i = 0; i < clusterNum; i++) {
               tbImpAdapt.addRow(new Object[]{
                       i + 1,
                       impAdaptJaccard.get(i),
                       impAdaptDice.get(i)
                });
                tbImpWeka.addRow(new Object[]{
                        i + 1,
                        impWekaJaccard.get(i),
                        impWekaDice.get(i)
                });
                tbAdaptWeka.addRow(new Object[]{
                        i + 1,
                        adaptWekaJaccard.get(i),
                        adaptWekaDice.get(i)
                });

                tbShiluette.addRow(new Object[]{
                        i+1,
                        impSil.get(i),
                        adaptSil.get(i),
                        wekaSil.get(i)
                });
            }

            tbShiluette.setRenderer();
            tbImpAdapt.setRenderer();
            tbImpWeka.setRenderer();
            tbAdaptWeka.setRenderer();
        }
    }
    private class ClusterPanel extends JPanel{
        private CTable ownCTable;
        private CTable adaptCTable;
        private CTable wekaCTable;

        public ClusterPanel(){
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            ownCTable = new CTable();

            ownCTable.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
            ownCTable.setColumnWidth("30,100,100,50");

            adaptCTable = new CTable();
            adaptCTable.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
            adaptCTable.setColumnWidth("30,100,100,50");

            wekaCTable = new CTable();

            wekaCTable.addColumn("Lp.","Współrzędne","Rozmiar","Kolor");
            wekaCTable.setColumnWidth("30,100,100,50");

            TablePane tpOwnC = new TablePane(AlgorithmsMetrics.IMP.getValue(), ownCTable);
            TablePane tpAcaptC = new TablePane(AlgorithmsMetrics.ADAPT.getValue(), adaptCTable);
            TablePane tpWekaC = new TablePane(AlgorithmsMetrics.WEKA.getValue(), wekaCTable);

            this.add(tpOwnC);
            this.add(tpAcaptC);
            this.add(tpWekaC);
        }

        public void fillTables(ArrayList<Cluster> dataSet1, ArrayList<Cluster> dataSet2, ArrayList<Cluster> dataSet3){
            for (int i = 0; i < dataSet1.size();i++) {
                Cluster ds1C = dataSet1.get(i);
                Cluster ds2C = dataSet2.get(i);
                Cluster ds3C = dataSet3.get(i);

                ownCTable.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        ds1C.getSize(),
                        new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
                });
                adaptCTable.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        ds2C.getSize(),
                        new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
                });
                wekaCTable.addRow(new Object[]{
                        i + 1,
                        ds3C.toString(),
                        ds3C.getSize(),
                        new Color(ds3C.getX(), ds3C.getY(), ds3C.getZ())
                });
            }

            ownCTable.setRenderer();
            adaptCTable.setRenderer();
            wekaCTable.setRenderer();
        }
    }
    private class InitialCentroidPanel extends JPanel{
        private CTable ownITable;
        private CTable adaptITable;
        private CTable wekaITable;

        InitialCentroidPanel(){
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            ownITable = new CTable();

            ownITable.addColumn("Lp.","Współrzędne","Kolor");
            ownITable.setColumnWidth("30,100,50");

            adaptITable = new CTable();
            adaptITable.addColumn("Lp.","Współrzędne","Kolor");
            adaptITable.setColumnWidth("30,100,50");

            wekaITable = new CTable();
            wekaITable.addColumn("Lp.","Współrzędne","Kolor");
            wekaITable.setColumnWidth("30,100,50");

            TablePane tpOwnI = new TablePane(AlgorithmsMetrics.IMP.getValue(), ownITable);
            TablePane tpAdaptI = new TablePane(AlgorithmsMetrics.ADAPT.getValue(), adaptITable);
            TablePane tpWekaI = new TablePane(AlgorithmsMetrics.WEKA.getValue(), wekaITable);

            this.add(tpOwnI);
            this.add(tpAdaptI);
            this.add(tpWekaI);
        }

        public void fillTables(ArrayList<Point3D> dataSet1, ArrayList<Point3D> dataSet2, ArrayList<Point3D> dataSet3){
            for (int i = 0; i < dataSet1.size();i++) {
                Point3D ds1C = dataSet1.get(i);
                Point3D ds2C = dataSet2.get(i);
                Point3D ds3C = dataSet3.get(i);

                ownITable.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
                });
                adaptITable.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
                });
                wekaITable.addRow(new Object[]{
                        i + 1,
                        ds3C.toString(),
                        new Color(ds3C.getX(), ds3C.getY(), ds3C.getZ())
                });

            }

            ownITable.setRenderer();
            adaptITable.setRenderer();
            wekaITable.setRenderer();
        }
    }
}
