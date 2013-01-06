package com.german.model;

/**
 * Bean con informaci�n de la consulta.
 * @author German Zanussi
 *
 */
public class SQLResult {

	
	public int rowCount;
	public String[] columnsName;
	public String[][] data;
	
	
	public String[][] getData() {
		return data;
	}
	public void setData(String[][] data) {
		this.data = data;
	}
	public String[] getColumnsName() {
		return columnsName;
	}
	public void setColumnsName(String[] columnsName) {
		this.columnsName = columnsName;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	
}
