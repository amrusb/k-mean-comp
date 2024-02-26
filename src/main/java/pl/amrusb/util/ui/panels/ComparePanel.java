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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
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

    private String imageName;

    private BufferedImage leftImage;
    private JLabel leftImageLabel;

    private BufferedImage rightImage;
    private JLabel rightImageLabel;

    private JFreeChart imageChart;
    private ChartPanel leftBottomPanel;

    private JPanel statsPanel;
    private CardLayout cardLayout;
    private JTable propTable;
    private DefaultTableModel propModel;

    private DefaultTableModel ownCModel;
    private JTable ownCTable;
    private DefaultTableModel wekaCModel;
    private JTable wekaCTable;

    private DefaultTableModel ownIModel;
    private JTable ownITable;
    private DefaultTableModel wekaIModel;
    private JTable wekaITable;
    private DefaultTableModel metricsModel;
    private JTable metricsTable;

    private JLabel lImageName;
    private JLabel lClusterNum;

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
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 50;
        c.weighty = 50;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets.set(20, 10, 5, 10);

        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(
                new Dimension(
                        MainFrame.getFrameWidth(),
                        MainFrame.getFrameHeight() / 3
                )
        );
        bottomPanel.setLayout(new GridLayout(1,2));
        leftBottomPanel = new ChartPanel(imageChart);


        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setLayout(new GridBagLayout());

        rightBottomPanel.add(new JLabel("Plik:"), c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        lImageName = new JLabel();
        rightBottomPanel.add(lImageName, c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets.set(5, 10, 5, 10);
        rightBottomPanel.add(new JLabel("Liczba klastrów:"), c);

        c.gridx = 3;
        c.anchor = GridBagConstraints.WEST;
        lClusterNum = new JLabel();
        rightBottomPanel.add(lClusterNum, c);

        c.gridx = 4;
        c.anchor = GridBagConstraints.EAST;
        rightBottomPanel.add(new JLabel("Pokaż statystyki:"), c);

        c.gridx = 5;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        JComboBox<String> comboBox =  StatsComboBox.getJComboBox();
        comboBox.addItemListener(e->{
            cardLayout.show(statsPanel, e.getItem().toString());
        });
        rightBottomPanel.add(comboBox, c);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 6;
        c.gridheight = 2;

        createStatsPanel();
        rightBottomPanel.add(statsPanel, c);


        bottomPanel.add(leftBottomPanel);
        bottomPanel.add(rightBottomPanel);
    }

    private void createStatsPanel(){
        cardLayout = new CardLayout();
        statsPanel = new JPanel();
        statsPanel.setLayout(cardLayout);

        JPanel metricsPanel = new JPanel();
        metricsPanel.setLayout(new BorderLayout());

        propModel = new DefaultTableModel();
        propTable = new JTable(propModel);

        propModel.addColumn("Właściwość");
        propModel.addColumn("Implementacja");
        propModel.addColumn("Weka");

        propTable.setAutoCreateRowSorter(true);
        metricsPanel.add(new JScrollPane(propTable), BorderLayout.CENTER);


        JPanel clustersPanel  = new JPanel();
        clustersPanel.setLayout(new BorderLayout());

        ownCModel = new DefaultTableModel();
        ownCTable = new JTable(ownCModel);
        ownCTable.setAutoCreateRowSorter(true);

        ownCModel.addColumn("Lp.");
        ownCModel.addColumn("Współrzędne");
        ownCModel.addColumn("Rozmiar");
        ownCModel.addColumn("Kolor");

        wekaCModel = new DefaultTableModel();
        wekaCTable = new JTable(wekaCModel);
        wekaCTable.setAutoCreateRowSorter(true);

        wekaCModel.addColumn("Lp.");
        wekaCModel.addColumn("Współrzędne");
        wekaCModel.addColumn("Rozmiar");
        wekaCModel.addColumn("Kolor");
        metricsModel = new DefaultTableModel();
        metricsTable = new JTable(metricsModel);
        metricsModel.addColumn("Lp.");
        metricsModel.addColumn("Indeks Jaccard'a");
        metricsModel.addColumn("Współczynnik Dice'a");

        clustersPanel.add(new JScrollPane(ownCTable), BorderLayout.WEST);
        clustersPanel.add(new JScrollPane(metricsTable), BorderLayout.CENTER);
        clustersPanel.add(new JScrollPane(wekaCTable), BorderLayout.EAST);


        JPanel initialsPanel = new JPanel();
        initialsPanel.setLayout(new BorderLayout());
        ownIModel = new DefaultTableModel();
        ownITable = new JTable(ownIModel);
        ownITable.setAutoCreateRowSorter(true);

        ownIModel.addColumn("Lp.");
        ownIModel.addColumn("Współrzędne");
        ownIModel.addColumn("Kolor");

        wekaIModel = new DefaultTableModel();
        wekaITable = new JTable(wekaIModel);
        wekaITable.setAutoCreateRowSorter(true);

        wekaIModel.addColumn("Lp.");
        wekaIModel.addColumn("Współrzędne");
        wekaIModel.addColumn("Kolor");



        initialsPanel.add(new JScrollPane(ownITable), BorderLayout.WEST);
        initialsPanel.add(new JScrollPane(wekaITable), BorderLayout.EAST);


        statsPanel.add(metricsPanel, StatsComboBox.PROPERTIES.value);
        statsPanel.add(new JScrollPane(clustersPanel), StatsComboBox.CLUSTERS.value);
        statsPanel.add(new JScrollPane(initialsPanel), StatsComboBox.INITIAL.value);

    }
    public void fillInitialsTable(ArrayList<Point3D> dataSet1, ArrayList<Point3D> dataSet2){
        String color = "<html><body style=\"background-color:%s;\"> %s </body></html>";
        if(dataSet1.size() == dataSet2.size()){
            for (int i = 0; i < dataSet1.size();i++) {
                Point3D ds1C = dataSet1.get(i);
                Point3D ds2C = dataSet2.get(i);

                ownIModel.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        String.format(color, ds1C.toHex(), ds1C.toHex())
                });
                wekaIModel.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        String.format(color, ds2C.toHex(), ds2C.toHex())
                });
            }
        }
        else{
            for (int i = 0; i < dataSet1.size();i++) {
                Point3D ds1C = dataSet1.get(i);

                ownIModel.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        String.format(color, ds1C.toHex(), ds1C.toHex())
                });
            }
            for (int i = 0; i < dataSet2.size();i++) {
                Point3D ds2C = dataSet2.get(i);

                wekaIModel.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        String.format(color, ds2C.toHex(), ds2C.toHex())
                });
            }
        }
    }
    public void fillClustersTable(ArrayList<Cluster> dataSet1, ArrayList<Cluster> dataSet2){
        String color = "<html><body style=\"background-color:%s;\"> %s </body></html>";
        if(dataSet1.size() == dataSet2.size()){
            for (int i = 0; i < dataSet1.size();i++) {
                Cluster ds1C = dataSet1.get(i);
                Cluster ds2C = dataSet2.get(i);

                ownCModel.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        ds1C.getSize(),
                        String.format(color, ds1C.toHex(), ds1C.toHex())
                });
                wekaCModel.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        ds2C.getSize(),
                        String.format(color, ds2C.toHex(), ds2C.toHex())
                });
            }
        }
        else{
            for (int i = 0; i < dataSet1.size();i++) {
                Cluster ds1C = dataSet1.get(i);

                ownCModel.addRow(new Object[]{
                        i + 1,
                        ds1C.toString(),
                        ds1C.getSize(),
                        String.format(color, ds1C.toHex(), ds1C.toHex())
                });
            }
            for (int i = 0; i < dataSet2.size();i++) {
                Cluster ds2C = dataSet2.get(i);

                wekaCModel.addRow(new Object[]{
                        i + 1,
                        ds2C.toString(),
                        ds2C.getSize(),
                        String.format(color, ds2C.toHex(), ds2C.toHex())
                });            }
        }
    }
    public void fillClustersMetricsTable(double[] jaccardIndexes, double[] diceCoefs){
        int size = jaccardIndexes.length;

        for(int i = 0; i < size; i++){
            metricsModel.addRow(new Object[]{
                    i + 1,
                    Calculations.round(jaccardIndexes[i], 4),
                    Calculations.round(diceCoefs[i], 4)
            });
        }

    }
    public void addPropTableRow(Object val1, Object val2, Object val3){
        propModel.addRow(new Object[]{val1, val2, val3});
    }

    public void setImageLabel(BufferedImage image, Position position){
        ImageIcon imageIcon = null;
        double frameWidth = MainFrame.getFrameWidth() / 2.0;
        double frameHeight = MainFrame.getFrameHeight() * 2.0 / 3;

        int width = image.getWidth();
        int height = image.getHeight();

        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min((double) frameWidth / (width), (double) frameHeight / (height));
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

    public void setImageName(String imageName){
        this.imageName = imageName;
        this.lImageName.setText(imageName);
    }

    public void setImageChart(JFreeChart chart){
        imageChart = chart;
        leftBottomPanel.setChart(chart);
    }

    public void setClusterNum(Integer value){
        lClusterNum.setText(value.toString());
    }


    public enum Position{ LEFT, RIGHT }

    private enum StatsComboBox{
        PROPERTIES("Właściwości"),
        CLUSTERS("Klastry"),
        INITIAL("Punkty początkowe"),
        OTHERS("Inne");

        final String value;

        private StatsComboBox(String value){
            this.value = value;
        }

        public static JComboBox<String> getJComboBox(){
            return new JComboBox<>(new String[]{PROPERTIES.value, CLUSTERS.value,INITIAL.value, OTHERS.value});
        }
    }
}
