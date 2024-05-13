package pl.amrusb.segm.comp;

import lombok.Getter;
import pl.amrusb.util.constants.AlgorithmsMetrics;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

@Getter
public class PropertiesPanel extends JPanel {
    JLabel lName = new JLabel("Nazwa pliku:");
    JLabel vlName = new JLabel();

    JLabel lPixelCount = new JLabel("Liczba pikseli:");
    JLabel vlPixelCount = new JLabel();

    JLabel lClusterCount = new JLabel("Liczba klastrów:");
    JLabel vlClusterCount = new JLabel();

    JLabel lMaxIter = new JLabel("Maksymalna liczba iteracji:");
    JLabel vlMaxIter = new JLabel();

    JLabel lImp = new JLabel(AlgorithmsMetrics.IMP.getValue(), SwingConstants.CENTER);
    JLabel lAdapt = new JLabel(AlgorithmsMetrics.ADAPT.getValue(), SwingConstants.CENTER);
    JLabel lWeka = new JLabel(AlgorithmsMetrics.WEKA.getValue(), SwingConstants.CENTER);


    JLabel lIterationCount = new JLabel("Liczba iteracji:");
    JLabel vlImpIterCount  = new JLabel();
    JLabel vlAdaptIterCount  = new JLabel();
    JLabel vlWekaIterCount  = new JLabel();


    JLabel lTime = new JLabel("<html>Czas trwania <small>(sec)</small>:</html>");
    JLabel vlImpTime = new JLabel();
    JLabel vlAdaptTime = new JLabel();
    JLabel vlWekaTime = new JLabel();

    public PropertiesPanel(){
        Border lineBorder = BorderFactory.createEtchedBorder();
        Border etchedBorder = BorderFactory.createEtchedBorder();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        c.weightx = 100;
        c.weighty = 100;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.BOTH;

        this.add(lName,c);
        lName.setBorder(lineBorder);
        c.gridx = 1;
        c.gridwidth = 3;
        this.add(vlName, c);
        vlName.setBorder(lineBorder);
        vlName.setOpaque(true);
        vlName.setBackground(Color.WHITE);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        this.add(lPixelCount,c);
        lPixelCount.setBorder(lineBorder);
        c.gridx = 1;
        c.gridwidth = 3;
        this.add(vlPixelCount, c);
        vlPixelCount.setBorder(lineBorder);
        vlPixelCount.setOpaque(true);
        vlPixelCount.setBackground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        this.add(lClusterCount,c);
        lClusterCount.setBorder(lineBorder);
        c.gridx = 1;
        c.gridwidth = 3;
        this.add(vlClusterCount, c);
        vlClusterCount.setBorder(lineBorder);
        vlClusterCount.setOpaque(true);
        vlClusterCount.setBackground(Color.WHITE);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        this.add(lMaxIter,c);
        lMaxIter.setBorder(lineBorder);
        c.gridx = 1;
        c.gridwidth = 3;
        this.add(vlMaxIter, c);
        vlMaxIter.setBorder(lineBorder);
        vlMaxIter.setOpaque(true);
        vlMaxIter.setBackground(Color.WHITE);

        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        this.add(lImp,c);
        lImp.setBorder(etchedBorder);
        c.gridx = 2;
        this.add(lAdapt,c);
        lAdapt.setBorder(etchedBorder);
        c.gridx = 3;
        this.add(lWeka, c);
        lWeka.setBorder(etchedBorder);
        c.gridx = 0;
        c.gridy = 5;
        this.add(lIterationCount,c);
        lIterationCount.setBorder(lineBorder);
        c.gridx = 1;
        this.add(vlImpIterCount,c);
        vlImpIterCount.setBorder(lineBorder);
        vlImpIterCount.setOpaque(true);
        vlImpIterCount.setBackground(Color.WHITE);
        c.gridx = 2;
        this.add(vlAdaptIterCount,c);
        vlAdaptIterCount.setBorder(lineBorder);
        vlAdaptIterCount.setOpaque(true);
        vlAdaptIterCount.setBackground(Color.WHITE);
        c.gridx = 3;
        this.add(vlWekaIterCount,c);
        c.gridx = 0;
        c.gridy = 6;
        vlWekaIterCount.setBorder(lineBorder);
        vlWekaIterCount.setOpaque(true);
        vlWekaIterCount.setBackground(Color.WHITE);

        this.add(lTime,c);
        lTime.setBorder(lineBorder);
        c.gridx = 1;
        this.add(vlImpTime,c);
        vlImpTime.setBorder(lineBorder);
        vlImpTime.setOpaque(true);
        vlImpTime.setBackground(Color.WHITE);
        c.gridx = 2;
        this.add(vlAdaptTime,c);
        vlAdaptTime.setBorder(lineBorder);
        vlAdaptTime.setOpaque(true);
        vlAdaptTime.setBackground(Color.WHITE);
        c.gridx = 3;
        this.add(vlWekaTime,c);
        vlWekaTime.setBorder(lineBorder);
        vlWekaTime.setBackground(Color.WHITE);
        vlWekaTime.setOpaque(true);
    }

    public void setValues(
            String fileName,
            Integer pixelCount,
            Integer clusterCount,
            Integer maxIterations,
            Integer impIterCount,
            Integer adaptIterCount,
            Integer wekaIterCount,
            Float impTime,
            Float adaptTime,
            Float wekaTime
    ){
        vlName.setText(fileName);
        vlPixelCount.setText(pixelCount.toString());
        vlClusterCount.setText(clusterCount.toString());
        vlMaxIter.setText(maxIterations.toString());
        vlImpIterCount.setText(impIterCount.toString());
        vlAdaptIterCount.setText(adaptIterCount.toString());
        vlWekaIterCount.setText(wekaIterCount.toString());
        vlImpTime.setText(impTime.toString());
        vlAdaptTime.setText(adaptTime.toString());
        vlWekaTime.setText(wekaTime.toString());
    }
}