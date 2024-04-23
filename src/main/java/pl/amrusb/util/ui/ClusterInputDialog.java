package pl.amrusb.util.ui;

import lombok.Getter;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ClusterInputDialog extends JDialog {
    private final JLabel errorLabel = new JLabel();
    private final JFormattedTextField tfMaxIter;
    private final JFormattedTextField tfClusterCount;
    private final JCheckBox imageSource = new JCheckBox("Segmentuj obraz w oryginalnych wymiarach");
    private final JButton submitButton = new JButton("Zatwierdź");

    @Getter
    private Integer clusterCount;
    @Getter
    private Integer maxIter;

    public ClusterInputDialog(JFrame parent, String title, boolean hasRescaledImage) {
        super(parent, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 340);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(intFormat);
        numberFormatter.setAllowsInvalid(false);

        GridBagConstraints constr = new GridBagConstraints();

        constr.gridx = 0;
        constr.gridy = 0;
        constr.gridwidth = 2;
        constr.fill = GridBagConstraints.BOTH;
        constr.insets.set(5,0,5, 0);
        errorLabel.setVisible(false);
        errorLabel.setFont(new Font("Sans-serif", Font.PLAIN, 12));
        add(errorLabel, constr);


        constr.gridy = 1;
        constr.gridwidth = 1;
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.insets.set(10,15,5, 5);

        this.add(new JLabel("Maksymalna ilość iteracji:"), constr);

        constr.gridx = 1;
        constr.insets.set(10,5,5, 15);
        tfMaxIter = new JFormattedTextField(numberFormatter);
        tfMaxIter.setValue(100);
        this.add(tfMaxIter, constr);

        constr.gridx = 0;
        constr.gridy = 2;
        constr.gridwidth = 2;
        constr.insets.set(5,10,10, 10);
        JSlider sMaxIter = new JSlider(100, 500,100);
        sMaxIter.setPaintTicks(true);
        sMaxIter.setSnapToTicks(true);
        sMaxIter.setMajorTickSpacing(50);
        sMaxIter.setMinorTickSpacing(10);
        sMaxIter.setSnapToTicks(true);
        sMaxIter.setPaintLabels(true);
        sMaxIter.addChangeListener(e->{
            JSlider source = (JSlider) e.getSource();
            tfMaxIter.setText("" + source.getValue());
        });
        add(sMaxIter, constr);


        constr.gridx = 0;
        constr.gridy = 3;
        constr.gridwidth = 1;
        constr.fill = GridBagConstraints.BOTH;
        constr.insets.set(10,15,5, 5);
        add(new JLabel("Liczba klastrów:"), constr);

        constr.gridx = 1;
        constr.insets.set(10,5,5, 15);
        tfClusterCount =  new JFormattedTextField(numberFormatter);
        tfClusterCount.setValue(6);
        numberFormatter.setMinimum(0L);
        tfClusterCount.setEditable(true);
        add(tfClusterCount, constr);

        constr.gridy = 4;
        constr.gridx = 0;
        constr.gridwidth = 2;
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.insets.set(5,10,10, 10);

        JSlider sClusterCount = new JSlider(2, 30, 6);
        sClusterCount.setPaintTicks(true);
        sClusterCount.setSnapToTicks(true);
        sClusterCount.setMajorTickSpacing(4);
        sClusterCount.setMinorTickSpacing(1);
        sClusterCount.setSnapToTicks(true);
        sClusterCount.setPaintLabels(true);
        sClusterCount.addChangeListener(e->{
            JSlider source = (JSlider) e.getSource();
            tfClusterCount.setText("" + source.getValue());
        });
        add(sClusterCount, constr);

        constr.gridy = 5;
        constr.gridheight = 1;
        constr.insets.set(5,15,10, 15);
        constr.fill = GridBagConstraints.NONE;
        imageSource.setSelected(true);
        imageSource.setEnabled(hasRescaledImage);
        add(imageSource, constr);

        JPanel buttonPanel = new JPanel();
        submitButton.addActionListener(e -> {
            if (e.getSource() == submitButton) {
                clusterCount = Integer.parseInt(tfClusterCount.getText());
                maxIter = Integer.parseInt(tfMaxIter.getText());
                if(validation()){
                    dispose();
                }
            }
        });
        buttonPanel.add(submitButton);

        JButton cancelButton = new JButton("Anuluj");
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(e -> {
            clusterCount = null;
            maxIter = null;
            dispose();
        });
        constr.gridy = 6;
        constr.insets.set(5,15,10, 15);
        add(buttonPanel, constr);
    }

    private boolean validation(){
        if(maxIter < 100){
            errorLabel.setText("<html><font color='red'><i>Zbyt mała liczba maksymalnej iteracji!</i></font></html>");
            errorLabel.setVisible(true);
            return false;
        }

        if(clusterCount < 2){
            errorLabel.setText("<html><font color='red'><i>Liczba klastrów musi być większa od 1!</i></font></html>");
            errorLabel.setVisible(true);
            return false;
        }

        errorLabel.setVisible(false);
        return true;
    }
    public Boolean checkImageSource() {return imageSource.isSelected(); }

}
