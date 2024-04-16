package pl.amrusb.util.ui.panels;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import pl.amrusb.util.Calculations;
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

@Getter
@Setter
public class ComparePanel extends JPanel {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JPanel leftPanel;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JPanel rightPanel;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JPanel centerPanel;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JPanel bottomPanel;

    private BufferedImage leftImage;
    private JLabel leftImageLabel;

    private BufferedImage centerImage;
    private JLabel centerImageLabel;

    private BufferedImage rightImage;
    private JLabel rightImageLabel;

    private JPanel chartsPanel;
    private CardLayout chartsCardLayout;
    private ChartPanel histogramPanel;
    private JFreeChart histogramChart;
    private ChartPanel mainMetricsPanel;
    private JFreeChart mainMetricsChart;
    private ChartPanel metricsChartPanel;
    private JFreeChart metricsChart;
    private ChartPanel sizesChartPanel;
    private JFreeChart sizesChart;
    private ChartPanel chpSilhouette;
    private JFreeChart chSilhouette;
    private ChartPanel chpMetrics;
    private JFreeChart chMetrics;

    private JPanel statsPanel;
    private CardLayout statsCardLayout;
    private MetricsPanel pMetrics;
    private PropertiesPanel pProperties;

    private CTable ownCTable;
    private CTable adaptCTable;
    private CTable wekaCTable;

    private CTable ownITable;
    private CTable adaptITable;
    private CTable wekaITable;
    private CTable metricsTable;


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

        JScrollPane leftSP = new JScrollPane(leftPanel);
        JScrollPane centerSP = new JScrollPane(centerPanel);
        JScrollPane rightSP = new JScrollPane(rightPanel);

        imagePanel.add(leftSP);
        imagePanel.add(centerSP);
        imagePanel.add(rightSP);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, imagePanel, bottomPanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerSize(5);

        this.add(splitPane, BorderLayout.CENTER);
    }

    private void createComponents(){
        createLeftPanel();
        createCenterPanel();
        createRightPanel();
        createBottomPanel();
    }

    private void createLeftPanel(){
        leftPanel = new JPanel();

        leftPanel.setLayout(new BorderLayout());

        leftImageLabel = new JLabel();
        leftImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        leftImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel leftLabel = new JLabel("Własna implementacja");

        leftPanel.add(leftImageLabel, BorderLayout.CENTER);
        leftPanel.add(leftLabel, BorderLayout.SOUTH);
    }

    private void createCenterPanel(){
        centerPanel = new JPanel();

        centerPanel.setLayout(new BorderLayout());

        centerImageLabel = new JLabel();
        centerImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        centerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel centerLabel = new JLabel("Adaptive");

        centerPanel.add(centerImageLabel, BorderLayout.CENTER);
        centerPanel.add(centerLabel, BorderLayout.SOUTH);
    }

    private void createRightPanel(){
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        rightImageLabel = new JLabel();
        rightImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        rightImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel rightLabel = new JLabel("Weka");

        rightPanel.add(rightImageLabel, BorderLayout.CENTER);
        rightPanel.add(rightLabel, BorderLayout.SOUTH);
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

    private void createLeftBottomPanel(){
        chartsCardLayout = new CardLayout();
        chartsPanel = new JPanel();
        chartsPanel.setLayout(chartsCardLayout);

        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new GridLayout(0,2));
        mainMetricsPanel = new ChartPanel(mainMetricsChart);
        histogramPanel = new ChartPanel(histogramChart);

        propertiesPanel.add(histogramPanel);
        propertiesPanel.add(mainMetricsPanel);

        JPanel clustersPanel = new JPanel();
        clustersPanel.setLayout(new GridLayout(0,2));

        metricsChartPanel = new ChartPanel(metricsChart);
        sizesChartPanel = new ChartPanel(sizesChart);

        clustersPanel.add(sizesChartPanel);
        clustersPanel.add(metricsChartPanel);

        JPanel metricsPanel = new JPanel();
        metricsPanel.setLayout(new GridLayout(0,2));

        chpSilhouette = new ChartPanel(chSilhouette);
        chpMetrics = new ChartPanel(chMetrics);
        metricsPanel.add(chpSilhouette);
        metricsPanel.add(chpMetrics);

        chartsPanel.add(propertiesPanel, StatsComboBox.PROPERTIES.value);
        chartsPanel.add(clustersPanel, StatsComboBox.CLUSTERS.value);
        chartsPanel.add(metricsPanel, StatsComboBox.METRICS.value);

    }
    private void createStatsPanel(){
        statsCardLayout = new CardLayout();
        statsPanel = new JPanel();
        statsPanel.setLayout(statsCardLayout);

        pMetrics = new MetricsPanel();
        pProperties = new PropertiesPanel();

        /* PANEL KLASTRÓW */
        JPanel clustersPanel  = new JPanel();
        clustersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        ownCTable = new CTable();

        ownCTable.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
        ownCTable.setColumnWidth("30,100,100,50");

        adaptCTable = new CTable();
        adaptCTable.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
        adaptCTable.setColumnWidth("30,100,100,50");

        wekaCTable = new CTable();

        wekaCTable.addColumn("Lp.","Współrzędne","Rozmiar","Kolor");
        wekaCTable.setColumnWidth("30,100,100,50");

        metricsTable = new CTable();
        metricsTable.addColumn("Lp.", "Indeks Jaccard'a", "Współczynnik Dice'a");
        metricsTable.setColumnWidth("30,100,100");

        TablePane tpOwnC = new TablePane("Implementajca", ownCTable);
        TablePane tpAcaptC = new TablePane("Adaptive", adaptCTable);
        TablePane tpWekaC = new TablePane("Weka", wekaCTable);
        TablePane tpMetrics = new TablePane("Metryki", metricsTable);

        clustersPanel.add(tpOwnC);
        clustersPanel.add(tpAcaptC);
        clustersPanel.add(tpWekaC);
        clustersPanel.add(tpMetrics);

        /* PANEL POCZATKOWYCH PUNKTÓW */
        JPanel initialsPanel = new JPanel();
        initialsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ownITable = new CTable();

        ownITable.addColumn("Lp.","Współrzędne","Kolor");
        ownITable.setColumnWidth("30,100,50");

        adaptITable = new CTable();
        adaptITable.addColumn("Lp.","Współrzędne","Kolor");
        adaptITable.setColumnWidth("30,100,50");

        wekaITable = new CTable();
        wekaITable.addColumn("Lp.","Współrzędne","Kolor");
        wekaITable.setColumnWidth("30,100,50");

        TablePane tpOwnI = new TablePane("Implementacja", ownITable);
        TablePane tpAdaptI = new TablePane("Adaptive", adaptITable);
        TablePane tpWekaI = new TablePane("Weka", wekaITable);

        initialsPanel.add(tpOwnI);
        initialsPanel.add(tpAdaptI);
        initialsPanel.add(tpWekaI);

        /* */

        statsPanel.add(pProperties, StatsComboBox.PROPERTIES.value);
        statsPanel.add(new JScrollPane(clustersPanel), StatsComboBox.CLUSTERS.value);
        statsPanel.add(pMetrics, StatsComboBox.METRICS.value);
        statsPanel.add(new JScrollPane(initialsPanel), StatsComboBox.INITIAL.value);

    }
    public void fillInitialsTable(ArrayList<Point3D> dataSet1, ArrayList<Point3D> dataSet2, ArrayList<Point3D> dataSet3){
        if(dataSet1.size() == dataSet2.size()){
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
        }
        else{
            for (int i = 0; i < dataSet1.size();i++) {
                Point3D ds1C = dataSet1.get(i);

                ownITable.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
                });
            }
            for (int i = 0; i < dataSet2.size();i++) {
                Point3D ds2C = dataSet2.get(i);

                wekaITable.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
                });
            }
        }

        ownITable.setRenderer();
        adaptITable.setRenderer();
        wekaITable.setRenderer();
    }
    public void fillClustersTable(ArrayList<Cluster> dataSet1, ArrayList<Cluster> dataSet2, ArrayList<Cluster> dataSet3){
        if(dataSet1.size() == dataSet2.size()){
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
        }
        else{
            for (int i = 0; i < dataSet1.size();i++) {
                Cluster ds1C = dataSet1.get(i);

                ownCTable.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        ds1C.getSize(),
                        new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
                });
            }
            for (int i = 0; i < dataSet2.size();i++) {
                Cluster ds2C = dataSet2.get(i);

                wekaCTable.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        ds2C.getSize(),
                        new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
                });
            }
        }

        ownCTable.setRenderer();
        adaptCTable.setRenderer();
        wekaCTable.setRenderer();
    }
    public void fillClustersMetricsTable(double[] jaccardIndexes, double[] diceCoefs){
        int size = jaccardIndexes.length;

        for(int i = 0; i < size; i++){
            metricsTable.addRow(new Object[]{
                    i + 1,
                    Calculations.round(jaccardIndexes[i], 4),
                    Calculations.round(diceCoefs[i], 4)
            });
        }

        metricsTable.setRenderer();
    }
    public void setPropertiesValues(
            String fileName,
            Integer pixelCount,
            Integer clusterCount,
            Integer impIterCount,
            Integer adaptIterCount,
            Integer wekaIterCount,
            Float impTime,
            Float adaptTime,
            Float wekaTime
    ){
        pProperties.setValues(fileName,pixelCount,clusterCount,impIterCount,adaptIterCount,wekaIterCount,impTime,adaptTime,wekaTime);
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

    public void setImageLabel(BufferedImage image, Position position){
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

        File stream = null;
        try {
            stream = File.createTempFile("show-img", ".jpg");
            ImageIO.write(image, "jpg", stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        htmlString = String.format(htmlString, stream.toString() , width, height);

        switch(position){
            case LEFT -> leftImageLabel.setText(htmlString);
            case RIGHT -> rightImageLabel.setText(htmlString);
            case CENTER -> centerImageLabel.setText(htmlString);
        }
    }


    public void setMainMetriscChart(JFreeChart chart){
        mainMetricsChart = chart;
        mainMetricsPanel.setChart(chart);
    }
    public void setHistogram(JFreeChart chart){
        histogramChart = chart;
        histogramPanel.setChart(chart);
    }
    public void setMetricsChart(JFreeChart chart){
        metricsChart = chart;
        metricsChartPanel.setChart(chart);
    }

    public void setSizesChart(JFreeChart chart){
        sizesChart = chart;
        sizesChartPanel.setChart(chart);
    }

    public void setChMetrics(JFreeChart chart){
        chMetrics = chart;
        chpMetrics.setChart(chart);
    }

    public enum Position{ LEFT, RIGHT, CENTER }

    private enum StatsComboBox{
        PROPERTIES("Właściwości"),
        CLUSTERS("Klastry"),
        METRICS("Metryki"),
        INITIAL("Punkty początkowe");

        final String value;

        StatsComboBox(String value){
            this.value = value;
        }

        public static JComboBox<String> getJComboBox(){
            return new JComboBox<>(new String[]{PROPERTIES.value, CLUSTERS.value, METRICS.value,INITIAL.value});
        }
    }
    private class MetricsPanel extends JPanel{
        JLabel lImpAdapt = new JLabel("Implementacja - Adaptive", SwingConstants.CENTER);
        JLabel lImpWeka = new JLabel("Implementacja - Weka", SwingConstants.CENTER);
        JLabel lAdaptWeka = new JLabel("Adaptive - Weka", SwingConstants.CENTER);

        JLabel lJaccard = new JLabel("Indeks Jaccard'a", SwingConstants.CENTER);
        JTextField tfJaccard1 = new JTextField();
        JTextField tfJaccard2 = new JTextField();
        JTextField tfJaccard3 = new JTextField();
        JLabel lDice = new JLabel("Współczynnik Dice'a", SwingConstants.CENTER);
        JTextField tfDice1 = new JTextField();
        JTextField tfDice2 = new JTextField();
        JTextField tfDice3 = new JTextField();

        JLabel lImp = new JLabel("Implementacja", SwingConstants.CENTER);
        JLabel lAdapt = new JLabel("Adaptive", SwingConstants.CENTER);
        JLabel lWeka = new JLabel("Weka", SwingConstants.CENTER);


        JLabel lSil = new JLabel("Wynik Silhouette", SwingConstants.CENTER);
        JTextField tfImpSil   = new JTextField();
        JTextField tfAdaptSil   = new JTextField();
        JTextField tfWekaSil   = new JTextField();


        public MetricsPanel(){
            Border lineBorder = BorderFactory.createEtchedBorder();
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
    private class PropertiesPanel extends JPanel{
        JLabel lName = new JLabel("Nazwa pliku:");
        JLabel vlName = new JLabel();

        JLabel lPixelCount = new JLabel("Liczba pikseli:");
        JLabel vlPixelCount = new JLabel();

        JLabel lClusterCount = new JLabel("Liczba klastrów:");
        JLabel vlClusterCount = new JLabel();

        JLabel lImp = new JLabel("Implementacja", SwingConstants.CENTER);
        JLabel lAdapt = new JLabel("Adaptive", SwingConstants.CENTER);
        JLabel lWeka = new JLabel("Weka", SwingConstants.CENTER);


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

            c.gridx = 1;
            c.gridy = 3;
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
            c.gridy = 4;
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
            c.gridy = 5;
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
            vlImpIterCount.setText(impIterCount.toString());
            vlAdaptIterCount.setText(adaptIterCount.toString());
            vlWekaIterCount.setText(wekaIterCount.toString());
            vlImpTime.setText(impTime.toString());
            vlAdaptTime.setText(adaptTime.toString());
            vlWekaTime.setText(wekaTime.toString());
        }
    }
}
