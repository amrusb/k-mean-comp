package pl.amrusb.ui;


import lombok.Getter;
import pl.amrusb.segm.KMeansAction;
import pl.amrusb.segm.comp.CompareAction;
import pl.amrusb.util.actions.*;
import pl.amrusb.util.constants.AlgorithmsMetrics;

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
    private static final JMenuItem wekaItem = new JMenuItem("<html>K-means <small>Weka</small></html>");
    private static final JMenuItem adaptiveItem = new JMenuItem("<html>Adaptive k-means</html>");
    private static final JMenuItem compItem = new JMenuItem("Porównanie");
    private static final JMenuItem undo = new JMenuItem("Cofnij");
    @Getter
    private static JFrame owner;


    public MainMenuBar(JFrame frame){
        owner = frame;
        imageMenu.add(openItem);

        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        openItem.addActionListener(new OpenAction());

        imageMenu.add(saveItem);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        saveItem.addActionListener(new SaveAction(false));

        imageMenu.add(saveAsItem);
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK));
        saveAsItem.addActionListener(new SaveAction(true));
        add(imageMenu);

        segmentationMenu.add(kmeanItem);
        kmeanItem.addActionListener(new KMeansAction(AlgorithmsMetrics.IMP));

        segmentationMenu.add(wekaItem);
        wekaItem.addActionListener(new KMeansAction(AlgorithmsMetrics.WEKA));

        segmentationMenu.add(adaptiveItem);
        adaptiveItem.addActionListener(new KMeansAction(AlgorithmsMetrics.ADAPT));

        segmentationMenu.addSeparator();

        segmentationMenu.add(compItem);
        compItem.addActionListener(new CompareAction());

        segmentationMenu.addSeparator();
        segmentationMenu.add(undo);
        undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
        undo.setEnabled(false);
        undo.addActionListener(new UndoAction());

        add(segmentationMenu);

        programMenu.add(exitItem);
        exitItem.addActionListener(e-> System.exit(0));
        add(programMenu);

        reload();
    }

    /**
     * Metoda ustawia dostepnosc elementow paska menu w zależności od tego czy użytkownik wybrał plik
     * */
    public static void reload(){
        if(MainFrame.getTabbedPane().getTabCount() > 0){
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

    public static void enableAlgorithms(Boolean value){
        kmeanItem.setEnabled(value);
        wekaItem.setEnabled(value);
        adaptiveItem.setEnabled(value);
        compItem.setEnabled(value);
    }
    public static void enableUndo(Boolean value){
        undo.setEnabled(value);
    }

    public static void enableSave(Boolean value) {
        saveItem.setEnabled(value);
        saveAsItem.setEnabled(value);
    }
}

