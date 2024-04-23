package pl.amrusb.util.ui.panels;

import lombok.Getter;
import lombok.Setter;
import pl.amrusb.util.img.ImageRescaler;
import pl.amrusb.util.ui.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
public class ImagePanel extends JPanel {
    public static final String BASIC_PANEL = "basicPanel";
    public static final String COMPARE_PANEL = "comparePanel";

    private final JLabel imageLabel = new JLabel();
    private BufferedImage originalImage = null;
    private BufferedImage rescaledImage = null;
    private BufferedImage segmentedImage = null;
    private String fileName;
    private String filePath;

    private JPanel basicPanel;
    private ComparePanel comparePanel;
    private static CardLayout cardLayout = null;

    public ImagePanel(){
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        createBasicPanel();
        comparePanel = new ComparePanel();

        this.add(basicPanel, BASIC_PANEL);
        this.add(comparePanel, COMPARE_PANEL);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }

    private void createBasicPanel(){
        basicPanel  = new JPanel();
        basicPanel.setLayout(new BorderLayout());
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        basicPanel.add(imageLabel, BorderLayout.CENTER);
    }

    public void changePanel(String panel){
        cardLayout.show(this, panel);
    }
    /**
     * Ustawia obraz jako ikonę etykiety w scrollPane.
     * Jeżeli obraz jest za duży, zostaje przeskalowany do odpowiednich wymiarów
     * @param image obraz do ustawienia jako ikona etykiety
     */
    public void setImageLabel(BufferedImage image){
        int frameWidth = MainFrame.getFrameWidth();
        int frameHeight = MainFrame.getFrameHeight();

        int width = image.getWidth();
        int height = image.getHeight();
        String htmlString = "<html><img src=\"file:%s\" width=\"%s\" height=\"%s\"></html>";

        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min((double) frameWidth / (width), (double) frameHeight / (height));
            width = (int)(width * scale);
            height = (int)(height * scale);

            AtomicReference<BufferedImage> displayImage = new AtomicReference<>();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(() -> displayImage.set(ImageRescaler.rescaleImage(image, scale)));


            executor.shutdown();

            try {
                future.get();
                rescaledImage = displayImage.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        File stream;
        try {
            stream = File.createTempFile("show-img", ".jpg");
            ImageIO.write(image, "jpg", stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        htmlString = String.format(htmlString, stream, width, height);
        imageLabel.setText(htmlString);
    }

    /**
     * Zwraca informacje o przechowywaniu przeskalowanego obrazu
     * @return <ol>
     *     <li>true wtw, gdy obiekt image przechowuje przeskalowany</li>
     *     <li>false w przeciwnym wypadku</li>
     * </ol>*/
    public boolean hasRescaledImage(){
        return rescaledImage != null;
    }
    /**
     * Zwraca informację o przechowywaniu obrazu po zastosowaniu algorytmu segmentacji
     * @return <ol>
     *     <li>true wtw, gdy obiekt image po zastosowaniu algorytmu segmentacji</li>
     *     <li>false w przeciwnym wypadku</li>
     * </ol>*/
    public boolean hasSegmentedImage() {return segmentedImage != null; }
}
