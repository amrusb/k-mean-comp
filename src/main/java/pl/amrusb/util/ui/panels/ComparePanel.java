package pl.amrusb.util.ui.panels;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import pl.amrusb.util.img.ImageRescaler;
import pl.amrusb.util.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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

    private JLabel lImageName;
    private JLabel lClusterNum;
    private JLabel lRsme;
    private JLabel lJackard;
    private JLabel lDice;
    private JLabel lOwnTime;
    private JLabel lWekaTime;

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
        c.weightx = 100;
        c.weighty = 100;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
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

        lImageName = new JLabel();
        rightBottomPanel.add(lImageName, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets.set(5, 10, 5, 10);
        rightBottomPanel.add(new JLabel("<html><b>Liczba klastrów:</b></html>"), c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        lClusterNum = new JLabel("X");
        rightBottomPanel.add(lClusterNum, c);
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        rightBottomPanel.add(new JLabel("<html><b>RMSE:</b></html>"), c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        lRsme = new JLabel("X");
        rightBottomPanel.add(lRsme, c);

        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.EAST;
        rightBottomPanel.add(new JLabel("<html><b>Indeks Jaccard'a:</b></html>"), c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        lJackard = new JLabel("X");
        rightBottomPanel.add(lJackard, c);

        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.EAST;
        rightBottomPanel.add(new JLabel("<html><b>Współczynnik Dice’a:</b></html>"), c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        lDice = new JLabel("X");
        rightBottomPanel.add(lDice, c);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.CENTER;
        rightBottomPanel.add(new JLabel("<html><b>Czas trwania algorytmów:</b></html>"), c);
        c.gridy = 6;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets.set(5, 10, 20, 10);
        rightBottomPanel.add(new JLabel("<html><b>Implementacja:</b></html>"), c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        lOwnTime = new JLabel("XXX");
        rightBottomPanel.add(lOwnTime, c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.EAST;
        rightBottomPanel.add(new JLabel("<html><b>Weka:</b></html>"), c);
        c.gridx = 4;
        c.anchor = GridBagConstraints.WEST;
        lWekaTime = new JLabel("XXX");
        rightBottomPanel.add(lWekaTime, c);

        bottomPanel.add(leftBottomPanel);
        bottomPanel.add(rightBottomPanel);
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

    public enum Position{ LEFT, RIGHT }
}
