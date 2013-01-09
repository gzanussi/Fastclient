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
	
    

    /* (non-Javadoc)
     * @see com.german.model.ConnectionBean#getConnectionName()
     */
	@Override
    public String getConnectionName() {
		return connectionName;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#setConnectionName(java.lang.String)
     */
	@Override
    public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#getUser()
     */
	@Override
    public String getUser() {
		return user;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#setUser(java.lang.String)
     */
	@Override
    public void setUser(String user) {
		this.user = user;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#getPassword()
     */
	@Override
    public String getPassword() {
		return password;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#setPassword(java.lang.String)
     */
	@Override
    public void setPassword(String password) {
		this.password = password;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#getSchema()
     */
	@Override
    public String getSchema() {
		return schema;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#setSchema(java.lang.String)
     */
	@Override
    public void setSchema(String schema) {
		this.schema = schema;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#getUrl()
     */
	@Override
    public String getUrl() {
		return url;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#setUrl(java.lang.String)
     */
	@Override
    public void setUrl(String url) {
		this.url = url;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#getDriver()
     */
	@Override
    public String getDriver() {
		return driver;
	}
	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#setDriver(java.lang.String)
     */
	@Override
    public void setDriver(String driver) {
		this.driver = driver;
	}
	

	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#getImmageName()
     */
	@Override
    public String getImageName() {
		return (imageName!=null && imageName!="")?imageName:"default.gif";
	}

	/* (non-Javadoc)
     * @see com.german.model.ConnectionBean#setImmageName(java.lang.String)
     */
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
