package pl.amrusb.util.math;

import pl.amrusb.algs.comb_opt.HungarianAlgorithm;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.models.Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClusterSorter {

    public static Map<AlgorithmsMetrics, ArrayList<Cluster>> hungarianAlgorithm(Map<AlgorithmsMetrics, ArrayList<Cluster>> clusters){
        ArrayList<Cluster> imp = clusters.get(AlgorithmsMetrics.IMP);
        ArrayList<Cluster> adapt = clusters.get(AlgorithmsMetrics.ADAPT);
        ArrayList<Cluster> weka = clusters.get(AlgorithmsMetrics.WEKA);

        imp.sort(new ClusterComparator());

        int k = imp.size();
        double[][] impAdapt = new double[k][k];
        double[][] impWeka = new double[k][k];

        for(int i = 0; i < k; i++){
            Cluster currentImp = imp.get(i);
            for(int j = 0; j < k; j++){
                Cluster currentAdapt = adapt.get(j);
                Cluster currentWeka = weka.get(j);

                impAdapt[j][i] = Calculations.calculateDistance(currentImp, currentAdapt);
                impWeka[j][i] = Calculations.calculateDistance(currentImp, currentWeka);
            }
        }

        int[][] adaptOrder = new HungarianAlgorithm(impAdapt).findOptimalAssignment();
        int[][] wekaOrder = new HungarianAlgorithm(impWeka).findOptimalAssignment();

        ArrayList<Cluster> newAdapt = new ArrayList<>(k);
        ArrayList<Cluster> newWeka = new ArrayList<>(k);

        for (int i = 0; i < k; i++) {
            newAdapt.add(adapt.get(adaptOrder[i][1]));
            newWeka.add(weka.get(wekaOrder[i][1]));
        }

        Map<AlgorithmsMetrics, ArrayList<Cluster>> result = new HashMap<>();
        result.put(AlgorithmsMetrics.IMP, imp);
        result.put(AlgorithmsMetrics.ADAPT, newAdapt);
        result.put(AlgorithmsMetrics.WEKA, newWeka);

        return result;
    }
}
