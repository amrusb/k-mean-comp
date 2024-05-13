package pl.amrusb.util.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Point3D {
    private int X;
    private int Y;
    private int Z;

    public Point3D(){
        X = 0;
        Y = 0;
        Z = 0;
    }
    @Override
    public String toString() {
        return "(" + X+", "+Y+", "+Z +")";
    }

    @Override
    protected Object clone()  {
        return new Point3D(X,Y,Z);
    }
}
