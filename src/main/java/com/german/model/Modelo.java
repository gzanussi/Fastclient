package com.german.model;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class Modelo extends AbstractTableModel {

	private Object[][] datoColumna;
	private Object[] titColumna;

	public Modelo(Object[][] datoColumna, Object[] titColumna) {
		this.datoColumna = datoColumna;
		this.titColumna = titColumna;
	}
	
	public String getColumnName(int column) {
		return titColumna[column].toString();
	}

	public int getRowCount() {
		return datoColumna.length;
	}

	public int getColumnCount() {
		return titColumna.length;
	}

	public Object getValueAt(int row, int col) {
		return datoColumna[row][col];
	}

	public boolean isCellEditable(int row, int column) {
		return true;
	}

	public void setValueAt(Object value, int row, int col) {
		datoColumna[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}
