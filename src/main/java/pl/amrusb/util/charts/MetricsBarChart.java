package pl.amrusb.util.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import pl.amrusb.util.models.Cluster;

import java.awt.*;
import java.util.ArrayList;

public class MetricsBarChart {
    private static final String TITLE = "Metryki";
    private static final String JACCARD_LBL = "Indeks Jaccard'a";
    private static final String DICE_LBL = "Współczynnik Dice'a";
    private static final String SILHOUETTE = "Wynik Silhouette";

    private static final String[] ALGS = {"Impementacja", "Adaptive", "Weka"};
    private static final String[] ALGS_C = {"Implementacja - Adaptive", "Implementacja - Weka", "Adaptive - Weka"};

    private static DefaultCategoryDataset createDataset(double[] jaccard, double[] dice){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int k = jaccard.length;
        for (int i = 0; i < k; i++) {
            dataset.addValue(jaccard[i], Integer.toString(i + 1), JACCARD_LBL);
            dataset.addValue(dice[i], Integer.toString(i + 1), DICE_LBL);
        }

        return dataset;
    }

    private static DefaultCategoryDataset createDataset(ArrayList<Double> jaccard, ArrayList<Double> dice){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i < jaccard.size(); i++){
            dataset.addValue(jaccard.get(i),ALGS_C[i], JACCARD_LBL);
            dataset.addValue(dice.get(i),ALGS_C[i], DICE_LBL);
        }
        return dataset;
    }

    private static DefaultCategoryDataset createDataset(ArrayList<Double> silhouette){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i <  silhouette.size(); i++){
            dataset.addValue(silhouette.get(i),ALGS[i], SILHOUETTE);
        }
        return dataset;
    }

    private static void customizeChart(JFreeChart chart, ArrayList<Cluster> c1, ArrayList<Cluster> c2){
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        int k = c1.size();
        for (int i = 0; i < k; i++) {
            int r = (c1.get(i).getX() + c2.get(i).getX()) / 2;
            int g = (c1.get(i).getY() + c2.get(i).getY()) / 2;
            int b = (c1.get(i).getZ() + c2.get(i).getZ()) / 2;

            plot.getRenderer().setSeriesPaint(i, new Color(r,g,b));
        }

        TextTitle title = new TextTitle(TITLE, new Font("SansSerif", Font.BOLD, 14));
        chart.setTitle(title);
    }

    private static void customizeChart(JFreeChart chart){
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.getRenderer().setSeriesPaint(0, new Color(245, 213, 71));
        plot.getRenderer().setSeriesPaint(1, new Color(119, 51,68));

        TextTitle title = new TextTitle(TITLE, new Font("SansSerif", Font.BOLD, 14));
        chart.setTitle(title);
    }

    public static JFreeChart create(double[] jaccard, double[] dice, ArrayList<Cluster> c1, ArrayList<Cluster> c2){
        DefaultCategoryDataset dataset =  createDataset(jaccard, dice);

        JFreeChart chart = ChartFactory.createBarChart(
                TITLE,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        customizeChart(chart, c1, c2);
        return chart;
    }

    public static JFreeChart create(ArrayList<Double> jaccard, ArrayList<Double> dice){
        DefaultCategoryDataset dataset = createDataset(jaccard,dice);

        JFreeChart chart = ChartFactory.createBarChart(
                TITLE,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        customizeChart(chart);
        chart.setTitle("");
        return chart;
    }

    public static JFreeChart create(ArrayList<Double> silhouette){
        DefaultCategoryDataset dataset = createDataset(silhouette);

        JFreeChart chart = ChartFactory.createBarChart(
                TITLE,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        customizeChart(chart);
        chart.setTitle("");
        return chart;
    }

}
