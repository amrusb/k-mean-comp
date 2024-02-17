package pl.amrusb.util.actions;

import pl.amrusb.util.ui.MainFrame;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class UndoAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
        BufferedImage image = current.getOriginalImage();
        current.setSegmentedImage(null);

        current.setImageLabel(image);

        JMenuItem item = (JMenuItem) e.getSource();
        item.setEnabled(false);
    }
}
