package pl.amrusb.segm.comp;

import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.ui.panels.TablePane;
import pl.amrusb.util.ui.table.CTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClusterPanel extends JPanel {
    private final CTable tOwn;
    private final CTable tAdapt;
    private final CTable tWeka;

    public ClusterPanel(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        tOwn = new CTable();

        tOwn.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
        tOwn.setColumnWidth("30,100,100,50");

        tAdapt = new CTable();
        tAdapt.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
        tAdapt.setColumnWidth("30,100,100,50");

        tWeka = new CTable();

        tWeka.addColumn("Lp.","Współrzędne","Rozmiar","Kolor");
        tWeka.setColumnWidth("30,100,100,50");

        TablePane tpOwnC = new TablePane(AlgorithmsMetrics.IMP.getValue(), tOwn);
        TablePane tpAdaptC = new TablePane(AlgorithmsMetrics.ADAPT.getValue(), tAdapt);
        TablePane tpWekaC = new TablePane(AlgorithmsMetrics.WEKA.getValue(), tWeka);

        this.add(tpOwnC);
        this.add(tpAdaptC);
        this.add(tpWekaC);
    }

    public void fillTables(ArrayList<Cluster> dataSet1, ArrayList<Cluster> dataSet2, ArrayList<Cluster> dataSet3){
        for (int i = 0; i < dataSet1.size();i++) {
            Cluster ds1C = dataSet1.get(i);
            Cluster ds2C = dataSet2.get(i);
            Cluster ds3C = dataSet3.get(i);

            tOwn.addRow(new Object[]{
                    i + 1,
                    ds1C.toString(),
                    ds1C.getSize(),
                    new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
            });
            tAdapt.addRow(new Object[]{
                    i + 1,
                    ds2C.toString(),
                    ds2C.getSize(),
                    new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
            });
            tWeka.addRow(new Object[]{
                    i + 1,
                    ds3C.toString(),
                    ds3C.getSize(),
                    new Color(ds3C.getX(), ds3C.getY(), ds3C.getZ())
            });
        }

        tOwn.setRenderer();
        tAdapt.setRenderer();
        tWeka.setRenderer();
    }
}