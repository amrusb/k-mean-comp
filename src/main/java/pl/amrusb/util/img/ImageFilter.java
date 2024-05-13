package pl.amrusb.util.img;


import javax.swing.filechooser.FileFilter;
import java.io.File;

import static pl.amrusb.util.constants.FileType.*;

public class ImageFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                return extension.equals(TIFF.getValue()) ||
                        extension.equals(TIF.getValue()) ||
                        extension.equals(JPEG.getValue()) ||
                        extension.equals(JPG.getValue()) ||
                        extension.equals(PNG.getValue());
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "Pliki obrazów";
        }

        String getExtension(File f) {
            String extension = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 &&  i < s.length() - 1) {
                extension = s.substring(i+1).toLowerCase();
            }
            return extension;
        }
    }
