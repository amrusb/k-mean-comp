package pl.amrusb.util.timer;

public class Timer {
    private static Long start = null;
    private static Long elapsedTimeMs = null;

    public static void start() throws Exception {
        if(start == null){
            elapsedTimeMs = null;
            start = System.currentTimeMillis();
        }
        else throw new Exception("Timer już działa!");
    }

    public static Float stop() throws Exception {
        if(start != null) {
            elapsedTimeMs = System.currentTimeMillis() - start;
            start = null;
            return elapsedTimeMs / 1000F;
        }
        else throw new Exception("Timer nie został jeszcze uruchomiony!");
    }

    public static Float getResult() throws Exception {
        if(start == null && elapsedTimeMs != null) {
            return elapsedTimeMs / 1000F;
        }
        else throw new Exception("Timer nie został jeszcze uruchomiony!");
    }

}
