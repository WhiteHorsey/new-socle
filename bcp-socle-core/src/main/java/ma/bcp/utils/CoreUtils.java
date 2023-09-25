package ma.bcp.utils;

import ma.bcp.exception.custom.exceptions.ImageConversionException;
import ma.bcp.exception.custom.exceptions.InvalidURIException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ma.bcp.utils.Constants.Date.*;

public class CoreUtils {
    public static URI parseURI(String uriString) throws InvalidURIException {
        try {
            return new URI(uriString);
        } catch (URISyntaxException e) {
            throw new InvalidURIException("Invalid URI: " + e.getMessage());
        }
    }

    public static String imageToString(MultipartFile image) throws ImageConversionException {
        try {
            return new String(Base64.encodeBase64(image.getBytes()));
        } catch (IOException e) {
            throw new ImageConversionException("Error converting image to Base64");

        }
    }

    public static String getBaseDateFormat(String format) {
        return DateTimeFormatter.ofPattern(format).format(LocalDateTime.now());
    }

    public static String getCurrentDateWithIso8601() {
        return getBaseDateFormat(ISO_8601);
    }

    public static String getCurrentDateWithRfc3339() {
        return getBaseDateFormat(RFC_3339);
    }

    public static String getCurrentDateOnly() {
        return getBaseDateFormat(DATE_ONLY);
    }

    public static String getCurrentTimeOnly() {
        return getBaseDateFormat(TIME_ONLY);
    }

    public static String getCurrentDateForFile() {
        return getBaseDateFormat(FILE_NAMING);
    }



}