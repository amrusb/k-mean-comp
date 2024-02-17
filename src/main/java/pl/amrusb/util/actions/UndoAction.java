package pl.amrusb.util.actions;

import pl.amrusb.util.ui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class UndoAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        BufferedImage image = MainFrame.getImage();
        MainFrame.setSegmentedImage(null);

        MainFrame.setImageLabel(image);

        JMenuItem item = (JMenuItem) e.getSource();
        item.setEnabled(false);
    }
}
