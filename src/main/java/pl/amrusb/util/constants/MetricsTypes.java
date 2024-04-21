package pl.amrusb.util.constants;

import lombok.Getter;

public enum MetricsTypes {
    DICE("Współczynnik Dice'a"),
    JACCARD("Indeks Jaccard'a"),
    SIHLOUETTE("Wynik Sihlouette");

    @Getter
    final String value;
    MetricsTypes(String value){
        this.value = value;
    }
}
