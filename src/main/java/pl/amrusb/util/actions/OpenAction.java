package pl.amrusb.util.actions;

import pl.amrusb.util.img.ImageReader;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.ui.MainMenuBar;
import pl.amrusb.util.img.ImageFilter;
import pl.amrusb.segm.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel panel = new ImagePanel();

        JFileChooser imageChooser = new JFileChooser();
        imageChooser.setCurrentDirectory(new File("."));
        imageChooser.addChoosableFileFilter(new ImageFilter());
        imageChooser.setAcceptAllFileFilterUsed(false);

        int result = imageChooser.showOpenDialog(null);
        MainMenuBar.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        if(result == JFileChooser.APPROVE_OPTION){
            String fileName = imageChooser.getSelectedFile().getName();
            panel.setFileName(fileName);

            String filePath = imageChooser.getSelectedFile().getAbsolutePath();
            panel.setFilePath(filePath);
            ImageReader.setFilePath(filePath);
            panel.setOriginalImage(ImageReader.readImage());

            panel.setImageLabel(ImageReader.readImage());
            MainFrame.addTab(panel);
            MainFrame.changePanel();
            MainMenuBar.reload();
        }

        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }
}
