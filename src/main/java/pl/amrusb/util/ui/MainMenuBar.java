package pl.amrusb.util.ui;


import lombok.Getter;
import pl.amrusb.util.actions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


public class MainMenuBar extends JMenuBar {
    private static final JMenu programMenu = new JMenu("Program");
    private static final JMenuItem exitItem = new JMenuItem("Wyjście");
    private static final JMenu imageMenu = new JMenu("Plik");
    private static final JMenuItem openItem = new JMenuItem("Otwórz");
    private static final JMenuItem saveItem = new JMenuItem("Zapisz");
    private static final JMenuItem saveAsItem = new JMenuItem("Zapisz jako");
    private static final JMenu segmentationMenu = new JMenu("Segmentacja");
    private static final JMenuItem kmeanItem = new JMenuItem("K-means");
    private static final JMenuItem undo = new JMenuItem("Cofnij");

    @Getter
    private static JFrame owner;


    public MainMenuBar(JFrame frame){
        owner = frame;
        imageMenu.add(openItem);
        imageMenu.setFont(MainFrame.getBasicFont());
        openItem.setFont(MainFrame.getBasicFont());
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        openItem.addActionListener(new OpenAction());

        imageMenu.add(saveItem);
        saveItem.setFont(MainFrame.getBasicFont());
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        saveItem.addActionListener(new SaveAction());

        imageMenu.add(saveAsItem);
        saveAsItem.setFont(MainFrame.getBasicFont());
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        saveAsItem.addActionListener(new SaveAsAction());
        add(imageMenu);

        segmentationMenu.add(kmeanItem);
        kmeanItem.addActionListener(new KMeansAction());
        segmentationMenu.setFont(MainFrame.getBasicFont());
        kmeanItem.setFont(MainFrame.getBasicFont());

        segmentationMenu.addSeparator();

        segmentationMenu.add(undo);
        undo.setFont(MainFrame.getBasicFont());
        undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
        undo.setEnabled(false);
        undo.addActionListener(new UndoAction());

        add(segmentationMenu);

        programMenu.setFont(MainFrame.getBasicFont());
        programMenu.add(exitItem);
        exitItem.addActionListener(e-> System.exit(0));
        exitItem.setFont(MainFrame.getBasicFont());
        add(programMenu);

        reload();
    }

    /**
     * Metoda ustawia dostepnosc elementow paska menu w zależności od tego czy użytkownik wybrał plik
     * */
    public static void reload(){
        if(MainFrame.hasImage()){
            saveItem.setEnabled(true);
            saveAsItem.setEnabled(true);
            segmentationMenu.setEnabled(true);
        }
        else{
            saveItem.setEnabled(false);
            saveAsItem.setEnabled(false);
            segmentationMenu.setEnabled(false);
        }
    }

}

