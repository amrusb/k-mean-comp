package pl.amrusb.util.actions;

import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;

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
        MainMenuBar.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String filePath = ImageReader.getFilePath();
        File output = new File(filePath);
        String fileName = ImageReader.getFileName();
        String formatName = fileName.substring(fileName.indexOf('.') + 1 );
        try{
            BufferedImage image;
            if(MainFrame.hasSegmentedImage()) {
                image = MainFrame.getSegmentedImage();
            }
            else{
                image = MainFrame.getImage();
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
            System.out.println(ex.getMessage());
        }
        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }
}
