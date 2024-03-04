package pl.amrusb.util.ui.panels;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import pl.amrusb.util.Calculations;
import pl.amrusb.util.img.ImageRescaler;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Point3D;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.table.CTable;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private JPanel bottomPanel;

    private BufferedImage leftImage;
    private JLabel leftImageLabel;

    private BufferedImage rightImage;
    private JLabel rightImageLabel;

    private JPanel chartsPanel;
    private CardLayout chartsCardLayout;
    private ChartPanel histogramPanel;
    private JFreeChart histogramChart;
    private ChartPanel metricsChartPanel;
    private JFreeChart metricsChart;
    private ChartPanel sizesChartPanel;
    private JFreeChart sizesChart;


    private JPanel statsPanel;
    private CardLayout statsCardLayout;
    private MetricsPanel pMetrics;
    private PropertiesPanel pProperties;

    private CTable ownCTable;
    private CTable wekaCTable;

    private CTable ownITable;
    private CTable wekaITable;
    private CTable metricsTable;


    public ComparePanel(){
        setLayout(new BorderLayout());

        createComponents();
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(1,2));
        imagePanel.setPreferredSize(
                new Dimension(
                        MainFrame.getFrameWidth(),
                        MainFrame.getFrameHeight() * 2 / 3
                )
        );

        JScrollPane leftSP = new JScrollPane(leftPanel);
        JScrollPane rightSP = new JScrollPane(rightPanel);

        imagePanel.add(leftSP);
        imagePanel.add(rightSP);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, imagePanel, bottomPanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerSize(5);

        this.add(splitPane, BorderLayout.CENTER);
    }

    private void createComponents(){
        createLeftPanel();
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


        histogramPanel = new ChartPanel(histogramChart);

        JPanel clustersPanel = new JPanel();
        clustersPanel.setLayout(new GridLayout(0,2));

        metricsChartPanel = new ChartPanel(metricsChart);
        sizesChartPanel = new ChartPanel(sizesChart);

        clustersPanel.add(sizesChartPanel);
        clustersPanel.add(metricsChartPanel);

        chartsPanel.add(histogramPanel, StatsComboBox.PROPERTIES.value);
        chartsPanel.add(clustersPanel, StatsComboBox.CLUSTERS.value);

    }
    private void createStatsPanel(){
        statsCardLayout = new CardLayout();
        statsPanel = new JPanel();
        statsPanel.setLayout(statsCardLayout);

        JPanel metricsPanel = new JPanel();
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(10);
        metricsPanel.setLayout(borderLayout);


        pMetrics = new MetricsPanel();
        metricsPanel.add(pMetrics, BorderLayout.WEST);
        pProperties = new PropertiesPanel();
        metricsPanel.add(pProperties, BorderLayout.CENTER);

        /* PANEL KLASTRÓW */
        JPanel clustersPanel  = new JPanel();
        clustersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        ownCTable = new CTable();

        ownCTable.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
        ownCTable.setColumnWidth("30,100,100,50");

        wekaCTable = new CTable();

        wekaCTable.addColumn("Lp.","Współrzędne","Rozmiar","Kolor");
        wekaCTable.setColumnWidth("30,100,100,50");

        metricsTable = new CTable();
        metricsTable.addColumn("Lp.", "Indeks Jaccard'a", "Współczynnik Dice'a");
        metricsTable.setColumnWidth("30,100,100");

        TablePane tpOwnC = new TablePane("Implementajca", ownCTable);
        TablePane tpWekaC = new TablePane("Weka", wekaCTable);
        TablePane tpMetrics = new TablePane("Metryki", metricsTable);

        clustersPanel.add(tpOwnC);
        clustersPanel.add(tpWekaC);
        clustersPanel.add(tpMetrics);

        /* PANEL POCZATKOWYCH PUNKTÓW */
        JPanel initialsPanel = new JPanel();
        initialsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ownITable = new CTable();

        ownITable.addColumn("Lp.","Współrzędne","Kolor");
        ownITable.setColumnWidth("30,100,50");
        wekaITable = new CTable();

        wekaITable.addColumn("Lp.","Współrzędne","Kolor");
        wekaITable.setColumnWidth("30,100,50");

        TablePane tpOwnI = new TablePane("Implementacja", ownITable);
        TablePane tpWekaI = new TablePane("Weka", wekaITable);

        initialsPanel.add(tpOwnI);
        initialsPanel.add(tpWekaI);

        /* */

        statsPanel.add(metricsPanel, StatsComboBox.PROPERTIES.value);
        statsPanel.add(new JScrollPane(clustersPanel), StatsComboBox.CLUSTERS.value);
        statsPanel.add(new JScrollPane(initialsPanel), StatsComboBox.INITIAL.value);

    }
    public void fillInitialsTable(ArrayList<Point3D> dataSet1, ArrayList<Point3D> dataSet2){
        if(dataSet1.size() == dataSet2.size()){
            for (int i = 0; i < dataSet1.size();i++) {
                Point3D ds1C = dataSet1.get(i);
                Point3D ds2C = dataSet2.get(i);

                ownITable.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
                });
                wekaITable.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
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
        wekaITable.setRenderer();
    }
    public void fillClustersTable(ArrayList<Cluster> dataSet1, ArrayList<Cluster> dataSet2){
        if(dataSet1.size() == dataSet2.size()){
            for (int i = 0; i < dataSet1.size();i++) {
                Cluster ds1C = dataSet1.get(i);
                Cluster ds2C = dataSet2.get(i);

                ownCTable.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        ds1C.getSize(),
                        new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
                });
                wekaCTable.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        ds2C.getSize(),
                        new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
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
            Integer wekaIterCount,
            Float impTime,
            Float wekaTime
    ){
        pProperties.setValues(fileName,pixelCount,clusterCount,impIterCount,wekaIterCount,impTime,wekaTime);
    }

    public void setMetricsValues(
            Double jaccard,
            Double dice,
            Double mse,
            Double rmse
    ){
        pMetrics.setValues(jaccard, dice, mse, rmse);
    }
    public void setImageLabel(BufferedImage image, Position position){
        ImageIcon imageIcon;
        double frameWidth = MainFrame.getFrameWidth() / 2.0;
        double frameHeight = MainFrame.getFrameHeight() * 2.0 / 3;

        int width = image.getWidth();
        int height = image.getHeight();

        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min(frameWidth / (width), frameHeight / (height));
            BufferedImage displayImage = ImageRescaler.rescaleImage(image, scale);
            imageIcon = new ImageIcon(displayImage);
        }
        else{
            imageIcon = new ImageIcon(image);
        }

        switch(position){
            case LEFT -> leftImageLabel.setIcon(imageIcon);
            case RIGHT -> rightImageLabel.setIcon(imageIcon);
        }

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

    public enum Position{ LEFT, RIGHT }

    private enum StatsComboBox{
        PROPERTIES("Właściwości"),
        CLUSTERS("Klastry"),
        INITIAL("Punkty początkowe");

        final String value;

        StatsComboBox(String value){
            this.value = value;
        }

        public static JComboBox<String> getJComboBox(){
            return new JComboBox<>(new String[]{PROPERTIES.value, CLUSTERS.value,INITIAL.value});
        }
    }
    private class MetricsPanel extends JPanel{
        JLabel lMetric = new JLabel("Metryka", SwingConstants.CENTER);
        JLabel lValue = new JLabel("Wartość", SwingConstants.CENTER);
        JLabel lJaccard = new JLabel("Indeks Jaccard'a", SwingConstants.CENTER);
        JTextField tfJaccard = new JTextField();
        JLabel lDice = new JLabel("Współczynnik Dice'a", SwingConstants.CENTER);
        JTextField tfDice = new JTextField();
        JLabel lMSE = new JLabel("MSE", SwingConstants.CENTER);
        JTextField tfMSE = new JTextField();
        JLabel lRMSE = new JLabel("RMSE", SwingConstants.CENTER);
        JTextField tfRMSE = new JTextField();

        public MetricsPanel(){
            tfDice.setEditable(false);
            tfJaccard.setEditable(false);
            tfMSE.setEditable(false);
            tfRMSE.setEditable(false);
            tfDice.setBackground(Color.WHITE);
            tfJaccard.setBackground(Color.WHITE);
            tfMSE.setBackground(Color.WHITE);
            tfRMSE.setBackground(Color.WHITE);

            Border etchedBorder = BorderFactory.createEtchedBorder();
            GridBagConstraints c = new GridBagConstraints();
            this.setLayout(new GridLayout(5, 2));
            c.weightx = 100;
            c.weighty = 100;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.ipadx = 10;
            c.ipady = 5;
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.BOTH;

            this.add(lMetric,c);
            lMetric.setBorder(etchedBorder);
            c.gridx = 1;
            this.add(lValue,c);
            lValue.setBorder(etchedBorder);

            c.gridx = 0;
            c.gridy = 1;
            this.add(lJaccard,c);
            lJaccard.setBorder(etchedBorder);
            c.gridx = 1;
            this.add(tfJaccard,c);

            c.gridx = 0;
            c.gridy = 2;
            this.add(lDice,c);
            lDice.setBorder(etchedBorder);
            c.gridx = 1;
            this.add(tfDice,c);
            c.gridx = 0;
            c.gridy = 3;
            this.add(lMSE,c);
            lMSE.setBorder(etchedBorder);
            c.gridx = 1;
            this.add(tfMSE,c);
            c.gridx = 0;
            c.gridy = 4;
            this.add(lRMSE,c);
            lRMSE.setBorder(etchedBorder);
            c.gridx = 1;
            this.add(tfRMSE,c);
        }
        public void setValues(
                Double jaccard,
                Double dice,
                Double mse,
                Double rmse
        ){
            tfJaccard.setText(
                    String.valueOf(Calculations.round(jaccard, 4))
            );
            tfDice.setText(
                    String.valueOf(Calculations.round(dice,4))
            );
            tfMSE.setText(
                    String.valueOf(Calculations.round(mse, 4))
            );

            tfRMSE.setText(
                    String.valueOf(Calculations.round(rmse,4))
            );
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
        JLabel lWeka = new JLabel("Weka", SwingConstants.CENTER);
        JLabel lIterationCount = new JLabel("Liczba iteracji:");
        JLabel vlImpIterCount  = new JLabel();
        JLabel vlWekaIterCount  = new JLabel();

        JLabel lTime = new JLabel("<html>Czas trwania <small>(sec)</small>:</html>");
        JLabel vlImpTime = new JLabel();
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
            c.gridwidth = 2;
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
            c.gridwidth = 2;
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
            c.gridwidth = 2;
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
                Integer wekaIterCount,
                Float impTime,
                Float wekaTime
        ){
            vlName.setText(fileName);
            vlPixelCount.setText(pixelCount.toString());
            vlClusterCount.setText(clusterCount.toString());
            vlImpIterCount.setText(impIterCount.toString());
            vlWekaIterCount.setText(wekaIterCount.toString());
            vlImpTime.setText(impTime.toString());
            vlWekaTime.setText(wekaTime.toString());
        }
    }
}
