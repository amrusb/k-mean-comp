package pl.amrusb.util;

import pl.amrusb.util.models.Pair;
import pl.amrusb.util.models.Point3D;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

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

    public static double round(double value, int position){
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(position, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Pair<Integer, Double> getMinValue(double[] row){
        int minIdx = 0;
        double minDst = row[0];

        for (int j = 1; j < row.length; j++) {
            if(row[j] < minDst){
                minIdx = j;
                minDst = row[j];
            }

        }
        return new Pair<>(minIdx, minDst);

    }
    private static final double INF = Double.POSITIVE_INFINITY;
    public static int[] findMinCostAssignment(double[][] cost) {
        int n = cost.length;
        double[] u = new double[n];
        double[] v = new double[n];
        int[] match = new int[n];
        int[] way = new int[n];
        Arrays.fill(match, -1); // Wszystkie wartości są inicjalnie -1, co wskazuje na brak przypisania.

        for (int i = 0; i < n; ++i) {
            int[] minIndex = new int[n];
            double[] min = new double[n];
            Arrays.fill(min, INF);
            int markedI = i, markedJ = -1, prevJ = -1;
            do {
                double delta = INF;
                for (int j = 0; j < n; ++j) {
                    if (min[j] == INF) {
                        double cur = cost[markedI][j] - u[markedI] - v[j];
                        if (cur < min[j]) {
                            min[j] = cur;
                            minIndex[j] = prevJ;
                        }
                        if (min[j] < delta) {
                            delta = min[j];
                            markedJ = j;
                        }
                    }
                }

                for (int j = 0; j < n; ++j) {
                    if (min[j] == INF) {
                        min[j] -= delta;
                    } else {
                        u[way[j]] += delta;
                        v[j] -= delta;
                    }
                }
                u[i] += delta;

                if (markedJ == -1) {
                    break; // Dodano zabezpieczenie przed próbą dostępu do tablicy match z nieprawidłowym indeksem
                }

                prevJ = markedJ;
                markedI = match[markedJ]; // Tutaj zgłasza błąd jeśli markedJ jest poza zakresem
            } while (markedI != -1);


            do {
                int tempJ = minIndex[markedJ];
                match[markedJ] = (tempJ == -1 ? i : match[tempJ]);
                markedJ = tempJ;
            } while (markedJ != -1);
        }
        return match;
    }

}
