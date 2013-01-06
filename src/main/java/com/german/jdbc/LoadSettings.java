package com.german.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Vector;

import com.german.model.ConnectionBean;
import com.german.model.DBConnectionBeanImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class LoadSettings {

    private static final String SETTINGS_FILE = System.getProperty("file.separator") + "settings.xml";

    private Vector<ConnectionBean> connections = null;

    @SuppressWarnings("unchecked")
    public Vector<ConnectionBean> load() {

        if (connections == null) {

            Reader reader = null;
            try {
                XStream xstream = new XStream(new DomDriver());
                xstream.alias("connection", DBConnectionBeanImpl.class);
                InputStream inputStream = LoadSettings.class.getResourceAsStream(SETTINGS_FILE);
                reader = new InputStreamReader(inputStream);
                xstream.alias("ConnectionBean", ConnectionBean.class, DBConnectionBeanImpl.class);
                List<ConnectionBean> list = (List<ConnectionBean>) xstream.fromXML(reader);
                connections = new Vector<ConnectionBean>(list);

            }  finally {
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
