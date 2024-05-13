package pl.amrusb.util.img;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import pl.amrusb.util.constants.FileType;
import pl.amrusb.util.models.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static pl.amrusb.util.constants.FileType.JPG;
import static pl.amrusb.util.constants.FileType.PNG;

@RequiredArgsConstructor
public class ImageReader {
    private final String filePath;
    private File file;
    private BufferedImage image;

    @SneakyThrows
    public BufferedImage readImage(){
        file = new File(filePath);
        image = ImageIO.read(file);
        return image;
    }

    @SneakyThrows
    public FileType getFileType(){
        byte[] magicNumbers = new byte[4];
        FileInputStream is = new FileInputStream(file);
        if(is.read(magicNumbers) == -1){
            throw new RuntimeException("Błąd w odczycie typu pliku.");
        }

        if(magicNumbers[0] == (byte) 0xFF && magicNumbers[1] == (byte) 0xD8 &&
                magicNumbers[2] == (byte) 0xFF && magicNumbers[3] == (byte) 0xE0){
            return JPG;
        }
        else if(magicNumbers[0] == (byte) 0x89 && magicNumbers[1] == (byte) 0x50 &&
                magicNumbers[2] == (byte) 0x4E && magicNumbers[3] == (byte) 0x47){
            return PNG;
        }
        else throw new RuntimeException("Nieobsługiwany format!");
    }

    /**
     * Konwertuje obraz w postaci BufferedImage na ArrayListę obiektów Pixel.
     * @param image obiekt BufferedImage reprezentujący obraz
     * @return ArrayLista obiektów Pixel reprezentujących piksele obrazu
     */
    public static ArrayList<Pixel> getPixelArray(BufferedImage image){
        int image_width = image.getWidth();
        int image_height = image.getHeight();
        ArrayList<Pixel> array = new ArrayList<>(image_width * image_height);
        for (int i = 0; i < image_height; i++) {
            for (int j = 0; j < image_width; j++) {
                array.add(new Pixel(image.getRGB(j, i)));
            }
        }
        return array;
    }

}
