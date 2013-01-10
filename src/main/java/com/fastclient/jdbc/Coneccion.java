package com.fastclient.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fastclient.model.ConnectionBean;
import com.fastclient.model.PrimaryKeyElement;
import com.fastclient.model.SQLResult;

public class Coneccion{
	
	private ConnectionBean connectionBean;
	
	
	public Coneccion(final ConnectionBean bean) {
		this.connectionBean = bean;
	}



	/**
	 *  Ejecuta el sql dado.
	 * @param sql
	 * @param maxRows
	 * @return
	 */
	public SQLResult executeQuery(String sql, int maxRows){
		
		Connection connection = getConnection();
		SQLResult result = new SQLResult();
		
		try {
			
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setMaxRows(maxRows);
			ResultSet r= prepareStatement.executeQuery();
			
			int num_columnas;
			ResultSetMetaData mdata = r.getMetaData();
			num_columnas = mdata.getColumnCount();
			
			String[] titColumna = new String[num_columnas];
			String[][] data = new String[maxRows][num_columnas];
			
			for( int i=0; i < num_columnas; i++ ) {
	        	titColumna[i] = mdata.getColumnName(i+1);
	        }
	        
			int fila =0;				
		    while(r.next()){
		        for( int i=0; i < num_columnas; i++ ) {
		        	data[fila][i] = r.getString(i+1);
		        }
		        fila++;
		    }
			
			result.setData(data);
			result.setColumnsName(titColumna);
			
			
		} catch (SQLException e) {
			String[][] data = {{e.toString()},{""}};
			String[] titColumna ={"Error en la consulta"};
			result.setData(data);
			result.setColumnsName(titColumna);
			rollback(connection);
			e.printStackTrace();
		
		}finally{
			closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	/**
	 * @param connection
	 */
	private void rollback(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException e) {
			System.err.println("Rollback...");
			e.printStackTrace();
		}
		
	}



	/**
	 * Devuelve una lista de String con el nombre de todas las tablas.
	 * @return
	 */
	public List<String> showAllTables() {

		String[] types = {"TABLE"};
		return  getForTypes(types);
		
	}
	
	/**
	 * Devuelve una lista de String con el nombre de todas las vistas.
	 * @return
	 */
	public List<String> showAllViews() {

		String[] types = {"VIEW"};
		return  getForTypes(types);
		
	}


	private List<String> getForTypes(String[] types) {
		Connection conn = getConnection();
	
		List<String> tablas = new ArrayList<String>();
		
		try {
			DatabaseMetaData dbmdata = conn.getMetaData();
			ResultSet tableTypes = dbmdata.getTables(null, connectionBean.getSchema(),"%",types);
		    while(tableTypes.next()){
	        	tablas.add(tableTypes.getString( tableTypes.findColumn( "TABLE_NAME" ) ) );
		    }

		    
		} catch (SQLException e) {
			rollback(conn);
			e.printStackTrace();
		}finally{
			closeConnection(conn);
		}
		return tablas;
	}
	
	/**
	 * Ejecuta una sentencia SQL de tipo update.
	 * @param sql
	 * @return
	 */
	public String updateSQL(String sql) {
		return updateSQLWithParameters(sql, null);
	}
	
	/**
	 * Ejecuta una sentencia SQL de tipo update con los parametros dados.
	 * @param sql
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public String updateSQLWithParameters(String sql,List parameters) {
		StringBuffer resultado = new StringBuffer();
		Connection connection = getConnection();
		
		try {
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			if(parameters != null){
				
				Iterator iterator = parameters.iterator();
				for (int i=1;iterator.hasNext();i++) {
					statement.setObject(i, iterator.next());
				}
			}
			
			resultado.append(statement.executeUpdate());
			resultado.append(" filas afectadas.");
			
		} catch (SQLException e) {
		    
//			resultado.append(e.getMessage());
		    rollback(connection);
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally{
			closeConnection(connection);
		}
		return resultado.toString();
	}
	
	
	/**
	 * Retorna una lista de los nombres de las columnas de la tabla dada.
	 * @param tableName
	 * @return
	 */
	public List<String> getColumnsNameForTable(String tableName){
		
		List<String> result = new ArrayList<String>();
		String catalog = connectionBean.getSchema();
		String schemaPattern =null;
		String columnNamePattern = "%";
		ResultSet columns = null;
		Connection connection = getConnection();
		try {
			
			columns = connection.getMetaData().getColumns(catalog,schemaPattern, tableName, columnNamePattern);			   
			while (columns.next()) {
				result.add(columns.getString("COLUMN_NAME") + ": "+ columns.getString("TYPE_NAME").toUpperCase());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(connection);
		}finally{
			closeConnection(connection);
		}
		
		return result;
		
	}


	/**
	 * Obtiene las primary keys de la tabla dada.
	 * @param tableName
	 * @param catalog
	 * @param schemaPattern
	 * @param connection
	 * @throws SQLException
	 * @return primary keys
	 */
	public List<PrimaryKeyElement> getPrimaryKeys(String tableName){
		
		Connection connection = getConnection();
		String schemaPattern =null;
		String catalog = connectionBean.getSchema();
		List<PrimaryKeyElement> keys = new ArrayList<PrimaryKeyElement>();
		
		ResultSet primaryKeys;
		try {
			primaryKeys = connection.getMetaData().getPrimaryKeys(catalog, schemaPattern, tableName);
			while (primaryKeys.next()) {
				String columnName = primaryKeys.getString("COLUMN_NAME");
				PositionType positionType = getPosicion(columnName,tableName);
				keys.add(new PrimaryKeyElement(columnName,positionType.posicion,positionType.type));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		closeConnection(connection);   
		return keys; 
	}

	/**
	 * Obtiene la posici�n de la columna columName en la tabla tableName.
	 * @param columnName
	 * @param tableName
	 * @return
	 */
	private PositionType getPosicion(String columnName, String tableName) {
		
		String catalog = connectionBean.getSchema();
		String schemaPattern =null;
		String columnNamePattern = "%";
		ResultSet columns = null;
		Connection connection = getConnection();
		
		
		try {
			columns = connection.getMetaData().getColumns(catalog,schemaPattern, tableName, columnNamePattern);
			while (columns.next()) {
				if(columns.getString(4).equals(columnName)){
					return  new PositionType(columns.getInt("ORDINAL_POSITION")-1,columns.getString("TYPE_NAME"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}			   
		
		
		return null;
	}



	public Connection getConnection(){
		return DBConnectionService.establishConnectionWith(connectionBean);
	}
	
	private void closeConnection(Connection connection){
		try {
			if (connection != null){
				connection.close();	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private final class PositionType{

		int posicion;
		String type;
		public PositionType(int posicion, String type) {
			this.posicion = posicion;
			this.type = type;
		}
		
	}
}
