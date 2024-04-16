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

public class ClusterSizesBarChart {
    private static final String TITLE = "Rozmiar klastrów";
    private static final String ADAPTIVE_LBL = "Adaptive";
    private static final String IMPLEMENTATION_LBL = "Implementacja";
    private static final String WEKA_LBL = "Weka";

    private static DefaultCategoryDataset createDataset(ArrayList<Cluster> c1, ArrayList<Cluster> c2, ArrayList<Cluster> c3){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int k = c1.size();
        for (int i = 0; i < k; i++) {
            dataset.addValue(c1.get(i).getSize(), IMPLEMENTATION_LBL , Integer.toString(i + 1));
            dataset.addValue(c2.get(i).getSize(), ADAPTIVE_LBL, Integer.toString(i+1));
            dataset.addValue(c3.get(i).getSize(), WEKA_LBL, Integer.toString(i + 1));
        }

        return dataset;
    }

    private static void customizeChart(JFreeChart chart){
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.getRenderer().setSeriesPaint(0, new Color(0, 48, 161));
        plot.getRenderer().setSeriesPaint(1, new Color(0,161,0));
        plot.getRenderer().setSeriesPaint(2, new Color(161, 48, 0));

        TextTitle title = new TextTitle(TITLE, new Font("SansSerif", Font.BOLD, 14));
        chart.setTitle(title);
    }

    public static JFreeChart create(ArrayList<Cluster> c1, ArrayList<Cluster> c2, ArrayList<Cluster> c3){
        DefaultCategoryDataset dataset =  createDataset(c1, c2, c3);

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

        return chart;
    }

}
