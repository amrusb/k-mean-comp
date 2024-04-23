package pl.amrusb.util.constants;

import lombok.Getter;

public enum AlgorithmsMetrics {
    IMP("Implementacja"),
    ADAPT("Adaptive"),
    WEKA("Weka"),
    IMP_ADAPT("Implementacja - Adaptive"),
    IMP_WEKA("Implemetnacja - Weka"),
    ADAPT_WEKA("Adaptive - Weka");

    @Getter
    final String value;

    AlgorithmsMetrics(String name){
        this.value = name;
    }


}
