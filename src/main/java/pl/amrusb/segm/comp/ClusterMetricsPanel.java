package pl.amrusb.segm.comp;

import lombok.Getter;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.constants.MetricsTypes;
import pl.amrusb.util.ui.panels.TablePane;
import pl.amrusb.util.ui.table.CTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

@Getter
public class ClusterMetricsPanel extends JPanel {
    private final CTable tbShiluette;
    private final CTable tbImpAdapt;
    private final CTable tbImpWeka;
    private final CTable tbAdaptWeka;

    public ClusterMetricsPanel(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        tbShiluette = new CTable();
        tbShiluette.addColumn("Lp.", AlgorithmsMetrics.IMP.getValue(), AlgorithmsMetrics.ADAPT.getValue(), AlgorithmsMetrics.WEKA.getValue());
        tbShiluette.setColumnWidth("30,100,100,100");

        tbImpAdapt = new CTable();
        tbImpAdapt.addColumn("Lp.", MetricsTypes.JACCARD.getValue(), MetricsTypes.DICE.getValue());
        tbImpAdapt.setColumnWidth("30,100,100");

        tbImpWeka = new CTable();
        tbImpWeka.addColumn("Lp.", MetricsTypes.JACCARD.getValue(), MetricsTypes.DICE.getValue());
        tbImpWeka.setColumnWidth("30,100,100");

        tbAdaptWeka = new CTable();
        tbAdaptWeka.addColumn("Lp.", MetricsTypes.JACCARD.getValue(), MetricsTypes.DICE.getValue());
        tbAdaptWeka.setColumnWidth("30,100,100");


        this.add(new TablePane(MetricsTypes.SIHLOUETTE.getValue(), tbShiluette));
        this.add(new TablePane(AlgorithmsMetrics.IMP_ADAPT.getValue(), tbImpAdapt));
        this.add(new TablePane(AlgorithmsMetrics.IMP_WEKA.getValue(), tbImpWeka));
        this.add(new TablePane(AlgorithmsMetrics.ADAPT_WEKA.getValue(), tbAdaptWeka));
    }

    public void fillTables(Map<AlgorithmsMetrics, ArrayList<Double>> dataSet1, Map<AlgorithmsMetrics, ArrayList<Double>> dataSet2, Map<AlgorithmsMetrics, ArrayList<Double>> dataSet3) {

        ArrayList<Double> impAdaptJaccard = dataSet1.get(AlgorithmsMetrics.IMP_ADAPT);
        ArrayList<Double> impAdaptDice = dataSet2.get(AlgorithmsMetrics.IMP_ADAPT);
        ArrayList<Double> impWekaJaccard = dataSet1.get(AlgorithmsMetrics.IMP_WEKA);
        ArrayList<Double> impWekaDice = dataSet2.get(AlgorithmsMetrics.IMP_WEKA);
        ArrayList<Double> adaptWekaJaccard = dataSet1.get(AlgorithmsMetrics.ADAPT_WEKA);
        ArrayList<Double> adaptWekaDice = dataSet2.get(AlgorithmsMetrics.ADAPT_WEKA);
        ArrayList<Double> impSil = dataSet3.get(AlgorithmsMetrics.IMP);
        ArrayList<Double> adaptSil = dataSet3.get(AlgorithmsMetrics.ADAPT);
        ArrayList<Double> wekaSil = dataSet3.get(AlgorithmsMetrics.WEKA);
        int clusterNum = wekaSil.size();
        for (int i = 0; i < clusterNum; i++) {
            tbImpAdapt.addRow(new Object[]{
                    i + 1,
                    impAdaptJaccard.get(i),
                    impAdaptDice.get(i)
            });
            tbImpWeka.addRow(new Object[]{
                    i + 1,
                    impWekaJaccard.get(i),
                    impWekaDice.get(i)
            });
            tbAdaptWeka.addRow(new Object[]{
                    i + 1,
                    adaptWekaJaccard.get(i),
                    adaptWekaDice.get(i)
            });

            tbShiluette.addRow(new Object[]{
                    i+1,
                    impSil.get(i),
                    adaptSil.get(i),
                    wekaSil.get(i)
            });
        }

        tbShiluette.setRenderer();
        tbImpAdapt.setRenderer();
        tbImpWeka.setRenderer();
        tbAdaptWeka.setRenderer();
    }
}