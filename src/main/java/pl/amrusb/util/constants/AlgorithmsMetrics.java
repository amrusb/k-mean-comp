package pl.amrusb.util.constants;

import lombok.Getter;
@Getter
public enum AlgorithmsMetrics {
    IMP("Implementacja"),
    ADAPT("Adaptive"),
    WEKA("Weka"),
    IMP_ADAPT("Implementacja - Adaptive"),
    IMP_WEKA("Implemetnacja - Weka"),
    ADAPT_WEKA("Adaptive - Weka");
    
    final String value;

    AlgorithmsMetrics(String name){
        this.value = name;
    }


}
