package pl.amrusb.util.img;

import pl.amrusb.util.models.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageReader {
    private static String filePath;
    /**
     * Ustawia ścieżkę do pliku, z którego zostanie odczytany obraz.
     * @param filePath ścieżka do pliku
     */

    public static void setFilePath(String filePath) {
        ImageReader.filePath = filePath;
    }
    /**
     * Odczytuje obraz z pliku i zwraca go jako obiekt BufferedImage.
     * @return obiekt BufferedImage reprezentujący odczytany obraz
     */
    public static BufferedImage readImage() throws RuntimeException{
        File file;
        BufferedImage image;
        try{
            file = new File(filePath);
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
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
