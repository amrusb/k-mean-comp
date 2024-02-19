package pl.amrusb.util.ui.panels;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BottomPanel extends JPanel {
    private static final JLabel durationInfo = new JLabel("Czas trwania algorytmu:");
    private static final JLabel durationTime = new JLabel();
    private static final JProgressBar progressBar = new JProgressBar();
    private static final JLabel progressLabel = new JLabel();

    public BottomPanel(){
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createEtchedBorder();
        setBorder(border);
        var constraints = new GridBagConstraints();
        constraints.weightx = 100;
        constraints.weighty = 100;


        constraints.gridx = 1;
        constraints.insets.set(2, 10,2, 5);
        constraints.anchor = GridBagConstraints.EAST;
        add(durationInfo, constraints);
        durationInfo.setVisible(false);
        constraints.gridx = 3;
        constraints.insets.set(2, 5,2, 10);
        constraints.anchor = GridBagConstraints.WEST;
        add(durationTime, constraints);

        constraints.gridx = 4;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets.set(2, 10,2, 4);
        add(progressLabel, constraints);

        constraints.gridx = 5;
        constraints.gridwidth = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets.set(2, 4,2, 5);
        add(progressBar, constraints);
        progressBar.setMinimum(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
    }


    public static void setDurationTime(float time){
        durationTime.setText(time + " sec");
    }
    public static void decrementProgressMaximum(int value){
        progressBar.setMaximum(progressBar.getMaximum() - value);
    }
    public static void setProgress(int value) {
        progressBar.setValue(value);
        progressBar.repaint();
    }
    public static void setProgressMaximum(int maxValue){
        progressBar.setMaximum(maxValue);
    }
    public static void incrementProgress(){
        progressBar.setValue(progressBar.getValue()+1);
    }
    public static void setProgressBarVisible(boolean flag){
        progressBar.setVisible(flag);
        progressLabel.setVisible(flag);
        if(!flag){
            progressBar.setValue(0);
            progressLabel.setText("");
        }

    }
    public static void setProgressLabel(String label){
        progressLabel.setText(label);
    }
    public static void setDurationInfoVisible(boolean flag){
        durationInfo.setVisible(flag);
        durationTime.setVisible(flag);
        if(!flag) durationTime.setText("");
    }

    public static void clear(){
        durationTime.setText("");
        setProgressLabel("");
        setProgress(0);
    }
}
