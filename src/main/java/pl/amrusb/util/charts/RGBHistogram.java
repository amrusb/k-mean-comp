package pl.amrusb.util.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.models.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RGBHistogram {
    private static final String TITLE = "Histogram RGB";

    private static XYDataset createDataset(ArrayList<Pixel> pixels){
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries rSeries = new XYSeries("Czerwony");
        XYSeries gSeries = new XYSeries("Zielony");
        XYSeries bSeries = new XYSeries("Niebieski");

        double[] rHistogram = new double[256];
        double[] gHistogram = new double[256];
        double[] bHistogram = new double[256];

        for (Pixel pixel: pixels) {
            int r = pixel.getR();
            int g = pixel.getG();
            int b = pixel.getB();

            rHistogram[r]++;
            gHistogram[g]++;
            bHistogram[b]++;
        }

        int size = pixels.size();

        for(int i = 0; i < 256; i++){
            rSeries.add(i, rHistogram[i] / size);
            gSeries.add(i, gHistogram[i] /  size);
            bSeries.add(i, bHistogram[i] /  size);
        }

        dataset.addSeries(rSeries);
        dataset.addSeries(gSeries);
        dataset.addSeries(bSeries);

        return dataset;
    }

    private static void customizeChart(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);

        renderer.setSeriesPaint(0,new Color(218,0,10));
        renderer.setSeriesPaint(1,new Color(29,160,62));
        renderer.setSeriesPaint(2, new Color(0,48,161));


        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setSeriesStroke(1, new BasicStroke(2.5f));
        renderer.setSeriesStroke(2, new BasicStroke(2.5f));

        plot.setRenderer(renderer);

        TextTitle title = new TextTitle(TITLE, new Font("SansSerif", Font.BOLD, 14));
        chart.setTitle(title);
    }

    public static JFreeChart create(BufferedImage image){
        ArrayList<Pixel> pixels = ImageReader.getPixelArray(image);
        XYDataset dataset = createDataset(pixels);

        JFreeChart chart = ChartFactory.createXYLineChart(
                null,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        customizeChart(chart);

        return chart;
    }
}
