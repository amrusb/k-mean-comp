package pl.amrusb;

import lombok.Getter;
import pl.amrusb.ui.MainFrame;

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
        String propPath = ROOT_PATH + "application.properties";
        try{
            properties.load(new FileInputStream(propPath));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        final MainFrame frame = new MainFrame();
        EventQueue.invokeLater(()->{
            frame.setVisible(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}