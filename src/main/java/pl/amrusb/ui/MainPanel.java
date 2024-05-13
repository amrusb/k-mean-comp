package pl.amrusb.ui;

import lombok.SneakyThrows;
import pl.amrusb.Main;
import pl.amrusb.util.ui.panels.CHtmlEditorPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.InputStream;
import java.net.URL;

public class MainPanel extends JPanel {

    @SneakyThrows
    public MainPanel(){
        final JLabel background = new JLabel();
        this.setLayout(new BorderLayout());

        URL logo = Main.class.getResource(Main.getProperties().getProperty("app.background.logo"));
        InputStream f = Main.class.getResourceAsStream(Main.getProperties().getProperty("app.background.file"));
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
