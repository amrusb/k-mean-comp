package pl.amrusb.algs.rand;

import pl.amrusb.Main;

public class LCGenerator {
    private final int a;
    private final double m = Math.pow(2.0, 32.0);
    private final int c;

    private double value;

    public LCGenerator(long seed){
        a = Integer.parseInt(Main.getProperties().getProperty("algs.rand.a"));
        c = Integer.parseInt(Main.getProperties().getProperty("algs.rand.c"));
        if(seed % 2 == 0) seed += 1;
        this.value = seed;
    }
    /**
     * Generuje kolejną losową liczbę z rozkładu jednorodnego.
     *
     * @return wygenerowana liczba losowa z zakresu [0, 1)
     */
    public double nextDouble(){
        value = (a * value + c) % m;
        return value / m;
    }

}
