package pl.amrusb.util.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
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
    private static final String MSELabel = "MSE";
    private static final String RMSELabel = "RMSE";

    private static DefaultCategoryDataset createDataset(double[] jaccard, double[] dice){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int k = jaccard.length;
        for (int i = 0; i < k; i++) {
            dataset.addValue(jaccard[i], Integer.toString(i + 1), JACCARD_LBL);
            dataset.addValue(dice[i], Integer.toString(i + 1), DICE_LBL);
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

        TextTitle title = new TextTitle(TITLE, new Font("Verdana", Font.BOLD, 14));
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


}
