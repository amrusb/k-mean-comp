package pl.amrusb.util.ui.panels;

import lombok.Getter;
import pl.amrusb.util.constants.AlgorithmsMetrics;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageViewPanel extends JPanel {
    @Getter
    private BufferedImage image;
    private final JLabel lImage;

    public ImageViewPanel(AlgorithmsMetrics name){
        this.setLayout(new BorderLayout());

        JLabel lAlgorithm = new JLabel(name.getValue(), JLabel.CENTER);
        lAlgorithm.setBorder(BorderFactory.createEtchedBorder());
        new EtchedBorder();
        lAlgorithm.setOpaque(true);

        lImage = new JLabel();
        lImage.setVerticalAlignment(SwingConstants.CENTER);
        lImage.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane spImage = new JScrollPane(lImage);
        spImage.setBackground(Color.WHITE);
        this.setBackground(Color.WHITE);
        spImage.getViewport().setBackground(Color.WHITE);

        this.add(lAlgorithm,BorderLayout.NORTH);
        this.add(spImage, BorderLayout.CENTER);
    }

    public ImageViewPanel(String name){
        this.setLayout(new BorderLayout());

        JLabel lAlgorithm = new JLabel(name, JLabel.CENTER);
        lAlgorithm.setBorder(BorderFactory.createEtchedBorder());
        new EtchedBorder();
        lAlgorithm.setOpaque(true);

        lImage = new JLabel();
        lImage.setVerticalAlignment(SwingConstants.CENTER);
        lImage.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane spImage = new JScrollPane(lImage);
        spImage.setBackground(Color.WHITE);
        this.setBackground(Color.WHITE);
        spImage.getViewport().setBackground(Color.WHITE);

        this.add(lAlgorithm,BorderLayout.NORTH);
        this.add(spImage, BorderLayout.CENTER);
    }

    public void setImage(String html){
        this.lImage.setText(html);
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }
}
