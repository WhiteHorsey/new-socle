package ma.bcp.exception.custom.exceptions;

import java.io.IOException;

public class ImageConversionException extends IOException {

    public ImageConversionException(String message) {
        super(message);
    }
}