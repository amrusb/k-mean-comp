package pl.amrusb.util.actions;

import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SaveAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
        MainMenuBar.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String filePath = current.getFilePath();
        File output = new File(filePath);
        String fileName = current.getFileName();
        String formatName = fileName.substring(fileName.indexOf('.') + 1 );
        try{
            BufferedImage image;
            if(current.hasSegmentedImage()) {
                image = current.getSegmentedImage();
            }
            else{
                image = current.getOriginalImage();
            }

            ImageIO.write(image, formatName, output);
        }
        catch (IOException ex){
            JOptionPane.showMessageDialog(
                    MainMenuBar.getOwner(),
                    ex.getMessage(),
                    "Błąd zapisu obrazu",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }
}
