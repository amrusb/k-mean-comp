package pl.amrusb.util.except;

import pl.amrusb.ui.MainMenuBar;

import javax.swing.*;
import java.awt.*;

public class ExceptionDialog extends JDialog {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 120;
    private static final int SP_HEIGHT = 350;

    public ExceptionDialog(JFrame parent, String title, String message, StackTraceElement[] stackTrace){
        super(parent, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(parent);
        setResizable(false);

        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());

        Icon errorIcon = UIManager.getIcon("OptionPane.errorIcon");

        JPanel messagePanel = new JPanel();
        JLabel messageLabel = new JLabel();
        messageLabel.setText(message);
        JLabel iconLabel = new JLabel(errorIcon);
        messagePanel.add(iconLabel);
        messagePanel.add(messageLabel);

        JPanel stackTracePanel = new JPanel(new BorderLayout());
        JButton showButton = new JButton("Pokaż więcej");
        JButton exitButton = new JButton("Ok");

        exitButton.addActionListener(e-> dispose());

        JTextArea stackTraceArea = new JTextArea();

        stackTraceArea.setEditable(false);
        stackTraceArea.setForeground(Color.RED);

        JScrollPane sp =  new JScrollPane(stackTraceArea);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setPreferredSize(new Dimension(WIDTH,SP_HEIGHT));
        sp.setVisible(false);

        showButton.addActionListener(e ->{
            sp.setVisible(!sp.isVisible());
            sp.getVerticalScrollBar().setValue(0);
            if(sp.isVisible()){
                setSize(WIDTH, HEIGHT + SP_HEIGHT);
                showButton.setText("Pokaż mniej");
            }
            else {
                showButton.setText("Pokaż więcej");
                setSize(WIDTH, HEIGHT);
            }
            this.revalidate();
        });

        for (StackTraceElement stackTraceElement : stackTrace) {
            stackTraceArea.append(stackTraceElement.toString() + "\n");
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 5, 10, 0);
        gbc.gridx = 1;
        gbc.gridy = 0;

        buttonPanel.add(showButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 10, 10);
        buttonPanel.add(exitButton, gbc);

        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10,10,10);
        buttonPanel.add(Box.createHorizontalGlue(), gbc);

        stackTracePanel.add(buttonPanel, BorderLayout.NORTH);
        stackTracePanel.add(sp, BorderLayout.CENTER);

        add(messagePanel,BorderLayout.CENTER);
        add(stackTracePanel,BorderLayout.SOUTH);
        setVisible(true);
    }
}
