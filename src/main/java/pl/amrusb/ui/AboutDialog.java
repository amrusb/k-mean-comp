package pl.amrusb.ui;

import lombok.SneakyThrows;
import pl.amrusb.Main;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.FileInputStream;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class AboutDialog{

    @SneakyThrows
    public static void show(){
        JEditorPane pHtml = new JEditorPane();
        JDialog dMain = new JDialog();
        dMain.setTitle("O programie");
        Icon icon = UIManager.getIcon("OptionPane.questionIcon");
        dMain.setIconImage(((ImageIcon) icon).getImage());
        dMain.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dMain.setSize(440, 400);
        dMain.setResizable(false);
        dMain.setLocationRelativeTo(MainMenuBar.getOwner());
        dMain.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        String path = Main.getROOT_PATH() + Main.getProperties().getProperty("app.about.text");
        FileInputStream f = new FileInputStream(path);
        String content = new String(f.readAllBytes());

        pHtml.setContentType("text/html");
        pHtml.setText(content);
        pHtml.setEditable(false);

        pHtml.addHyperlinkListener(AboutDialog::hyperlinkUpdate);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 3;
        c.weighty = 80;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(15, 15,0, 15);

        JScrollPane sp = new JScrollPane(pHtml);
        sp.getVerticalScrollBar().setValue(21);
        sp.setPreferredSize(new Dimension(410, 800));
        dMain.add(sp, c);

        JLabel lCopyright = new JLabel("<html>\u00a9 Bartosz Surma (amrusb) 2024 </html>");
        lCopyright.setFont(new Font("SansSerif", Font.PLAIN, 10));
        c.gridy = 5;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 15,0, 15);
        dMain.add(lCopyright,c);
        c.gridy = 6;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 15,5, 15);

        JButton bOk = new JButton("OK");
        bOk.addActionListener(l-> dMain.dispose());
        dMain.add(bOk, c);
        dMain.setVisible(true);
    }

    @SneakyThrows
    private static void hyperlinkUpdate(HyperlinkEvent hle) {
        if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(hle.getURL().toURI());
        }
    }
}
