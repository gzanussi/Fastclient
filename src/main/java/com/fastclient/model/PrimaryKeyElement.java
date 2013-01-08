/**
 * 
 */
package com.fastclient.model;


/**
 * @author German Zanussi
 *
 */
public class PrimaryKeyElement {

	private String nombre;
	private int posicion;
	private String type;
	
	/**
	 * @param string
	 */
	public PrimaryKeyElement(String nombre, int posicion,String type) {
		this.nombre = nombre;
		this.posicion = posicion;
		this.type = type;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean requiredQuotes(){
		
		if(type.equalsIgnoreCase("VARCHAR") ||type.equalsIgnoreCase("CHAR")){
			return true;
		}
		return false;
	}
}
