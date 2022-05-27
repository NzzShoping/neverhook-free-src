package ru.neverhook.utils.file;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FileUtils {

    public static void downloadFileFromUrl(String url, File file) {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            try (InputStream is = con.getInputStream();
                 FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buff = new byte[8192];
                int readedLen;
                while ((readedLen = is.read(buff)) > -1) {
                    fos.write(buff, 0, readedLen);
                }
            }
            con.disconnect();
        } catch (IOException e) {
            System.exit(-1);
        }
    }

    public static void showURL(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}