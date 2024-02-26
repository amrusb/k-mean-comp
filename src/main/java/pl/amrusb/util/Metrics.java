package pl.amrusb.util;

public class Metrics {
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
