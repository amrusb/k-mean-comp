package pl.amrusb.util.ui;

import lombok.Getter;
import pl.amrusb.Main;
import pl.amrusb.util.ui.panels.MainPanel;
import pl.amrusb.util.ui.panels.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;


public class MainFrame extends JFrame {
    private static final Double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final Double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    @Getter
    private static final JTabbedPane tabbedPane = new JTabbedPane();
    private static JPanel body = null;
    private static CardLayout cardLayout = null;

    private static final ArrayList<ImagePanel> imagePanels = new ArrayList<>();

    public MainFrame(){
        setTitle("Segmentacja obrazu");
        int width = (int)(SCREEN_WIDTH * 4 / 5);
        int height = (int)(SCREEN_HEIGHT * 4 / 5);
        int x = (int)(SCREEN_WIDTH - width) / 2;
        int y = (int)(SCREEN_HEIGHT - height) / 2;

        setBounds(x, y, width, height);
        getContentPane().setLayout(new BorderLayout());
        setJMenuBar(new MainMenuBar(this));

        cardLayout = new CardLayout();
        body = new JPanel();
        body.setLayout(cardLayout);
        MainPanel mainPanel = new MainPanel();
        JScrollPane scrollTabbedPane = new JScrollPane(tabbedPane);
        JButton closeButton = new JButton();
        Image closeIcon;
        try {
            closeIcon = ImageIO.read(new File(Main.getROOT_PATH() + "icons/xmark.png"));
            closeButton.setIcon(new ImageIcon(closeIcon));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    MainMenuBar.getOwner(),
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        body.add(mainPanel, "mainPanel");
        body.add(scrollTabbedPane, "tabbedPane");

        add(body, BorderLayout.CENTER);
        changePanel();
    }
    public static void changePanel(){
        if(tabbedPane.getTabCount() == 0){
            cardLayout.show(body, "mainPanel");
        }
        else{
            cardLayout.show(body, "tabbedPane");
        }
    }
    public static void addTab(ImagePanel panel){
        imagePanels.add(panel);
        int panelIdx = imagePanels.indexOf(panel);

        JPanel tabComponent = new JPanel(new BorderLayout());
        tabComponent.setOpaque(false);

        String fileName = panel.getFileName();
        JLabel tabLabel = new JLabel();
        tabLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tabbedPane.setToolTipText("<html>" + fileName + "</html>");
        if (fileName.length() > 14) {
            fileName = fileName.substring(0, 14) + "...";
        }

        tabLabel.setText("<html>" + fileName + "</html>");

        JButton closeButton = null;
        Image closeIcon;
        try {
            closeIcon = ImageIO.read(new File(Main.getROOT_PATH() + "icons/xmark.png"));
            closeButton = new JButton(new ImageIcon(closeIcon));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    MainMenuBar.getOwner(),
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        tabLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        assert closeButton != null;
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(e -> {
            int index = tabbedPane.indexOfComponent(imagePanels.get(panelIdx));
            if (index != -1) {
                tabbedPane.remove(index);
                imagePanels.set(index, null);
            }
            MainFrame.changePanel();
        });

        tabComponent.add(tabLabel, BorderLayout.CENTER);
        tabComponent.add(closeButton, BorderLayout.EAST);
        tabbedPane.addTab(null, imagePanels.get(panelIdx));
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabComponent);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
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