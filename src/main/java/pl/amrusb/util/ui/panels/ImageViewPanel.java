package pl.amrusb.util.ui.panels;

import lombok.Getter;
import lombok.SneakyThrows;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.ui.MainMenuBar;
import pl.amrusb.util.constants.AlgorithmsMetrics;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ImageViewPanel extends JPanel {
    @Getter
    private BufferedImage image;
    private final JLabel lImage;

    public ImageViewPanel(AlgorithmsMetrics name){
        this.setLayout(new BorderLayout());

        JLabel lAlgorithm = new JLabel(name.getValue(), JLabel.CENTER);
        lAlgorithm.setBorder(BorderFactory.createEtchedBorder());
        new EtchedBorder();
        lAlgorithm.setOpaque(true);

        lImage = new JLabel();
        lImage.setVerticalAlignment(SwingConstants.CENTER);
        lImage.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane spImage = new JScrollPane(lImage);
        spImage.setBackground(Color.WHITE);
        this.setBackground(Color.WHITE);
        spImage.getViewport().setBackground(Color.WHITE);

        this.add(lAlgorithm,BorderLayout.NORTH);
        this.add(spImage, BorderLayout.CENTER);


        lImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ImagePreviewer dialog = new ImagePreviewer();
                dialog.show(image, name.getValue());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lImage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }

    public ImageViewPanel(String name){
        this.setLayout(new BorderLayout());

        JLabel lAlgorithm = new JLabel(name, JLabel.CENTER);
        lAlgorithm.setBorder(BorderFactory.createEtchedBorder());
        new EtchedBorder();
        lAlgorithm.setOpaque(true);

        lImage = new JLabel();
        lImage.setVerticalAlignment(SwingConstants.CENTER);
        lImage.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane spImage = new JScrollPane(lImage);
        spImage.setBackground(Color.WHITE);
        this.setBackground(Color.WHITE);
        spImage.getViewport().setBackground(Color.WHITE);

        this.add(lAlgorithm,BorderLayout.NORTH);
        this.add(spImage, BorderLayout.CENTER);

        lImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ImagePreviewer dialog = new ImagePreviewer();
                dialog.show(image, name);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lImage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }

    public void setImage(String html){
        this.lImage.setText(html);
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }


    private class ImagePreviewer{
        @SneakyThrows
        private void show(BufferedImage image, String what){
            JDialog dMain = new JDialog();
            JLabel lImage = new JLabel();


            String htmlString = "<html><img src=\"file:%s\" width=\"%s\" height=\"%s\"></html>";
            int width = image.getWidth();
            int height = image.getHeight();
            double frameWidth = MainFrame.getFrameWidth();
            double frameHeight = MainFrame.getFrameHeight();

            if (width >= frameWidth || height >= frameHeight) {
                double scale = Math.min(frameWidth / (width), frameHeight / (height));
                width = (int)(width * scale);
                height = (int)(height * scale);
            }


            File stream;
            stream = File.createTempFile("show-img", ".jpg");
            ImageIO.write(image, "jpg", stream);
            htmlString = String.format(htmlString, stream , width, height);
            dMain.setTitle("Podgląd: " + what );
            dMain.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            dMain.setSize(width, height);
            dMain.setResizable(false);
            dMain.setLocationRelativeTo(MainMenuBar.getOwner());

            lImage.setText(htmlString);

            dMain.add(lImage);

            dMain.setVisible(true);

            dMain.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        dMain.dispose();
                    }
                }
            });
        }
    }
}
