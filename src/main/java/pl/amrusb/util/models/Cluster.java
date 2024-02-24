package pl.amrusb.util.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Cluster extends Point3D {
    private int size = 0;
    private final Integer ordinal;

    public Cluster(Pixel pixel){
        super(pixel.getR(),pixel.getG(),pixel.getB());
        ordinal = null;
    }
    public Cluster(Pixel pixel, int ordinal){
        super(pixel.getR(),pixel.getG(),pixel.getB());
        this.ordinal = ordinal;
    }
    public Cluster(int X, int Y, int Z, int size){
        super(X,Y,Z);
        this.size = size;
        this.ordinal = null;
    }
    public Cluster(int X, int Y, int Z, int size, int ordinal){
        super(X,Y,Z);
        this.size = size;
        this.ordinal = ordinal;
    }

    /**
     * Zwiększa rozmiar klastra o 1.
     */
    public void increaseSize(){
        size++;
    }
    /**
     * Zmniejsza rozmiar klastra o 1.
     */
    public void decreaseSize(){
        size--;
    }
    /**
     * Zwraca rozmiar klastra.
     *
     * @return rozmiar klastra
     */
    public int getSize(){
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Cluster c = (Cluster)obj;

        return super.getX() == c.getX() &&
                super.getY() == c.getY() &&
                super.getZ() == c.getZ();

    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Cluster clone() {
        return new Cluster(
                this.getX(),
                this.getY(),
                this.getZ(),
                getSize()
        );
    }
}
