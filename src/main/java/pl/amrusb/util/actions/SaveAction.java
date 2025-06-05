package pl.amrusb.util.actions;

import lombok.SneakyThrows;
import pl.amrusb.Main;
import pl.amrusb.util.img.ImageFilter;
import pl.amrusb.util.constants.AlgorithmsMetrics;
import pl.amrusb.ui.MainFrame;
import pl.amrusb.ui.MainMenuBar;
import pl.amrusb.segm.comp.CompareWindow;
import pl.amrusb.segm.ImageWidow;

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
        ImageWidow current = (ImageWidow) MainFrame.getTabbedPane().getSelectedComponent();
        MainMenuBar.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String filePath = current.getFilePath();
        String fileName = current.getFileName();
        String originalFileName = current.getFileName();
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
                    fileName +="." + formatName;
                }
            }
            else return;
        }

        try {
            if(current.getBfISegmented() == null){
                String rootPath = filePath.substring(0, filePath.lastIndexOf('\\'));
                String name = fileName.substring(0, fileName.indexOf('.'));
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String dirPath = rootPath + "\\" + name + "-" + date;

                File dir =  new File(dirPath);
                if(dir.exists()){
                    throw new RuntimeException("Istnieje już folder " + dirPath);
                }
                boolean created = dir.mkdir();
                if(created){
                    File impFile = new File(dirPath + "\\" + name + IMP_SUFIX + formatName);
                    File adaptFile = new File(dirPath + "\\" + name + ADAPT_SUFIX + formatName);
                    File wekaFile = new File(dirPath + "\\" + name + WEKA_SUFIX + formatName);

                    Map<AlgorithmsMetrics, BufferedImage> images = current.getCwCompare().getOutputImages();

                    ImageIO.write(images.get(AlgorithmsMetrics.IMP), formatName, impFile);
                    ImageIO.write(images.get(AlgorithmsMetrics.ADAPT), formatName, adaptFile);
                    ImageIO.write(images.get(AlgorithmsMetrics.WEKA), formatName, wekaFile);


                    CompareWindow comparePanel = current.getCwCompare();
                    FileWriter csvFileWriter = new FileWriter(dirPath + "\\" + name + INFO_SUFIX);
                    byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
                    csvFileWriter.write(new String(bom));
                    csvFileWriter.write("Nazwa pliku:;" + originalFileName + "\n");
                    csvFileWriter.write("Liczba klastrów:;" );
                    csvFileWriter.write(comparePanel.getPProperties().getVlClusterCount().getText() + "\n");
                    csvFileWriter.write("Maksymalna liczba iteracji:;");
                    csvFileWriter.write(comparePanel.getPProperties().getVlMaxIter().getText() + "\n");
                    csvFileWriter.write(";Implementacja;Adaptive;Weka" + "\n");
                    csvFileWriter.write("Liczba iteracji:;");
                    csvFileWriter.write("\"" + comparePanel.getPProperties().getVlImpIterCount().getText().replace('.',',') + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPProperties().getVlAdaptIterCount().getText().replace('.',',')  + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPProperties().getVlWekaIterCount().getText().replace('.',',')  + "\"\n");
                    csvFileWriter.write("Czas trwania (sec):;");
                    csvFileWriter.write("\"" + comparePanel.getPProperties().getVlImpTime().getText().replace('.',',')  + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPProperties().getVlAdaptTime().getText().replace('.',',')  + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPProperties().getVlWekaTime().getText().replace('.',',')  + "\"\n");
                    csvFileWriter.write("\n");
                    csvFileWriter.write(";Implementacja-Adaptive;Implementacja-Weka;Adaptive-Weka" + "\n");
                    csvFileWriter.write("Indeks Jaccard'a:;");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfJaccard1().getText().replace('.',',')  + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfJaccard2().getText().replace('.',',')  + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfJaccard3().getText().replace('.',',')  + "\"\n");
                    csvFileWriter.write("Współczynnik Dice'a:;");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfDice1().getText().replace('.',',')  + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfDice2().getText().replace('.',',')  + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfDice3().getText().replace('.',',')  + "\"\n");
                    csvFileWriter.write(";Implementacja;Adaptive;Weka" + "\n");

                    csvFileWriter.write("Wynik Sihlouette:;");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfImpSil().getText().replace('.',',')  + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfAdaptSil().getText().replace('.',',') + "\";");
                    csvFileWriter.write("\"" + comparePanel.getPMetrics().getTfWekaSil().getText().replace('.',',')  + "\"\n");
                    csvFileWriter.close();

                    MainFrame.setTabTitle(current, false);
                    current.setIsEdited(false);
                    JOptionPane.showMessageDialog(null, "Pomyślnie zapisano.");
                    MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
                }
                else throw new Exception("Błąd w tworzeniu katalogu!");
            }
            else{
                File output = new File(filePath);
                BufferedImage image;
                if(current.hasSegmentedImage()) {
                    image = current.getBfISegmented();
                }
                else{
                    image = current.getBfIOriginal();
                }

                ImageIO.write(image, formatName, output);
                MainFrame.setTabTitle(current, false);
                current.setIsEdited(false);
                JOptionPane.showMessageDialog(null, "Pomyślnie zapisano.");
                MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
            }
        }catch (Exception ex){
           throw new RuntimeException(ex);
        }
        MainMenuBar.getOwner().setCursor(Cursor.getDefaultCursor());
    }


}
