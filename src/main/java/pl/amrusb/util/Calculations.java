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

    /**
     * Metoda służy do obliczana indeksu Jaccard'a
     * @param assign1 lista przynależności pikseli ze zbioru 1
     * @param assign2 lista przynależności pikseli ze zbioru 2
     * @return wartość indeksu Jaccard'a
     */
    public static Double JaccardIndex(int[] assign1, int[] assign2){
        Double result = null;
        Double intercept = 0.0;
        Integer sum = assign1.length + assign2.length;

        for (int i = 0; i < assign1.length; i++) {
            if(assign1[i] == assign2[i]){
                intercept++;
            }
        }

        result = intercept / sum;

        return  result;
    }

    /**
     * Metoda oblicza wielkość współczynnika Sorensen-Dice
     * @param assign1 lista przynależności pikseli ze zbioru 1
     * @param assign2 lista przynależności pikseli ze zbioru 2
     * @return wartość współczynnika
     */
    public static Double SorensenDiceCoefficient(int[] assign1, int[] assign2){
        Double result = null;
        Double intercept = 0.0;
        Integer sum = assign1.length + assign2.length;

        for (int i = 0; i < assign1.length; i++) {
            if(assign1[i] == assign2[i]){
                intercept++;
            }
        }

        result =  (2 * intercept) / sum;

        return  result;
    }

    public static Double MeanSquareError(int[] assign1, int[] assign2){
        Double result = 0.0;

        for (int i = 0; i < assign1.length; i++){
           double temp = assign1[i] - assign2[i];
           temp *= temp;
           result += temp;
        }

        return result / assign1.length;
    }
}
