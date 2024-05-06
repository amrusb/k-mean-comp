package pl.amrusb.util.img;

import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.models.Point3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageReader {
    private static String filePath;
    private static String fileName;
    /**
     * Ustawia nazwę pliku, z którego zostanie odczytany obraz.
     * @param fileName nazwa pliku
     */

    public static void setFileName(String fileName) { ImageReader.fileName = fileName;}
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
    public static BufferedImage readImage(){
        File file;
        BufferedImage image = null;
        try{
            file = new File(filePath);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
        ArrayList<Pixel> array = new ArrayList<Pixel>(image_width * image_height);
        for (int i = 0; i < image_height; i++) {
            for (int j = 0; j < image_width; j++) {
                array.add(new Pixel(image.getRGB(j, i)));
            }
        }
        return array;
    }
    /**
     * Konwertuje obraz w postaci BufferedImage na dwuwymiarową tablicę obiektów Pixel.
     * @param image obiekt BufferedImage reprezentujący obraz
     * @return dwuwymiarowa tablica obiektów Pixel reprezentujących piksele obrazu
     */
    public static Pixel[][] get2DPixelArray(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        Pixel[][] array = new Pixel[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                array[j][i] = new Pixel(image.getRGB(j, i));
            }
        }
        return array;
    }

    public static Double getGrayScale(Point3D cluster){
        return 0.299 * cluster.getX() + 0.587 * cluster.getY()  + 0.114 * cluster.getZ();
    }
}
