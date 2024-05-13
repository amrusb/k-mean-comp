package pl.amrusb.ui;

import lombok.SneakyThrows;
import pl.amrusb.Main;
import pl.amrusb.util.ui.panels.CHtmlEditorPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;

public class MainPanel extends JPanel {

    @SneakyThrows
    public MainPanel(){
        final JLabel background = new JLabel();
        this.setLayout(new BorderLayout());

        String bodyPath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.file");
        String imagePath = Main.getROOT_PATH() + Main.getProperties().getProperty("app.background.logo");
        File logo = new File(imagePath);
        FileInputStream f = new FileInputStream(bodyPath);
        int width = MainFrame.getFrameWidth() / 5;
        String bodyBackground= String.format(new String(f.readAllBytes()), logo, width, width);
        f.close();

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
