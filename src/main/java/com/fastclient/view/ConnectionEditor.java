package com.fastclient.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.fastclient.model.ConnectionBean;
import com.fastclient.model.DBConnectionBeanImpl;
import com.fastclient.util.LoaderSettings;
import com.fastclient.util.ScreenUtil;

public class ConnectionEditor {

    private final JFrame frame = new JFrame("Fastclient - Connection Editor");
    private DBSelector dbSelector;

    public ConnectionEditor(final DBSelector dbSelector) {
        super();
        this.dbSelector = dbSelector;
        
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                dbSelector.showWindow();
            }
        });

        frame.add(BorderLayout.NORTH, new JLabel("Connection List"));
        JTable gridList = createGridList();
        createButtons(gridList);
        frame.setSize(800, 600);
        
        ScreenUtil.centerScreen(frame);
        frame.setVisible(true);

    }

    private void createButtons(final JTable gridList) {

        JButton newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) gridList.getModel();
                Object[] row = new Object[5];
                model.insertRow(gridList.getRowCount(), row);

            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void actionPerformed(ActionEvent e) {

                DefaultTableModel model = (DefaultTableModel) gridList.getModel();
                List<ConnectionBean> connections = new ArrayList<ConnectionBean>();
                for (Object vector : model.getDataVector()) {
                    connections.add(new DBConnectionBeanImpl((Vector<String>)vector));
                }
                LoaderSettings.getInstance().save(connections);
                dbSelector.refreshConnectionList();
                dbSelector.frame.setVisible(true);
                frame.dispose();

            }
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) gridList.getModel();
                model.removeRow(gridList.getSelectedRow());
            }
        });

        JPanel panel = new JPanel();
        panel.add(newButton);
        panel.add(saveButton);
        panel.add(removeButton);
        frame.add(BorderLayout.SOUTH, panel);
        
    }

    private JTable createGridList() {

        List<ConnectionBean> connections = LoaderSettings.getInstance().load();

        Object columnNames[] = { "Name", "Image", "User", "Password", "Schema", "Url" , "Driver" };
        Object rowData[][] = new Object[connections.size()][columnNames.length];

        for (int i = 0; i < rowData.length; i++) {
            rowData[i][0] = connections.get(i).getConnectionName();
            rowData[i][1] = connections.get(i).getImageName();
            rowData[i][2] = connections.get(i).getUser();
            rowData[i][3] = connections.get(i).getPassword();
            rowData[i][4] = connections.get(i).getSchema();
            rowData[i][5] = connections.get(i).getUrl();
            rowData[i][6] = connections.get(i).getDriver();
        }

        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        return table;
    }

}
