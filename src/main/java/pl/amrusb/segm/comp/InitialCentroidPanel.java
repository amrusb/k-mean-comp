package pl.amrusb.segm.comp;

import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.models.Point3D;
import pl.amrusb.util.ui.panels.TablePane;
import pl.amrusb.util.ui.table.CTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InitialCentroidPanel extends JPanel {
    private final CTable tOwn;
    private final CTable tAdapt;
    private final CTable tWeka;

    public InitialCentroidPanel(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        tOwn = new CTable();

        tOwn.addColumn("Lp.","Współrzędne","Kolor");
        tOwn.setColumnWidth("30,100,50");

        tAdapt = new CTable();
        tAdapt.addColumn("Lp.","Współrzędne","Kolor");
        tAdapt.setColumnWidth("30,100,50");

        tWeka = new CTable();
        tWeka.addColumn("Lp.","Współrzędne","Kolor");
        tWeka.setColumnWidth("30,100,50");

        TablePane tpOwnI = new TablePane(AlgorithmsMetrics.IMP.getValue(), tOwn);
        TablePane tpAdaptI = new TablePane(AlgorithmsMetrics.ADAPT.getValue(), tAdapt);
        TablePane tpWekaI = new TablePane(AlgorithmsMetrics.WEKA.getValue(), tWeka);

        this.add(tpOwnI);
        this.add(tpAdaptI);
        this.add(tpWekaI);
    }

    public void fillTables(ArrayList<Point3D> dataSet1, ArrayList<Point3D> dataSet2, ArrayList<Point3D> dataSet3){
        for (int i = 0; i < dataSet1.size();i++) {
            Point3D ds1C = dataSet1.get(i);
            Point3D ds2C = dataSet2.get(i);
            Point3D ds3C = dataSet3.get(i);

            tOwn.addRow(new Object[]{
                    i + 1,
                    ds1C.toString(),
                    new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
            });
            tAdapt.addRow(new Object[]{
                    i + 1,
                    ds2C.toString(),
                    new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
            });
            tWeka.addRow(new Object[]{
                    i + 1,
                    ds3C.toString(),
                    new Color(ds3C.getX(), ds3C.getY(), ds3C.getZ())
            });

        }

        tOwn.setRenderer();
        tAdapt.setRenderer();
        tWeka.setRenderer();
    }
}
