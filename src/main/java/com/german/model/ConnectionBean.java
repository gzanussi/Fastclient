package com.german.model;

public interface ConnectionBean {

    public abstract boolean isEmpty();
    
    public abstract String getConnectionName();

    public abstract void setConnectionName(String connectionName);

    public abstract String getUser();

    public abstract void setUser(String user);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract String getSchema();

    public abstract void setSchema(String schema);

    public abstract String getUrl();

    public abstract void setUrl(String url);

    public abstract String getDriver();

    public abstract void setDriver(String driver);

    public abstract String getImmageName();

    public abstract void setImmageName(String immageName);

}