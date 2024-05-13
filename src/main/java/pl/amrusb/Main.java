package pl.amrusb;

import lombok.Getter;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.util.except.ExceptionHandler;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    @Getter
    private static final Properties properties = new Properties();
    @Getter
    private static final String ROOT_PATH = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        String propPath = ROOT_PATH + "application.properties";
        try{
            properties.load(new FileInputStream(propPath));
            final MainFrame frame = new MainFrame();
            EventQueue.invokeLater(()->{
                frame.setVisible(true);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}