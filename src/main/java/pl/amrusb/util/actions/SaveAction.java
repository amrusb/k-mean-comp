package pl.amrusb.util.actions;

import lombok.SneakyThrows;
import pl.amrusb.Main;
import pl.amrusb.util.img.ImageFilter;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.ui.MainMenuBar;
import pl.amrusb.segm.comp.ComparePanel;
import pl.amrusb.segm.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SaveAction implements ActionListener {
    private static final String IMP_SUFIX = "-imp.";
    private static final String WEKA_SUFIX = "-weka.";
    private static final String ADAPT_SUFIX = "-adapt.";
    private static final String INFO_SUFIX = "-info.csv";

    private final Boolean saveAs;
    public SaveAction(Boolean saveAs){
        this.saveAs = saveAs;
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel current = (ImagePanel) MainFrame.getTabbedPane().getSelectedComponent();
        MainMenuBar.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String filePath = current.getFilePath();
        String fileName = current.getFileName();
        String formatName = fileName.substring(fileName.indexOf('.') + 1 );

        if(saveAs){
            JFileChooser imageChooser = new JFileChooser();
            final String defaultExtension = Main.getProperties().getProperty("image.default.extension");
            String currentDirPath = Main.getProperties().getProperty("image.current.dir");
            imageChooser.setCurrentDirectory(new File(currentDirPath));
            imageChooser.addChoosableFileFilter(new ImageFilter());
            imageChooser.setAcceptAllFileFilterUsed(false);

            int result = imageChooser.showSaveDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                filePath = imageChooser.getSelectedFile().getAbsolutePath();
                fileName = imageChooser.getSelectedFile().getName();

                if (fileName.indexOf('.') != -1) {
                    formatName = fileName.substring(fileName.indexOf('.') + 1);
                } else {
                    formatName = defaultExtension.toLowerCase();
                    filePath += "." + formatName;
                }
            }
            else return;
        }

        try {
            if(current.getSegmentedImage() == null){
                String rootPath = filePath.substring(0, filePath.lastIndexOf('\\'));
                String name = fileName.substring(0, fileName.indexOf('.'));
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String dirPath = rootPath + "\\" + name + "-" + date;

                boolean created = new File(dirPath).mkdir();
                if(created){
                    File impFile = new File(dirPath + "\\" + name + IMP_SUFIX + formatName);
                    File adaptFile = new File(dirPath + "\\" + name + ADAPT_SUFIX + formatName);
                    File wekaFile = new File(dirPath + "\\" + name + WEKA_SUFIX + formatName);

                    Map<AlgorithmsMetrics, BufferedImage> images = current.getComparePanel().getOutputImages();

                    ImageIO.write(images.get(AlgorithmsMetrics.IMP), formatName, impFile);
                    ImageIO.write(images.get(AlgorithmsMetrics.ADAPT), formatName, adaptFile);
                    ImageIO.write(images.get(AlgorithmsMetrics.WEKA), formatName, wekaFile);


                    ComparePanel comparePanel = current.getComparePanel();
                    FileWriter csvFileWriter = new FileWriter(dirPath + "\\" + name + INFO_SUFIX);
                    byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
                    csvFileWriter.write(new String(bom));
                    csvFileWriter.write("Nazwa pliku:;" + fileName + "\n");
                    csvFileWriter.write("Liczba klastrów:;" );
                    csvFileWriter.write(comparePanel.getPProperties().getVlClusterCount().getText() + "\n");
                    csvFileWriter.write("Maksymalna liczba iteracji:;");
                    csvFileWriter.write(comparePanel.getPProperties().getVlMaxIter().getText() + "\n");
                    csvFileWriter.write(";Implementacja;Adaptive;Weka" + "\n");
                    csvFileWriter.write("Liczba iteracji:;");
                    csvFileWriter.write("'" + comparePanel.getPProperties().getVlImpIterCount().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPProperties().getVlAdaptIterCount().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPProperties().getVlWekaIterCount().getText() + "\n");
                    csvFileWriter.write("Czas trwania (sec):;");
                    csvFileWriter.write("'" + comparePanel.getPProperties().getVlImpTime().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPProperties().getVlAdaptTime().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPProperties().getVlWekaTime().getText() + "\n");
                    csvFileWriter.write("\n");
                    csvFileWriter.write(";Implementacja-Adaptive;Implementacja-Weka;Adaptive-Weka" + "\n");
                    csvFileWriter.write("Indeks Jaccard'a:;");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfJaccard1().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfJaccard2().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfJaccard3().getText() + "\n");
                    csvFileWriter.write("Współczynnik Dice'a:;");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfDice1().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfDice2().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfDice3().getText() + "\n");
                    csvFileWriter.write(";Implementacja;Adaptive;Weka" + "\n");

                    csvFileWriter.write("Wynik Sihlouette:;");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfImpSil().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfAdaptSil().getText() + ";");
                    csvFileWriter.write("'" + comparePanel.getPMetrics().getTfWekaSil().getText() + "\n");
                    csvFileWriter.close();
                    MainFrame.setTabTitle(current, false);
                    current.setIsEdited(false);
                    JOptionPane.showMessageDialog(null, "Pomyślnie zapisano.");
                }
                else throw new Exception("Błąd w tworzeniu katalogu!");
            }
            else{
                File output = new File(filePath);
                BufferedImage image;
                if(current.hasSegmentedImage()) {
                    image = current.getSegmentedImage();
                }
                else{
                    image = current.getOriginalImage();
                }

                ImageIO.write(image, formatName, output);
                MainFrame.setTabTitle(current, false);
                current.setIsEdited(false);
                JOptionPane.showMessageDialog(null, "Pomyślnie zapisano.");
            }
        }catch (Exception ex){
           throw ex;
        }
        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }
}
