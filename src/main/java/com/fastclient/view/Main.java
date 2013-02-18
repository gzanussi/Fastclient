/**
 * 
 */
package com.fastclient.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.fastclient.jdbc.Coneccion;
import com.fastclient.model.ConnectionBean;
import com.fastclient.model.PrimaryKeyElement;
import com.fastclient.model.SQLResult;
import com.fastclient.model.TreeNodeCustom;
import com.fastclient.util.ScreenUtil;

/**
 * @author German Zanussi
 * 
 */
public class Main {

    private Coneccion coneccion;
    private final JTable tabla = new JTable();
    private final JTextComponent texto = new JTextArea(5, 70);
    private final JTextField cantidadRows = new JTextField("20", 4);
    private final JLabel label = new JLabel("Cantidad de Filas:", JLabel.RIGHT);
    private final JButton botonExecute = new JButton("Execute");
    private final JButton botonDelete = new JButton("Delete rows selected");
    private final JTree tree = new JTree();
    private final JScrollPane scrollTree = new JScrollPane(tree);
    private final JScrollPane scrollTable = new JScrollPane(tabla);
    private final JFrame ventana = new JFrame("Fastclient");
    private final JPanel panel = new JPanel();
    private final JPanel panel2 = new JPanel();
    private final JPanel panel3 = new JPanel();
    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    private final DefaultMutableTreeNode tablas = new DefaultMutableTreeNode("Tablas");
    private final DefaultMutableTreeNode vistas = new DefaultMutableTreeNode("Vistas");

    public Main(final ConnectionBean connectionBean, final DBSelector dbSelector) {

        this.coneccion = new Coneccion(connectionBean);
        ventana.setTitle(ventana.getTitle() + " - " + connectionBean.getConnectionName());
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    System.out.println("closing Data Base connection");
                    coneccion.getConnection().close();
                    dbSelector.showWindow();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        botonExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                refreshModel(texto.getText());
            }
        });

        botonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteRowsSelected();
            }
        });

        tree.setModel(addChildToTree());
        tree.addTreeSelectionListener(new ListenerForTree());
        tree.addMouseListener(new AdaptadorMouse());

        panel.add(new JScrollPane(texto));
        panel.add(botonExecute);

        scrollTree.setPreferredSize(new Dimension(240, 650));
        scrollTable.setPreferredSize(new Dimension(640, 550));
        
        panel2.add(scrollTree);
        panel2.add(scrollTable);
        panel3.add(label);
        panel3.add(cantidadRows);
        panel3.add(botonDelete);

        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.getContentPane().add(panel, BorderLayout.NORTH);
        ventana.getContentPane().add(panel2, BorderLayout.CENTER);
        ventana.getContentPane().add(panel3, BorderLayout.SOUTH);
        ventana.setSize(1000, 820);
        ventana.setVisible(true);
        
        ScreenUtil.centerScreen(ventana);
    }

    /**
     * Agrega las hojas al arbol.
     * @return
     */
    private DefaultTreeModel addChildToTree() {

        root.add(tablas);
        root.add(vistas);

        for (String nameTable : getConeccion().showAllTables()) {
            tablas.add(new TreeNodeCustom(nameTable, this.coneccion));
        }

        for (String nameView : getConeccion().showAllViews()) {
            vistas.add(new TreeNodeCustom(nameView, this.coneccion));
        }

        return new DefaultTreeModel(root);

    }

    /**
     * Ejecuta una actualizacion en la bd con el sql dado.
     * @param sql
     */
    private void refreshModelUpdateSQL(String sql) {
        String[] titColumna = { "Resultado" };
        String result = null;
        try {
            result = getConeccion().updateSQL(sql);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
        Object[][] datoColumna = { { result } };

        tabla.setModel(new TableModel(datoColumna, titColumna));
    }

    /**
     * Refresca el modelo de la tabla con la consulta sql dada.
     * @param sql
     */
    private void refreshModelExecutSQL(String sql) {

        SQLResult result = getConeccion().executeQuery(sql, Integer.valueOf(cantidadRows.getText()));
        setSizeColulmns(10);
        tabla.setModel(new TableModel(result.getData(), result.getColumnsName()));

    }

    /**
     * Setea el tama�o de las columnas de la tabla con el tama�o dado.
     * @param size
     */
    private void setSizeColulmns(int size) {
        for (int i = 1; i < tabla.getColumnModel().getColumnCount(); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            column.setPreferredWidth(size);
        }
    }

    /**
     * Refresca la Tabla
     * @param sql
     */
    private void refreshModel(String sql) {
        if (isQuery(sql)) {
            refreshModelExecutSQL(sql);
        }
        else {
            if (isDDL(sql)) {
                refreshModelDDL(sql);
            }
            else {
                refreshModelUpdateSQL(sql);
            }
        }
    }

    /**
     * Refresca el arbol cuando se hace una query del tipo DDL.
     * @param sql
     */
    private void refreshModelDDL(String sql) {
        tree.setSelectionPath(null);
        refreshModelUpdateSQL(sql);
        tree.setModel(addChildToTree());
        tree.repaint();
    }

    /**
     * Retorna true si es una consulta SQL.
     * @param sql
     * @return
     */
    private boolean isQuery(String sql) {
        return sql.trim().toUpperCase().startsWith("SELECT");
    }

    /**
     * Retorna true si es una query de tipo DDL en SQL.
     * @param sql
     * @return
     */
    private boolean isDDL(String sql) {

        boolean create = sql.trim().toUpperCase().startsWith("CREATE");
        boolean alter = sql.trim().toUpperCase().startsWith("ALTER");
        boolean drop = sql.trim().toUpperCase().startsWith("DROP");

        return create || alter || drop;
    }

    private Coneccion getConeccion() {
        return coneccion;
    }

    public static void main(String[] args) throws Exception {
        // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    /**
     * Clase utilizada para el click derecho del mouse en el arbol.
     * @author German Zanussi
     * 
     */
    private class AdaptadorMouse extends MouseAdapter {
        public void mouseClicked(MouseEvent evento) {
            if (evento.isMetaDown()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                if (node != null) {
                    if (node.isLeaf()) {
                        String tableName = node.getUserObject().toString();
                        for (String columnName : getConeccion().getColumnsNameForTable(tableName)) {
                            node.add(new DefaultMutableTreeNode(columnName));
                        }
                    }
                    else if (!node.equals(root) && !node.equals(tablas) && !node.equals(vistas)) {
                        node.removeAllChildren();
                    }
                    tree.repaint();
                }
            }
        }
    }

    /**
     * Clase utilizada para el click izquierdo del mouse en el arbol.
     * @author German Zanussi
     * 
     */
    private class ListenerForTree implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getPath().getLastPathComponent());

            if (!node.equals(root)) {
                if (node.getParent().equals(tablas) || node.getParent().equals(vistas)) {
                    String tableName = node.getUserObject().toString();
                    String sql = "select * from " + tableName;
                    texto.setText(sql);
                    refreshModel(sql);
                }
                else if (!node.equals(root) && !node.equals(tablas) && !node.equals(vistas)) {
                    String columnDescription = node.getUserObject().toString();
                    String columnName = (new StringTokenizer(columnDescription, ":")).nextToken();
                    String sql = "select " + columnName + " from " + node.getParent().toString();
                    texto.setText(sql);
                    refreshModel(sql);
                }
            }
        }
    }

    /**
     * Borra los registros seleccionados
     */
    private void deleteRowsSelected() {

        if (tabla.getSelectedRows().length == 0) {
            return;
        }

        TreeNodeCustom nodo = (TreeNodeCustom) tree.getSelectionModel().getSelectionPath().getPathComponent(2);
        String tableName = nodo.getTableName();

        List<PrimaryKeyElement> primaryKeys = nodo.getPrimaryKeys();

        if (primaryKeys.size() == 1) {
            refreshModelUpdateSQL(deleteWithSiglePrimaryKey(tableName, primaryKeys.get(0)));
        }
        else {
            refreshModelUpdateSQL(deleteWithCompositePrimaryKey(tableName, primaryKeys));
        }

    }

    /**
     * Construye el sql para eliminar los registros seleccionados en una tabla
     * con una primary key simple.
     * @param tableName
     * @param primaryKey
     * @return
     */
    public String deleteWithSiglePrimaryKey(String tableName, PrimaryKeyElement primaryKey) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from " + tableName + " where " + primaryKey.getNombre());

        if (tabla.getSelectedRows().length > 1) {

            sql.append(" in (");
            for (int i = 0; i < tabla.getSelectedRows().length; i++) {

                if (primaryKey.requiredQuotes()) {
                    sql.append("'" + tabla.getModel().getValueAt(tabla.getSelectedRows()[i], primaryKey.getPosicion()) + "'");
                }
                else {
                    sql.append(tabla.getModel().getValueAt(tabla.getSelectedRows()[i], primaryKey.getPosicion()));
                }
                if (i + 1 < tabla.getSelectedRows().length) {
                    sql.append(",");
                }
                else {
                    sql.append(")");
                }
            }

        }
        else {
            if (primaryKey.requiredQuotes()) {
                sql.append("= '" + tabla.getModel().getValueAt(tabla.getSelectedRows()[0], primaryKey.getPosicion()) + "'");
            }
            else {
                sql.append("=" + tabla.getModel().getValueAt(tabla.getSelectedRows()[0], primaryKey.getPosicion()));
            }
        }
        return sql.toString();
    }

    /**
     * Construye el sql para eliminar los registros seleccionados en una tabla
     * con una primary key compuesta.
     * @param tableName
     * @param primaryKeys
     * @return
     */
    public String deleteWithCompositePrimaryKey(String tableName, List<PrimaryKeyElement> primaryKeys) {

        StringBuffer sql = new StringBuffer();
        sql.append("delete from " + tableName + " where ");

        if (tabla.getSelectedRows().length == 1) {

            for (Iterator<PrimaryKeyElement> iterator = primaryKeys.iterator(); iterator.hasNext();) {
                PrimaryKeyElement pk = iterator.next();
                if (pk.requiredQuotes()) {
                    sql.append(pk.getNombre() + " = '" + tabla.getModel().getValueAt(tabla.getSelectedRows()[0], pk.getPosicion()) + "' ");
                }
                else {
                    sql.append(pk.getNombre() + " = " + tabla.getModel().getValueAt(tabla.getSelectedRows()[0], pk.getPosicion()));
                }

                if (iterator.hasNext()) {
                    sql.append(" AND ");
                }
            }

        }
        else {

            for (int i = 0; i < tabla.getSelectedRows().length; i++) {

                sql.append(" (");
                for (Iterator<PrimaryKeyElement> iterator = primaryKeys.iterator(); iterator.hasNext();) {
                    PrimaryKeyElement pk = iterator.next();

                    if (pk.requiredQuotes()) {
                        sql.append(pk.getNombre() + "='" + tabla.getModel().getValueAt(tabla.getSelectedRows()[i], pk.getPosicion()) + "'");
                    }
                    else {
                        sql.append(pk.getNombre() + "=" + tabla.getModel().getValueAt(tabla.getSelectedRows()[i], pk.getPosicion()));
                    }

                    if (iterator.hasNext()) {
                        sql.append(" AND ");
                    }
                }
                sql.append(") ");

                if (i < tabla.getSelectedRows().length - 1) {
                    sql.append(" OR ");
                }
            }
        }

        return sql.toString();
    }

}