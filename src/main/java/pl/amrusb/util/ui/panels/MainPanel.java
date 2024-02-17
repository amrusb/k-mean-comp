package pl.amrusb.util.ui.panels;

import pl.amrusb.Main;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;

public class MainPanel extends JPanel {

    public MainPanel(){
        final JLabel background = new JLabel();

        String bodyPath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.file");
        String imagePath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.logo");
        File logo = new File(imagePath);
        String bodyBackground = null;
        try {
            FileInputStream f = new FileInputStream(bodyPath);
            bodyBackground = MessageFormat.format(new String(f.readAllBytes()), logo.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        background.setText(bodyBackground);
        background.setVerticalAlignment(SwingConstants.CENTER);
        background.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(background);
    }
}
