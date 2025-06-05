package pl.amrusb.util.actions;

import lombok.SneakyThrows;
import pl.amrusb.util.constants.FileType;
import pl.amrusb.util.img.ImageReader;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.ui.MainMenuBar;
import pl.amrusb.util.img.ImageFilter;
import pl.amrusb.segm.ImageWidow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class OpenAction implements ActionListener {
    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        ImageWidow panel = new ImageWidow();

        JFileChooser imageChooser = new JFileChooser();
//        imageChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        imageChooser.setCurrentDirectory(new File("C:\\Users\\barto\\OneDrive\\Obrazy\\data-ser"));
        imageChooser.addChoosableFileFilter(new ImageFilter());
        imageChooser.setAcceptAllFileFilterUsed(false);

        int result = imageChooser.showOpenDialog(null);
        MainMenuBar.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        if(result == JFileChooser.APPROVE_OPTION){
            String fileName = imageChooser.getSelectedFile().getName();
            panel.setFileName(fileName);

            String filePath = imageChooser.getSelectedFile().getAbsolutePath();
            panel.setFilePath(filePath);
            ImageReader reader = new ImageReader(filePath);
            BufferedImage image = reader.readImage();
            FileType type = reader.getFileType();

            panel.setFileExt(type);
            panel.setBfIOriginal(image);
            panel.setlImage(image);

            MainFrame.addTab(panel);
            MainFrame.changePanel();
            MainMenuBar.reload();
        }

        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }
}
