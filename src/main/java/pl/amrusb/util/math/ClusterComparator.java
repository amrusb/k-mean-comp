package pl.amrusb.util.math;

import pl.amrusb.util.models.Point3D;

import java.util.Comparator;

public class ClusterComparator implements Comparator<Point3D> {
    @Override
    public int compare(Point3D p1, Point3D p2) {
        Point3D p0 = new Point3D(0,0,0);
        double d1 = Calculations.calculateDistance(p0,p1);
        double d2 = Calculations.calculateDistance(p0, p2);
        return Double.compare(d1, d2);
    }
}
