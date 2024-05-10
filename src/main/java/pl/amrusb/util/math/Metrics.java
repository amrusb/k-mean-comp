package pl.amrusb.util.math;

import pl.amrusb.util.models.Cluster;
import pl.amrusb.util.models.Pixel;

import java.util.ArrayList;

public class Metrics {
    /**
     * Metoda służy do obliczana indeksu Jaccard'a
     * @param assign1 lista przynależności pikseli ze zbioru 1
     * @param assign2 lista przynależności pikseli ze zbioru 2
     * @return wartość indeksu Jaccard'a
     */
    public static double JaccardIndex(int[] assign1, int[] assign2){
        double result;
        double intercept = 0.0;
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
    public static ArrayList<Double> JaccardIndex(int[] assign1, int[] assign2, int clusterNum){
        int[] sums = new int[clusterNum];
        double[] intercepts = new double[clusterNum];
        ArrayList<Double> results = new ArrayList<>();


        for (int i = 0; i < assign1.length; i++) {
            if(assign1[i] == assign2[i]){
                intercepts[assign1[i]]++;
            }
            sums[assign1[i]]++;
            sums[assign2[i]]++;
        }

        for (int i = 0; i < clusterNum; i++) {
            results.add(Calculations.round(intercepts[i] / (sums[i] - intercepts[i]),4));
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
        double result;
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
    public static ArrayList<Double> SorensenDiceCoefficient(int[] assign1, int[] assign2, int clusterNum){

        int[] sums = new int[clusterNum];
        double[] intercepts = new double[clusterNum];
        ArrayList<Double> results = new ArrayList<>();


        for (int i = 0; i < assign1.length; i++) {
            if(assign1[i] == assign2[i]){
                intercepts[assign1[i]]++;
            }
            sums[assign1[i]]++;
            sums[assign2[i]]++;
        }

        for (int i = 0; i < clusterNum; i++) {
            results.add(Calculations.round((2 * intercepts[i]) / sums[i], 4));
        }

        return  results;
    }

    public static Double SilhouetteScore(
            int clusterIndex,
            ArrayList<Cluster> clusters,
            ArrayList<Pixel> image,
            int[] assignments
    ){
        Cluster cluster = clusters.get(clusterIndex);
        // closest cluster
        double minDist = Double.MAX_VALUE;
        int closestIndex = -1;
        for (int i = 0; i < clusters.size() ; i++) {
            if(i == clusterIndex) continue;
            Cluster current = clusters.get(i);
            double distance = Calculations.calculateDistance(cluster, current);
            if(distance < minDist){
                minDist = distance;
                closestIndex = i;
            }
        }

        // intra-cluster-distance
        double a = 0.0;
        int size = 0;
        for (int i = 0; i < assignments.length; i++) {
            if(assignments[i] == clusterIndex){
                Pixel current = image.get(i);
                a += Calculations.calculateDistance(cluster, current);
                size++;
            }
        }
        a /= size;

        //inter-cluster-distance
        double b = 0.0;
        size = 0;
        for (int i = 0; i < assignments.length; i++) {
            if(assignments[i] == closestIndex){
                Pixel current = image.get(i);
                b += Calculations.calculateDistance(cluster, current);
                size++;
            }
        }
        b /= size;

        return (b-a) / (Math.max(a, b));
    }

    public static Double SilhouetteScore(
            ArrayList<Cluster> clusters,
            ArrayList<Pixel> image,
            int[] assignments
    ){
        double sum = 0.0;

        for (int i = 0; i < clusters.size(); i++) {
            sum += SilhouetteScore(i, clusters,image, assignments);
        }

        return  sum / clusters.size();
    }
}
