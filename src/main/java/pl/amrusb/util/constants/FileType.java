package pl.amrusb.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileType {
    JPEG("jpeg"),
    JPG("jpg"),
    TIFF("tiff"),
    TIF("tif"),
    PNG("png");

    final String value;

    public String getSuffix(){
        return "." + value;
    }
}
