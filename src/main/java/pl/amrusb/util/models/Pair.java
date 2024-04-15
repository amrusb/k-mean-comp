package pl.amrusb.util.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair<T,M>{
    private T first;
    private M second;
}
