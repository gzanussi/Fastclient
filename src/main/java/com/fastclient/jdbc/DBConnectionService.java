package com.fastclient.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.fastclient.model.ConnectionBean;

public final class DBConnectionService {
	
	private static Properties props;
    private static String user;
    private static String password;
    private static String schema;
    private static String url;
    private static String driver;
    
    public static Connection establishConnectionWith(final ConnectionBean connectionBean){
    	
    	Connection conn = null;
    	try {
    		loadSettings(connectionBean);
    		Class.forName(getDriver());
            conn = DriverManager.getConnection(getUrl(), getUser(), getPassword());
            
		} catch (Exception e) {
			System.err.println("Error trying to connect to DB");
			e.printStackTrace();
		}
        
        return conn;
    }    
    
    public static Connection establishConnection(){
    	
    	Connection conn = null;
    	try {
    		loadSettings();
    		Class.forName(getDriver());
            conn = DriverManager.getConnection(getUrl(), getUser(), getPassword());
            
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return conn;
    }
	private static void loadSettings(final ConnectionBean connectionBean) throws IOException{
		
    	setUser(connectionBean.getUser());
		setPassword(connectionBean.getPassword());
		setDriver(connectionBean.getDriver());
		setSchema(connectionBean.getSchema());
		setUrl(connectionBean.getUrl());
		
		
	}

	private static void loadSettings() throws IOException{
		
		props = new Properties();
		FileInputStream fileInputStream = new FileInputStream(getFileName());
		props.load(fileInputStream);
		
    	setUser((String) props.get("user"));
		setPassword((String) props.get("password"));
		setDriver((String) props.get("driver"));
		setSchema((String) props.get("schema"));
		setUrl((String) props.get("url"));
		fileInputStream.close();
		
	}

	private static String getFileName() {
		return System.getProperty("user.home")+System.getProperty("file.separator")+"settings.properties";
		
	}

	public static String getDriver() {
		return driver;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		DBConnectionService.password = password;
	}

	public static Properties getProps() {
		return props;
	}

	public static void setProps(Properties props) {
		DBConnectionService.props = props;
	}

	public static String getSchema() {
		return schema;
	}

	public static void setSchema(String schema) {
		DBConnectionService.schema = schema;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		DBConnectionService.url = url;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		DBConnectionService.user = user;
	}

	public static void setDriver(String driver) {
		DBConnectionService.driver = driver;
	}
    
}