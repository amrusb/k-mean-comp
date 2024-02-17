package pl.amrusb.util.ui;

import lombok.Getter;
import pl.amrusb.Main;
import pl.amrusb.util.img.ImageRescaler;
import pl.amrusb.util.ui.panels.BottomPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;

public class MainFrame extends JFrame {
    private static final Double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final Double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 16);
    private static final Font BASIC_FONT = new Font("SansSerif", Font.PLAIN, 14);

    private static final JLabel imageLabel = new JLabel();
    private static BufferedImage originalImage = null;
    @Getter
    private static BufferedImage rescaledImage = null;
    @Getter
    private static BufferedImage segmentedImage = null;

    public MainFrame(){
        setTitle("Segmentacja obrazu");
        Integer width = (int)(SCREEN_WIDTH * 4 / 5);
        Integer height = (int)(SCREEN_HEIGHT * 4 / 5);
        Integer x = (int)(SCREEN_WIDTH - width) / 2;
        Integer y = (int)(SCREEN_HEIGHT - height) / 2;

        setBounds(x, y, width, height);
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(imageLabel);
        imageLabel.setFont(HEADER_FONT);

        String bodyPath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.file");
        String imagePath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.logo");
        File logo = new File(imagePath);

        String bodyBackground = null;
        try {
            FileInputStream f = new FileInputStream(bodyPath);
            bodyBackground = MessageFormat.format(new String(f.readAllBytes()), logo.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        imageLabel.setText(bodyBackground);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        getContentPane().add(scrollPane);

        setJMenuBar(new MainMenuBar(this));

        add(new BottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Ustawia obraz jako ikonę etykiety w scrollPane
     * Jeżeli obraz jest zaduży, zostaje przeskalowany do odpowiednich wymiarów
     * @param image obraz do ustawienia jako ikona etykiety
     */
    public static void setImageLabel(BufferedImage image) {
        imageLabel.setText("");
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
    public static void setImage(BufferedImage newImage){ originalImage = newImage;}
    public static BufferedImage getImage(){
        return originalImage;
    }

    public static void setRescaledImage(BufferedImage newImage){
        rescaledImage = newImage;
    }

    public static void setSegmentedImage(BufferedImage segmentedImage) {MainFrame.segmentedImage = segmentedImage;}

    /**
     * Zwraca informacje o przechowywaniu oryginalego obrazu
     * @return (1) true jezeli obiekt image przechowuje obraz
     *         (2) false w przeciwnym wypadku*/
    public static boolean hasImage(){
        return originalImage != null;
    }
    /**
     * Zwraca informacje o przechowywaniu przeskalowanego obrazu
     * @return (1) true jezeli obiekt image przechowuje przeskalowany
     *         (2) false w przeciwnym wypadku*/
    public static boolean hasRescaledImage(){
        return rescaledImage != null;
    }
    /**
     * Zwraca informację o przechowywaniu obrazu po zastosowaniu algorytmu segmentacji
     * @return (1) true jezeli obiekt image po zastosowaniu algorytmu segmentacji
     *         (2) false w przeciwnym wypadku*/
    public static boolean hasSegmentedImage() {return segmentedImage != null; }

    /**
     * Zwraca domyślną szerokość okna programu
     * @return szerokość okna programu
     */
    public static int getFrameWidth(){
        return (int)(SCREEN_WIDTH * 4 / 5);
    }
    /**
     * Zwraca domyślną wysokość okna programu
     * @return wysokość okna programu
     */
    public static int getFrameHeight(){
        return (int)(SCREEN_HEIGHT * 4 / 5);
    }
    /**
     * Zwraca podstawową czcionkę używaną przez program
     * @return podstawowa czcionka
     */
    public static Font getBasicFont() {return BASIC_FONT;}

}