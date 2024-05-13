package pl.amrusb.util.actions;

import pl.amrusb.ui.MainFrame;
import pl.amrusb.ui.MainMenuBar;
import pl.amrusb.segm.ImageWidow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class UndoAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ImageWidow current = (ImageWidow) MainFrame.getTabbedPane().getSelectedComponent();
        BufferedImage image = current.getBfIOriginal();
        if(current.getBfISegmented() == null){
            current.changePanel(ImageWidow.BASIC_PANEL);
        }
        else{
            current.setBfISegmented(null);
            current.setlImage(image);
        }

        JMenuItem item = (JMenuItem) e.getSource();
        item.setEnabled(false);
        MainFrame.setTabTitle(current, false);
        MainMenuBar.enableAlgorithms(true);
        MainMenuBar.enableSave(false);
    }
}
