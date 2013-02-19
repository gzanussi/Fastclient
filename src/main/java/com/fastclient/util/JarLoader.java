package com.fastclient.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarLoader {

    
    public ClassLoader getClassLoaderForExtraModule(){

        List<URL> urls = new ArrayList<URL>();

        for(String filepath: LoaderSettings.getDriverNames()){
            try {
                loadJar(urls, LoaderSettings.LIB_DIRECTORY+LoaderSettings.FILE_SEPARATOR+filepath);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (urls.size() > 0) {
            cl = new URLClassLoader(urls.toArray(new URL[urls.size()]), ClassLoader.getSystemClassLoader());
        }
        return cl;
    }

    private void loadJar(List<URL> urls, String filepath) throws IOException, MalformedURLException {
        
        File jar = new File(filepath);
        JarFile jf = new JarFile(jar);
        urls.add(jar.toURI().toURL());
        Manifest mf = jf.getManifest();
        if (mf != null) {
            String cp = mf.getMainAttributes().getValue("class-path");
            if (cp != null) {
                for (String cpe : cp.split("\\s+")) {
                    File lib = new File(jar.getParentFile(), cpe);
                    urls.add(lib.toURI().toURL());
                }
            }
        }
        jf.close();
    }

}