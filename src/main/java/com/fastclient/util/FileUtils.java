package com.fastclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileUtils {

    public static void copyJarFileToLib(final File fileIn) {
        
        File f2 = null;
        InputStream in = null;
        OutputStream out = null;
        try {
        
            f2 = new File(LoaderSettings.LIB_DIRECTORY + LoaderSettings.FILE_SEPARATOR + fileIn.getName());
            in = new FileInputStream(fileIn);
            out = new FileOutputStream(f2);

            byte[] buf = new byte[10*1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            System.out.println("File copied.");
        }
        catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                in.close();
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
