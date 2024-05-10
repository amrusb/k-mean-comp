package pl.amrusb.segm.comp;

import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.ui.panels.TablePane;
import pl.amrusb.util.ui.table.CTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClusterPanel extends JPanel {
    private final CTable ownCTable;
    private final CTable adaptCTable;
    private final CTable wekaCTable;

    public ClusterPanel(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        ownCTable = new CTable();

        ownCTable.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
        ownCTable.setColumnWidth("30,100,100,50");

        adaptCTable = new CTable();
        adaptCTable.addColumn("Lp.", "Współrzędne","Rozmiar","Kolor");
        adaptCTable.setColumnWidth("30,100,100,50");

        wekaCTable = new CTable();

        wekaCTable.addColumn("Lp.","Współrzędne","Rozmiar","Kolor");
        wekaCTable.setColumnWidth("30,100,100,50");

        TablePane tpOwnC = new TablePane(AlgorithmsMetrics.IMP.getValue(), ownCTable);
        TablePane tpAdaptC = new TablePane(AlgorithmsMetrics.ADAPT.getValue(), adaptCTable);
        TablePane tpWekaC = new TablePane(AlgorithmsMetrics.WEKA.getValue(), wekaCTable);

        this.add(tpOwnC);
        this.add(tpAdaptC);
        this.add(tpWekaC);
    }

    public void fillTables(ArrayList<Cluster> dataSet1, ArrayList<Cluster> dataSet2, ArrayList<Cluster> dataSet3){
        for (int i = 0; i < dataSet1.size();i++) {
            Cluster ds1C = dataSet1.get(i);
            Cluster ds2C = dataSet2.get(i);
            Cluster ds3C = dataSet3.get(i);

            ownCTable.addRow(new Object[]{
                    i + 1,
                    ds1C.toString(),
                    ds1C.getSize(),
                    new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
            });
            adaptCTable.addRow(new Object[]{
                    i + 1,
                    ds2C.toString(),
                    ds2C.getSize(),
                    new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
            });
            wekaCTable.addRow(new Object[]{
                    i + 1,
                    ds3C.toString(),
                    ds3C.getSize(),
                    new Color(ds3C.getX(), ds3C.getY(), ds3C.getZ())
            });
        }

        ownCTable.setRenderer();
        adaptCTable.setRenderer();
        wekaCTable.setRenderer();
    }
}