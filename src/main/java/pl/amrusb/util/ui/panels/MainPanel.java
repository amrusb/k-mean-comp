package pl.amrusb.util.ui.panels;

import pl.amrusb.Main;
import pl.amrusb.util.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;

public class MainPanel extends JPanel {

    public MainPanel(){
        final JLabel background = new JLabel();
        final JLabel logoLabel = new JLabel();
        this.setLayout(new BorderLayout());

        String bodyPath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.file");
        String imagePath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.logo");
        File logo = new File(imagePath);
        String bodyBackground = null;
        try {
            FileInputStream f = new FileInputStream(bodyPath);
            bodyBackground = new String(f.readAllBytes());
            logoLabel.setText("<html><img src=\"file:" + logo.toString() + "\" width=\"350\" height=\"350\"</html>");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        background.setPreferredSize(new Dimension(MainFrame.getFrameWidth(), MainFrame.getFrameHeight()));
        background.setText(bodyBackground);
        background.setVerticalAlignment(SwingConstants.CENTER);
        background.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(logoLabel, BorderLayout.NORTH);
        this.add(background, BorderLayout.CENTER);
    }
}
