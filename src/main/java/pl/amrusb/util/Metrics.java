package pl.amrusb.util;

import pl.amrusb.util.models.Cluster;

import java.util.ArrayList;

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

        result = intercept / (sum - intercept);

        return  result;
    }

    /**
     * Metoda służy do obliczana indeksu Jaccard'a dla każdego z klastrów
     * @param assign1 lista przynależności pikseli ze zbioru 1
     * @param assign2 lista przynależności pikseli ze zbioru 2
     * @param clusterNum ilość klastrów
     * @return lista wartości indeksu dla poszczególnych klastrów
     */
    public static double[] JaccardIndex(int[] assign1, int[] assign2, int clusterNum){
        int[] sums = new int[clusterNum];
        double[] intercepts = new double[clusterNum];
        double[] results = new double[clusterNum];


        for (int i = 0; i < assign1.length; i++) {
            if(assign1[i] == assign2[i]){
                intercepts[assign1[i]]++;
            }
            sums[assign1[i]]++;
            sums[assign2[i]]++;
        }

        for (int i = 0; i < results.length; i++) {
            results[i] = intercepts[i] / (sums[i] - intercepts[i]);
        }

        return  results;
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

    /**
     * Metoda oblicza wielkość współczynnika Sorensen-Dice dla każdego z klastrów
     * @param assign1 lista przynależności pikseli ze zbioru 1
     * @param assign2 lista przynależności pikseli ze zbioru 2
     * @param clusterNum ilość klastrów
     * @return lista wartości współczynnika dla poszczególnych klastrów
     */
    public static double[] SorensenDiceCoefficient(int[] assign1, int[] assign2, int clusterNum){

        int[] sums = new int[clusterNum];
        double[] intercepts = new double[clusterNum];
        double[] results = new double[clusterNum];


        for (int i = 0; i < assign1.length; i++) {
            if(assign1[i] == assign2[i]){
                intercepts[assign1[i]]++;
            }
            sums[assign1[i]]++;
            sums[assign2[i]]++;
        }

        for (int i = 0; i < results.length; i++) {
            results[i] = (2 * intercepts[i]) / sums[i];
        }

        return  results;
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
