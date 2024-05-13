package pl.amrusb.segm;

import lombok.Getter;
import lombok.Setter;
import pl.amrusb.segm.comp.CompareWindow;
import pl.amrusb.util.img.ImageRescaler;
import pl.amrusb.ui.MainFrame;

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
public class ImageWidow extends JPanel {
    public static final String BASIC_PANEL = "basicPanel";
    public static final String COMPARE_PANEL = "comparePanel";
    private Boolean isEdited;

    private final JLabel lImage = new JLabel();
    private BufferedImage bfIOriginal = null;
    private BufferedImage bfIRescaled = null;
    private BufferedImage bfISegmented = null;
    private String fileName;
    private String filePath;


    private JPanel pBasics;
    private CompareWindow cwCompare;
    private CardLayout cardLayout = null;

    public ImageWidow(){
        isEdited = false;
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        createBasicPanel();
        cwCompare = new CompareWindow();

        this.add(pBasics, BASIC_PANEL);
        this.add(cwCompare, COMPARE_PANEL);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }

    private void createBasicPanel(){
        pBasics = new JPanel();
        pBasics.setLayout(new BorderLayout());
        lImage.setVerticalAlignment(SwingConstants.CENTER);
        lImage.setHorizontalAlignment(SwingConstants.CENTER);
        pBasics.add(lImage, BorderLayout.CENTER);
    }

    public void changePanel(String panel){
        cardLayout.show(this, panel);
        if(panel.equals(COMPARE_PANEL)){
            cwCompare.setOriginalImage(bfIOriginal);
        }
    }
    /**
     * Ustawia obraz jako ikonę etykiety w scrollPane.
     * Jeżeli obraz jest za duży, zostaje przeskalowany do odpowiednich wymiarów
     * @param image obraz do ustawienia jako ikona etykiety
     */
    public void setlImage(BufferedImage image){
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
                bfIRescaled = displayImage.get();
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
        lImage.setText(htmlString);
    }

    /**
     * Zwraca informacje o przechowywaniu przeskalowanego obrazu
     * @return <ol>
     *     <li>true wtw, gdy obiekt image przechowuje przeskalowany</li>
     *     <li>false w przeciwnym wypadku</li>
     * </ol>*/
    public boolean hasRescaledImage(){
        return bfIRescaled != null;
    }
    /**
     * Zwraca informację o przechowywaniu obrazu po zastosowaniu algorytmu segmentacji
     * @return <ol>
     *     <li>true wtw, gdy obiekt image po zastosowaniu algorytmu segmentacji</li>
     *     <li>false w przeciwnym wypadku</li>
     * </ol>*/
    public boolean hasSegmentedImage() {return bfISegmented != null; }
}
