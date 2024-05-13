package pl.amrusb.util.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.models.Cluster;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class MetricsBarChart {
    private static final String TITLE = "Metryki";
    private static final String JACCARD_LBL = "Indeks Jaccard'a";
    private static final String DICE_LBL = "Współczynnik Dice'a";
    private static final String SILHOUETTE = "Wynik Silhouette";

    private static final String[] ALGS = {"Impementacja", "Adaptive", "Weka"};
    private static final String[] ALGS_C = {"Implementacja - Adaptive", "Implementacja - Weka", "Adaptive - Weka"};

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

    private static void customizeChart(JFreeChart chart){
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.getRenderer().setSeriesPaint(0, new Color(245, 213, 71));
        plot.getRenderer().setSeriesPaint(1, new Color(119, 51,68));

        TextTitle title = new TextTitle(TITLE, new Font("SansSerif", Font.BOLD, 14));
        chart.setTitle(title);
    }

    private static DefaultCategoryDataset createDataset(Map<AlgorithmsMetrics, ArrayList<Double>> data){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        ArrayList<Double> first = data.get(AlgorithmsMetrics.IMP);
        String firstLabel = AlgorithmsMetrics.IMP.getValue();
        ArrayList<Double> second;
        String secondLabel;
        ArrayList<Double> third;
        String thirdLabel;

        if(first == null){
            firstLabel = AlgorithmsMetrics.IMP_ADAPT.getValue();
            secondLabel = AlgorithmsMetrics.IMP_WEKA.getValue();
            thirdLabel = AlgorithmsMetrics.ADAPT_WEKA.getValue();

            first = data.get(AlgorithmsMetrics.IMP_ADAPT);
            second = data.get(AlgorithmsMetrics.IMP_WEKA);
            third = data.get(AlgorithmsMetrics.ADAPT_WEKA);
        }
        else {
            secondLabel = AlgorithmsMetrics.ADAPT.getValue();
            thirdLabel = AlgorithmsMetrics.WEKA.getValue();
            second = data.get(AlgorithmsMetrics.ADAPT);
            third =data.get(AlgorithmsMetrics.WEKA);
        }

        for(int i = 0; i <  first.size(); i++){
            dataset.addValue(first.get(i), String.valueOf(i + 1), firstLabel);
            dataset.addValue(second.get(i), String.valueOf(i + 1), secondLabel);
            dataset.addValue(third.get(i), String.valueOf(i + 1), thirdLabel);
        }
        return dataset;
    }

    private static void customizeChart(JFreeChart chart, ArrayList<Cluster> c){
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        for (int i = 0; i < c.size(); i++){
            plot.getRenderer().setSeriesPaint(i, new Color(c.get(i).getX(), c.get(i).getY(), c.get(i).getZ()));
        }

        TextTitle title = new TextTitle(TITLE, new Font("SansSerif", Font.BOLD, 14));
        chart.setTitle(title);
    }

    public static JFreeChart create(Map<AlgorithmsMetrics, ArrayList<Double>> silhouette, String title, ArrayList<Cluster> c){
        DefaultCategoryDataset dataset =  createDataset(silhouette);

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
        customizeChart(chart, c);
        chart.setTitle(title);
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
