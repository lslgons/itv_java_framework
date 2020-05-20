import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Created by user on 2016-12-02.
 */
public class OcDownLoader {

    public static void loadDaFile(String host, String dest, String[] files) {
        try {
            for (int i = 0; i < files.length; i++) {
                loadDaFile(host, dest, files[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadDaFile(String host, String dest, String filename) {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(dest + filename);

            System.out.println("File Path : "+host+filename);
            URL url = new URL(host + filename);
            URLConnection urlConnection = url.openConnection();
            is = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int readBytes;
            while ((readBytes = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readBytes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
