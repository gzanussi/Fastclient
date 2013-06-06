/**
 * 
 */
package com.fastclient.model;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.fastclient.jdbc.Coneccion;

/**
 * @author German Zanussi
 *
 */
@SuppressWarnings("serial")
public class TreeNodeCustom extends DefaultMutableTreeNode{

	

	private String tableName;
	private List<PrimaryKeyElement> primaryKeys;
	
	public TreeNodeCustom() {
		super();
	}

	public TreeNodeCustom(Object userObject, Coneccion coneccion) {
		super(userObject);
		this.tableName = userObject.toString();
		
	      if(primaryKeys==null){
	            primaryKeys = coneccion.getPrimaryKeys(tableName);
	        }
	}

	public TreeNodeCustom(Object userObject) {
		super(userObject);
		this.tableName = userObject.toString();
	}
	
	public List<PrimaryKeyElement> getPrimaryKeys() {

		return primaryKeys;
	}
	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	
	 
}
