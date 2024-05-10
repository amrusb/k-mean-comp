package pl.amrusb.segm.comp;

import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.models.Point3D;
import pl.amrusb.util.ui.panels.TablePane;
import pl.amrusb.util.ui.table.CTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InitialCentroidPanel extends JPanel {
    private final CTable ownITable;
    private final CTable adaptITable;
    private final CTable wekaITable;

    public InitialCentroidPanel(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        ownITable = new CTable();

        ownITable.addColumn("Lp.","Współrzędne","Kolor");
        ownITable.setColumnWidth("30,100,50");

        adaptITable = new CTable();
        adaptITable.addColumn("Lp.","Współrzędne","Kolor");
        adaptITable.setColumnWidth("30,100,50");

        wekaITable = new CTable();
        wekaITable.addColumn("Lp.","Współrzędne","Kolor");
        wekaITable.setColumnWidth("30,100,50");

        TablePane tpOwnI = new TablePane(AlgorithmsMetrics.IMP.getValue(), ownITable);
        TablePane tpAdaptI = new TablePane(AlgorithmsMetrics.ADAPT.getValue(), adaptITable);
        TablePane tpWekaI = new TablePane(AlgorithmsMetrics.WEKA.getValue(), wekaITable);

        this.add(tpOwnI);
        this.add(tpAdaptI);
        this.add(tpWekaI);
    }

    public void fillTables(ArrayList<Point3D> dataSet1, ArrayList<Point3D> dataSet2, ArrayList<Point3D> dataSet3){
        for (int i = 0; i < dataSet1.size();i++) {
            Point3D ds1C = dataSet1.get(i);
            Point3D ds2C = dataSet2.get(i);
            Point3D ds3C = dataSet3.get(i);

            ownITable.addRow(new Object[]{
                    i + 1,
                    ds1C.toString(),
                    new Color(ds1C.getX(), ds1C.getY(), ds1C.getZ())
            });
            adaptITable.addRow(new Object[]{
                    i + 1,
                    ds2C.toString(),
                    new Color(ds2C.getX(), ds2C.getY(), ds2C.getZ())
            });
            wekaITable.addRow(new Object[]{
                    i + 1,
                    ds3C.toString(),
                    new Color(ds3C.getX(), ds3C.getY(), ds3C.getZ())
            });

        }

        ownITable.setRenderer();
        adaptITable.setRenderer();
        wekaITable.setRenderer();
    }
}
