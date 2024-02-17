package pl.amrusb.util.models;

import lombok.Getter;

@Getter
public class Cluster extends Point3D {
    private int size = 0;

    public Cluster(Pixel pixel){
        super(pixel.getR(),pixel.getG(),pixel.getB());
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
}
