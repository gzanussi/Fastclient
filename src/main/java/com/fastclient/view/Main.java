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
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Action;
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
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultEditorKit;
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
    private final JButton executeButton = new JButton("Execute");
    private final JButton insertButton = new JButton("Insert Template");
    private final JButton deleteButton = new JButton("Delete Template");
    private final JButton botonDelete = new JButton("Delete rows selected");
    private final JTree tree = new JTree();
    private final JScrollPane scrollTree = new JScrollPane(tree);
    private final JScrollPane scrollTable = new JScrollPane(tabla);
    private final JFrame ventana = new JFrame("Fastclient");
    private final JPanel panel = new JPanel();
    private final JPanel buttonsPanel = new JPanel();
    private final JPanel panel2 = new JPanel();
    private final JPanel panel3 = new JPanel();
    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    private final DefaultMutableTreeNode tables = new DefaultMutableTreeNode("Tables");
    private final DefaultMutableTreeNode views = new DefaultMutableTreeNode("Views");
    private final JTextArea history = new JTextArea(10, 1);
    private DefaultMutableTreeNode currentNode;
    private Action selectLine;
    
    public Main(final ConnectionBean connectionBean, final DBSelector dbSelector) {

        this.coneccion = new Coneccion(connectionBean);
        ventana.setTitle(ventana.getTitle() + " - " + connectionBean.getConnectionName());
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        history.setEditable(false);
        selectLine = getAction(DefaultEditorKit.selectLineAction);
        history.addMouseListener( new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if ( SwingUtilities.isLeftMouseButton(e)  && e.getClickCount() == 1)
                {
                    selectLine.actionPerformed( null );
                    texto.setText(history.getSelectedText());
                }
                
            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseReleased(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }
            
        });;
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

        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                refreshModel(texto.getText());
            }
        });

        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                StringBuilder sql = new StringBuilder();
                sql.append("insert into " + currentNode.getUserObject() + "\n(");

                List<String> columnsNameForTable = getConeccion().getColumnsNameForTable(currentNode.getUserObject().toString());
                Iterator<String> iterator = columnsNameForTable.iterator();
                StringBuffer defaultValues = new StringBuffer();
                defaultValues.append("\nvalues (");
                while (iterator.hasNext()) {

                    String columnName = (String) iterator.next();
                    sql.append(columnName.split(":")[0]);
                    defaultValues.append("null");

                    if (iterator.hasNext()) {
                        sql.append(",");
                        defaultValues.append(",");
                    }
                }

                sql.append(")");
                defaultValues.append(")");
                sql.append(defaultValues);
                texto.setText(sql.toString());
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                texto.setText("delete from " + currentNode.getUserObject());
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
        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.add(executeButton, BorderLayout.NORTH);
        buttonsPanel.add(insertButton, BorderLayout.CENTER);
        buttonsPanel.add(deleteButton, BorderLayout.SOUTH);
        panel.add(buttonsPanel);

        scrollTree.setPreferredSize(new Dimension(220, 300));
        scrollTable.setPreferredSize(new Dimension(500, 300));
        JPanel panelData = new JPanel();
        BorderLayout gridLayout = new BorderLayout();
        panelData.setLayout(gridLayout);
        
        
        JScrollPane scrollHistory = new JScrollPane(history);
        scrollHistory.setPreferredSize(new Dimension(200, 300));
        
        panelData.add(scrollTree,BorderLayout.LINE_START);
        panelData.add(scrollTable,BorderLayout.CENTER);
        panelData.add(scrollHistory,BorderLayout.LINE_END);
        panel2.add(panelData);

        panel3.add(label);
        panel3.add(cantidadRows);
        panel3.add(botonDelete);

        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.getContentPane().add(panel, BorderLayout.NORTH);
        ventana.getContentPane().add(panel2, BorderLayout.CENTER);
        ventana.getContentPane().add(panel3, BorderLayout.SOUTH);
        ventana.setSize(1000, 450);
        ventana.setVisible(true);

        ScreenUtil.centerScreen(ventana);
    }

    /**
     * Agrega las hojas al arbol.
     * @return
     */
    private DefaultTreeModel addChildToTree() {

        root.add(tables);

        for (String nameTable : getConeccion().showAllTables()) {
            tables.add(new TreeNodeCustom(nameTable, this.coneccion));
        }

        if (!getConeccion().showAllViews().isEmpty()) {
            root.add(views);
        }

        for (String nameView : getConeccion().showAllViews()) {
            views.add(new TreeNodeCustom(nameView, this.coneccion));
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

        
        history.append(sql+"\n");
//        history.setLineWrap(true);

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
                    else if (!node.equals(root) && !node.equals(tables) && !node.equals(views)) {
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
            currentNode = (DefaultMutableTreeNode) (e.getPath().getLastPathComponent());

            if (!currentNode.equals(root)) {
                if (currentNode.getParent().equals(tables) || currentNode.getParent().equals(views)) {
                    String tableName = currentNode.getUserObject().toString();
                    String sql = "select * from " + tableName;
                    texto.setText(sql);
                    refreshModel(sql);
                }
                else if (!currentNode.equals(root) && !currentNode.equals(tables) && !currentNode.equals(views)) {
                    String columnDescription = currentNode.getUserObject().toString();
                    String columnName = (new StringTokenizer(columnDescription, ":")).nextToken();
                    String sql = "select " + columnName + " from " + currentNode.getParent().toString();
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

    private Action getAction(String name)
    {
        Action action = null;
        Action[] actions = history.getActions();
 
        for (int i = 0; i < actions.length; i++)
        {
            if (name.equals( actions[i].getValue(Action.NAME).toString() ) )
            {
                action = actions[i];
                break;
            }
        }
 
        return action;
    }

   
}