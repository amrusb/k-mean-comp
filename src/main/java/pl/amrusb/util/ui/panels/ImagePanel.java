package pl.amrusb.util.ui.panels;

import lombok.Getter;
import lombok.Setter;
import pl.amrusb.util.img.ImageRescaler;
import pl.amrusb.util.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel sluzyacy do wyswietlania obrazka
 */
@Getter
@Setter
public class ImagePanel extends JPanel {
    private final JLabel imageLabel = new JLabel();
    private BufferedImage originalImage = null;
    private BufferedImage rescaledImage = null;
    private BufferedImage segmentedImage = null;
    private String fileName;
    private String filePath;

    public ImagePanel(){
        setLayout(new BorderLayout());
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(imageLabel, BorderLayout.CENTER);
    }


    /**
     * Ustawia obraz jako ikonę etykiety w scrollPane
     * Jeżeli obraz jest za duży, zostaje przeskalowany do odpowiednich wymiarów
     * @param image obraz do ustawienia jako ikona etykiety
     */
    public void setImageLabel(BufferedImage image) {
        int frameWidth = MainFrame.getFrameWidth();
        int frameHeight = MainFrame.getFrameHeight();

        int width = image.getWidth();
        int height = image.getHeight();

        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min((double) frameWidth / (width), (double) frameHeight / (height));
            BufferedImage displayImage = ImageRescaler.rescaleImage(image, scale);
            ImageIcon imageIcon = new ImageIcon(displayImage);
            imageLabel.setIcon(imageIcon);
            setRescaledImage(displayImage);
        }
        else{
            ImageIcon imageIcon = new ImageIcon(image);
            imageLabel.setIcon(imageIcon);
        }
    }

    /**
     * Zwraca informacje o przechowywaniu oryginalego obrazu
     * @return (1) true jezeli obiekt image przechowuje obraz
     *         (2) false w przeciwnym wypadku*/
    public boolean hasImage(){
        return originalImage != null;
    }
    /**
     * Zwraca informacje o przechowywaniu przeskalowanego obrazu
     * @return (1) true jezeli obiekt image przechowuje przeskalowany
     *         (2) false w przeciwnym wypadku*/
    public boolean hasRescaledImage(){
        return rescaledImage != null;
    }
    /**
     * Zwraca informację o przechowywaniu obrazu po zastosowaniu algorytmu segmentacji
     * @return (1) true jezeli obiekt image po zastosowaniu algorytmu segmentacji
     *         (2) false w przeciwnym wypadku*/
    public boolean hasSegmentedImage() {return segmentedImage != null; }
}
