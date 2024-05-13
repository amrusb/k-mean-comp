package pl.amrusb.util.img;


import pl.amrusb.util.models.Pixel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageSaver {
    /**
     * Konwertuje tablicę pikseli na obiekt BufferedImage o podanych wymiarach.
     *
     * @param array  lista pikseli reprezentowana przez obiekt ArrayList<Pixel>
     * @param width  szerokość obrazu wynikowego
     * @param height wysokość obrazu wynikowego
     * @return obiekt BufferedImage zawierający przekonwertowany obraz
     */
    public static BufferedImage convertToBufferedImage(ArrayList<Pixel> array, int width, int height){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pixel_it = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, array.get(pixel_it++).getBinaryPixel());
            }
        }
        return outputImage;
    }

}
