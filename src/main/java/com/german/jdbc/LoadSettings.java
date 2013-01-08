package com.german.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.german.model.ConnectionBean;
import com.german.model.DBConnectionBeanImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class LoadSettings {

    private static final String SETTINGS_FILE = System.getProperty("file.separator") + "settings.xml";
    private List<ConnectionBean> connections = null;

    private static LoadSettings instance;

    private LoadSettings() {
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
                InputStream inputStream = LoadSettings.class.getResourceAsStream(SETTINGS_FILE);
                reader = new InputStreamReader(inputStream);
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
