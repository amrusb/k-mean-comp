package pl.amrusb.ui;

import lombok.Getter;
import pl.amrusb.segm.ImageWidow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;


public class MainFrame extends JFrame {
    private static final String MAIN_PANEL = "mainPanel";
    private static final String TABBED_PANEL = "tabbedPanel";
    private static Double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static Double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    @Getter
    private static final JTabbedPane tabbedPane = new JTabbedPane();
    private static JPanel body = null;
    private static CardLayout cardLayout = null;

    private static final ArrayList<ImageWidow> imagePanels = new ArrayList<>();

    public MainFrame(){
        setTitle("Segmentacja obrazu");
        int width = (int)(SCREEN_WIDTH * 4 / 5);
        int height = (int)(SCREEN_HEIGHT * 4 / 5);
        int x = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - width) / 2;
        int y = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - height) / 2;

        setBounds(x, y, width, height);
        getContentPane().setLayout(new BorderLayout());
        setJMenuBar(new MainMenuBar(this));

        cardLayout = new CardLayout();
        body = new JPanel();
        body.setLayout(cardLayout);
        MainPanel mainPanel = new MainPanel();
        JScrollPane scrollTabbedPane = new JScrollPane(tabbedPane);

        body.add(mainPanel, MAIN_PANEL);
        body.add(scrollTabbedPane, TABBED_PANEL);

        add(body, BorderLayout.CENTER);
        changePanel();

        tabbedPane.addChangeListener((l)->{
            ImageWidow current = (ImageWidow) tabbedPane.getSelectedComponent();
            if(current != null) {
                Boolean isEdited = current.getIsEdited();

                MainMenuBar.enableUndo(isEdited);
                MainMenuBar.enableAlgorithms(!isEdited);
                MainMenuBar.enableSave(isEdited);
            }
        });

        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                SCREEN_WIDTH = e.getComponent().getWidth() * 1.0;
                SCREEN_HEIGHT = e.getComponent().getHeight() * 1.0;

                for (ImageWidow window :
                        imagePanels) {
                    if(window != null){
                        window.reload();
                    }
                }
            }
        });
    }
    public static void changePanel(){
        if(tabbedPane.getTabCount() == 0){
            cardLayout.show(body, MAIN_PANEL);
        }
        else{
            cardLayout.show(body, TABBED_PANEL);
        }
        MainMenuBar.reload();
    }
    public static void addTab(ImageWidow panel){
        imagePanels.add(panel);
        int panelIdx = imagePanels.indexOf(panel);

        tabbedPane.addTab(null, imagePanels.get(panelIdx));
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, getTabComponent(panel.getFileName(), panelIdx, false));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    private static JPanel getTabComponent(String fileName, int panelIdx, boolean edited){
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setOpaque(false);

        JLabel tabLabel = new JLabel();
        tabLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        if (fileName.length() > 14) {
            fileName = fileName.substring(0, 14) + "...";
        }

        if(edited){
            fileName = "<html><i>*" +fileName+"</i></html>";
        }
        tabLabel.setBackground(null);
        tabLabel.setText(fileName);
        tabPanel.add(tabLabel, BorderLayout.CENTER);
        tabPanel.add(getCloseButton(panelIdx), BorderLayout.EAST);

        return tabPanel;
    }

    private static JButton getCloseButton(int panelIdx) {
        JButton closeButton  = new JButton("<html><b>x</b></html>");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(e -> {
            int index = tabbedPane.indexOfComponent(imagePanels.get(panelIdx));

            int result = 0;

            if(((ImageWidow) tabbedPane.getSelectedComponent()).getIsEdited()){
                result = JOptionPane.showConfirmDialog(
                        null,
                        "Czy na pewno chcesz zamknąć bez zapisywania?",
                        "Potwierdzenie",
                        JOptionPane.OK_CANCEL_OPTION
                );
            }
            if (index != -1 && result == JOptionPane.OK_OPTION) {
                tabbedPane.removeTabAt(index);
                imagePanels.set(index, null);
            }
            MainFrame.changePanel();
        });
        return closeButton;
    }

    public static void setTabTitle(JPanel component, boolean edited){
        int index = tabbedPane.indexOfComponent(component);
        ImageWidow tab = (ImageWidow) tabbedPane.getComponentAt(index);

        String title = tab.getFileName();

        JPanel panel = getTabComponent(title, index, edited);
        panel.setBackground(null);
        tabbedPane.setTabComponentAt(index, panel);
    }

    /**
     * Zwraca domyślną szerokość okna programu
     * @return szerokość okna programu
     */
    public static int getFrameWidth(){
        return (int)(SCREEN_WIDTH * 4 / 5);
    }
    /**
     * Zwraca domyślną wysokość okna programu
     * @return wysokość okna programu
     */
    public static int getFrameHeight(){
        return (int)(SCREEN_HEIGHT * 4 / 5);
    }

}