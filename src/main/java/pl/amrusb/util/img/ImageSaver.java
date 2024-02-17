package pl.amrusb.util.img;


import pl.amrusb.util.models.Pixel;
import pl.amrusb.util.ui.panels.BottomPanel;

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
        BottomPanel.setProgress(0);
        BottomPanel.setProgressMaximum(height*width);
        BottomPanel.setProgressLabel("Zapisywanie obrazu...");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                BottomPanel.incrementProgress();
                outputImage.setRGB(j, i, array.get(pixel_it++).getBinaryPixel());
            }
        }
        return outputImage;
    }
    /**
     * Konwertuje tablicę dwuwymiarowych pikseli na obiekt BufferedImage o podanych wymiarach.
     *
     * @param array  dwuwymiarowa tablica pikseli reprezentowana przez obiekt Pixel[][]
     * @param width  szerokość obrazu wynikowego
     * @param height wysokość obrazu wynikowego
     * @return obiekt BufferedImage zawierający przekonwertowany obraz
     */
    public static BufferedImage convertToBufferedImage(Pixel[][] array, int width, int height){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BottomPanel.setProgress(0);
        BottomPanel.setProgressMaximum(height*width);
        BottomPanel.setProgressLabel("Zapisywanie obrazu...");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                BottomPanel.incrementProgress();
                outputImage.setRGB(j, i, array[j][i].getBinaryPixel());
            }
        }
        return outputImage;
    }
    /**
     * Konwertuje tablicę dwuwymiarowych wartości pikseli na obiekt BufferedImage o podanych wymiarach.
     * Wartości pikseli są interpretowane jako składowe koloru RGB.
     *
     * @param array  dwuwymiarowa tablica wartości pikseli reprezentowana przez obiekt double[][]
     * @param width  szerokość obrazu wynikowego
     * @param height wysokość obrazu wynikowego
     * @return obiekt BufferedImage zawierający przekonwertowany obraz
     */
    public static BufferedImage convertToBufferedImage(double[][] array, int width, int height){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BottomPanel.setProgress(0);
        BottomPanel.setProgressMaximum(height*width);
        BottomPanel.setProgressLabel("Zapisywanie obrazu...");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                BottomPanel.incrementProgress();
                int pixel = (255 << 24) | ((int)array[j][i] << 16) | ((int)array[j][i] << 8) | (int)array[j][i];
                outputImage.setRGB(j, i, pixel);
            }
        }
        return outputImage;
    }
}
