package com.fastclient.jdbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.fastclient.model.ConnectionBean;
import com.fastclient.model.DBConnectionBeanImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class LoadSettings {

    private static final String HOME_DIRECTORY = System.getProperty("user.home");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String APP_DIRECTORY = HOME_DIRECTORY + FILE_SEPARATOR + ".fastclient";
    private static final String SETTINGS_FILE = APP_DIRECTORY + FILE_SEPARATOR + "settings.xml";
    
    private List<ConnectionBean> connections = null;
    private File file;
    private static LoadSettings instance;

    private LoadSettings() {

        file = new File(SETTINGS_FILE);

        if (!file.exists()) {
            FileWriter writer = null;
            try {

                if (new File(APP_DIRECTORY).mkdir()) {

                    String contentFile = "<list></list>";
                    file.createNewFile();
                    writer = new FileWriter(file);
                    writer.append(contentFile);
                    writer.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (writer != null) {
                    try {
                        writer.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static LoadSettings getInstance() {

        if (instance == null) {
            instance = new LoadSettings();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<ConnectionBean> load() {

        if (connections == null) {

            Reader reader = null;
            try {
                XStream xstream = new XStream(new DomDriver());
                xstream.alias("connection", DBConnectionBeanImpl.class);
                try {
                    reader = new FileReader(file);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                xstream.alias("ConnectionBean", ConnectionBean.class, DBConnectionBeanImpl.class);
                connections = (List<ConnectionBean>) xstream.fromXML(reader);

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
        }
        return connections;
    }

}
