package pl.amrusb.util.ui.panels;

import pl.amrusb.Main;
import pl.amrusb.util.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;

public class MainPanel extends JPanel {

    public MainPanel(){
        final JLabel background = new JLabel();
        this.setLayout(new BorderLayout());

        String bodyPath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.file");
        String imagePath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.logo");
        File logo = new File(imagePath);
        String bodyBackground = null;
        try {
            FileInputStream f = new FileInputStream(bodyPath);
            int width = MainFrame.getFrameWidth() / 5;
            bodyBackground = String.format(new String(f.readAllBytes()), logo, width, width);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        CHtmlEditorPane ep = new CHtmlEditorPane(bodyBackground);

        ep.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ep.getCaret().setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        background.setPreferredSize(new Dimension(MainFrame.getFrameWidth(), MainFrame.getFrameHeight()));
        background.setText(bodyBackground);

        this.add(ep, BorderLayout.CENTER);
    }
}
