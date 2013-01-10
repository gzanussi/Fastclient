package com.fastclient.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.fastclient.model.ConnectionBean;
import com.fastclient.model.DBConnectionBeanImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class LoaderSettings {

    public static final String HOME_DIRECTORY = System.getProperty("user.home");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String APP_DIRECTORY = HOME_DIRECTORY + FILE_SEPARATOR + ".fastclient";
    public static final String SETTINGS_FILE = APP_DIRECTORY + FILE_SEPARATOR + "settings.xml";
    public static final String IMAGE_DIRECTORY = APP_DIRECTORY + FILE_SEPARATOR + "images";
    public static final String LIB_DIRECTORY = APP_DIRECTORY + FILE_SEPARATOR + "lib";
    private File file;
    private static LoaderSettings instance;

    private LoaderSettings() {

        checkDirectories();
        file = new File(SETTINGS_FILE);

        if (!file.exists()) {
            try {
                file.createNewFile();
                save(new ArrayList<ConnectionBean>());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void checkDirectories() {

        File directory = new File(APP_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
            new File(IMAGE_DIRECTORY).mkdir();
            new File(LIB_DIRECTORY).mkdir();
        }

    }

    public static LoaderSettings getInstance() {

        if (instance == null) {
            instance = new LoaderSettings();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<ConnectionBean> load() {

        List<ConnectionBean> connections = null;
        Reader reader = null;
        try {

            reader = new FileReader(file);
            connections = (List<ConnectionBean>) getXStream().fromXML(reader);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connections;

    }

    private XStream getXStream() {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("ConnectionBean", ConnectionBean.class, DBConnectionBeanImpl.class);
        return xstream;
    }

    public void save(List<ConnectionBean> list) {

        FileWriter writer = null;
        try {

            String contentFile = getXStream().toXML(list);
            writer = new FileWriter(file);
            writer.append(contentFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    
    public static String[] getDriverNames(){
        File file = new File(LIB_DIRECTORY);
        String[] list = file.list();
        return list;
    }
    
}
