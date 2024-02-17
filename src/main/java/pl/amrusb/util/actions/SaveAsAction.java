package pl.amrusb.util.actions;

import pl.amrusb.Main;
import pl.amrusb.util.ImageFilter;
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

public class SaveAsAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
        JFileChooser imageChooser = new JFileChooser();
        final String defaultExtension = Main.getProperties().getProperty("image.default.extension");
        String currentDirPath = Main.getProperties().getProperty("image.current.dir");
        imageChooser.setCurrentDirectory(new File(currentDirPath));
        imageChooser.addChoosableFileFilter(new ImageFilter());
        imageChooser.setAcceptAllFileFilterUsed(false);

        int result = imageChooser.showSaveDialog(null);
        MainMenuBar.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(result == JFileChooser.APPROVE_OPTION){
            String filePath = imageChooser.getSelectedFile().getAbsolutePath();
            String formatName;
            String fileName = imageChooser.getSelectedFile().getName();
            if(fileName.indexOf('.') != -1){
                formatName = fileName.substring(fileName.indexOf('.') + 1 );
            }
            else{
                formatName = defaultExtension.toLowerCase();
                filePath+= "." + formatName;
            }
            File output = new File(filePath);
            try{
                BufferedImage image;
                if(current.hasSegmentedImage()) image = current.getSegmentedImage();
                else image = current.getOriginalImage();

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
        }
        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }
}