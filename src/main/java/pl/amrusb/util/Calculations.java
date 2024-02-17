package pl.amrusb.util;

import pl.amrusb.util.models.Point3D;

public class Calculations {
    /**
     * Oblicza kwadrat odległości euklidesowej między dwoma punktami w trójwymiarowej przestrzeni.
     *
     * @param p1 pierwszy punkt
     * @param p2 drugi punkt
     * @return kwadrat odległości między punktami
     */
    public static double calculateDistanceSquared(Point3D p1, Point3D p2){
        int d_x = p1.getX() - p2.getX();
        int d_y = p1.getY() - p2.getY();
        int d_z = p1.getZ() - p2.getZ();

        return d_x*d_x+d_y*d_y+d_z*d_z;
    }
    /**
     * Oblicza odległość euklidesową między dwoma punktami w trójwymiarowej przestrzeni.
     *
     * @param p1 pierwszy punkt
     * @param p2 drugi punkt
     * @return odległość między punktami
     */
    public static double calculateDistance(Point3D p1, Point3D p2){
        int d_x = p1.getX() - p2.getX();
        int d_y = p1.getY() - p2.getY();
        int d_z = p1.getZ() - p2.getZ();

        return Math.sqrt(d_x*d_x+d_y*d_y+d_z*d_z);
    }
}
