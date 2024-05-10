package pl.amrusb.segm.comp;

import lombok.Getter;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.util.constants.MetricsTypes;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

@Getter
public class MetricsPanel extends JPanel {
    JLabel lImpAdapt = new JLabel(AlgorithmsMetrics.IMP_ADAPT.getValue(), SwingConstants.CENTER);
    JLabel lImpWeka = new JLabel(AlgorithmsMetrics.IMP_WEKA.getValue(), SwingConstants.CENTER);
    JLabel lAdaptWeka = new JLabel(AlgorithmsMetrics.ADAPT_WEKA.getValue(), SwingConstants.CENTER);

    JLabel lJaccard = new JLabel(MetricsTypes.JACCARD.getValue(), SwingConstants.CENTER);
    JTextField tfJaccard1 = new JTextField();
    JTextField tfJaccard2 = new JTextField();
    JTextField tfJaccard3 = new JTextField();
    JLabel lDice = new JLabel(MetricsTypes.DICE.getValue(), SwingConstants.CENTER);
    JTextField tfDice1 = new JTextField();
    JTextField tfDice2 = new JTextField();
    JTextField tfDice3 = new JTextField();

    JLabel lImp = new JLabel(AlgorithmsMetrics.IMP.getValue(), SwingConstants.CENTER);
    JLabel lAdapt = new JLabel(AlgorithmsMetrics.ADAPT.getValue(), SwingConstants.CENTER);
    JLabel lWeka = new JLabel(AlgorithmsMetrics.WEKA.getValue(), SwingConstants.CENTER);


    JLabel lSil = new JLabel(MetricsTypes.SIHLOUETTE.getValue(), SwingConstants.CENTER);
    JTextField tfImpSil   = new JTextField();
    JTextField tfAdaptSil   = new JTextField();
    JTextField tfWekaSil   = new JTextField();


    public MetricsPanel(){
        Border etchedBorder = BorderFactory.createEtchedBorder();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        c.weightx = 100;
        c.weighty = 100;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.BOTH;

        /*--JACCARD DICE--*/
        lImpAdapt.setBorder(etchedBorder);
        this.add(lImpAdapt, c);
        c.gridx = 2;
        lImpWeka.setBorder(etchedBorder);
        this.add(lImpWeka, c);
        c.gridx = 3;
        lAdaptWeka.setBorder(etchedBorder);
        this.add(lAdaptWeka, c);

        //JACCARD
        c.gridx = 0;
        c.gridy = 1;
        lJaccard.setBorder(etchedBorder);
        this.add(lJaccard, c);
        c.gridx = 1;
        tfJaccard1.setEditable(false);
        tfJaccard1.setBackground(Color.WHITE);
        this.add(tfJaccard1, c);
        c.gridx = 2;
        tfJaccard2.setEditable(false);
        tfJaccard2.setBackground(Color.WHITE);
        this.add(tfJaccard2, c);
        c.gridx = 3;
        tfJaccard3.setEditable(false);
        tfJaccard3.setBackground(Color.WHITE);
        this.add(tfJaccard3, c);

        //DICE
        c.gridx = 0;
        c.gridy = 2;
        lDice.setBorder(etchedBorder);
        this.add(lDice, c);
        c.gridx = 1;
        tfDice1.setEditable(false);
        tfDice1.setBackground(Color.WHITE);
        this.add(tfDice1, c);
        c.gridx = 2;
        tfDice2.setBackground(Color.WHITE);
        tfDice2.setEditable(false);
        this.add(tfDice2, c);
        c.gridx = 3;
        tfDice3.setEditable(false);
        tfDice3.setBackground(Color.WHITE);
        this.add(tfDice3, c);
        /*--Silhouette--*/
        c.gridy = 3;
        c.gridx = 1;
        lImp.setBorder(etchedBorder);
        this.add(lImp, c);
        c.gridx = 2;
        lAdapt.setBorder(etchedBorder);
        this.add(lAdapt, c);
        c.gridx = 3;
        lWeka.setBorder(etchedBorder);
        this.add(lWeka, c);
        c.gridx = 0;
        c.gridy = 4;
        lSil.setBorder(etchedBorder);
        this.add(lSil, c);
        c.gridx = 1;
        tfImpSil.setEditable(false);
        tfImpSil.setBackground(Color.WHITE);
        this.add(tfImpSil, c);
        c.gridx = 2;
        tfAdaptSil.setEditable(false);
        tfAdaptSil.setBackground(Color.WHITE);
        this.add(tfAdaptSil, c);
        c.gridx = 3;
        tfWekaSil.setEditable(false);
        tfWekaSil.setBackground(Color.WHITE);
        this.add(tfWekaSil, c);
    }

    public void setJaccardValues(Double jaccard1, Double jaccard2, Double jaccard3){
        tfJaccard1.setText(jaccard1.toString());
        tfJaccard2.setText(jaccard2.toString());
        tfJaccard3.setText(jaccard3.toString());
    }
    public void setDiceValues(Double dice1, Double dice2, Double dice3){
        tfDice1.setText(dice1.toString());
        tfDice2.setText(dice2.toString());
        tfDice3.setText(dice3.toString());
    }
    public void setSilhouetteValues(Double silhouette1,Double silhouette2,Double silhouette3){
        tfImpSil.setText(silhouette1.toString());
        tfAdaptSil.setText(silhouette2.toString());
        tfWekaSil.setText(silhouette3.toString());
    }
}