package pl.amrusb.util.actions;

import pl.amrusb.util.img.ImageReader;
import pl.amrusb.util.ui.panels.BottomPanel;
import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.MainMenuBar;
import pl.amrusb.util.ImageFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser imageChooser = new JFileChooser();
        imageChooser.setCurrentDirectory(new File("."));
        imageChooser.addChoosableFileFilter(new ImageFilter());
        imageChooser.setAcceptAllFileFilterUsed(false);

        int result = imageChooser.showOpenDialog(null);
        MainMenuBar.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(result == JFileChooser.APPROVE_OPTION){
            MainFrame.setRescaledImage(null);
            MainFrame.setImage(null);
            MainFrame.setSegmentedImage(null);
            BottomPanel.setDurationInfoVisible(false);
            BottomPanel.setFileNameVisible(true);
            BottomPanel.clear();

            String fileName = imageChooser.getSelectedFile().getName();
            BottomPanel.setFileName(fileName);
            ImageReader.setFileName(fileName);

            String filePath = imageChooser.getSelectedFile().getAbsolutePath();
            ImageReader.setFilePath(filePath);
            MainFrame.setImage(ImageReader.readImage());

            MainFrame.setImageLabel(MainFrame.getImage());
        }
        MainMenuBar.reload();
        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }
}
