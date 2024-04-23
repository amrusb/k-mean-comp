package pl.amrusb.util.ui.panels;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;


public class CChartPanel extends ChartPanel {
    private static JFreeChart chart;
    public CChartPanel() {
        super(chart);
    }
}
