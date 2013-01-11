package com.fastclient.model;

import java.util.Vector;

public class DBConnectionBeanImpl implements ConnectionBean {

    private String connectionName;
    private String imageName;
    private String user;
    private String password;
    private String schema;
    private String url;
    private String driver;

    public DBConnectionBeanImpl(String connectionName, String immageName) {
        this.connectionName = connectionName;
        this.imageName = immageName;
    }

    public DBConnectionBeanImpl(String connectionName) {
        this.connectionName = connectionName;
    }

    public DBConnectionBeanImpl() {

    }

    public DBConnectionBeanImpl(Vector<String> vector) {
        connectionName = vector.get(0);
        imageName = vector.get(1);
        user = vector.get(2);
        password = vector.get(3);
        schema = vector.get(4);
        url = vector.get(5);
        driver = vector.get(6);
    }

    @Override
    public String getConnectionName() {
        return connectionName;
    }

    @Override
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Override
    public String getImageName() {
        return (imageName != null && imageName != "") ? imageName : "default.png";
    }

    @Override
    public void setImageName(String immageName) {
        this.imageName = immageName;
    }

    @Override
    public String toString() {
        return this.connectionName;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
