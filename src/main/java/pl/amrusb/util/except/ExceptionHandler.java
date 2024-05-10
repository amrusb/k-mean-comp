package pl.amrusb.util.except;


public class ExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        new ExceptionDialog(null, "Błąd", e.getMessage(), e.getStackTrace());
        e.printStackTrace();
    }
}
