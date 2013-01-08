package com.fastclient.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.fastclient.jdbc.LoadSettings;
import com.fastclient.model.ConnectionBean;

public class ConnectionEditor {

    private final JFrame frame = new JFrame("Fastclient - Connection Editor");
    
    public ConnectionEditor() {
        super();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                DBSelector.frame.setVisible(true);
            }
        });

        
        frame.add(BorderLayout.NORTH,new JLabel("Connection List"));
        
        List<ConnectionBean> connections = LoadSettings.getInstance().load();
        
        Object columnNames[] = { "Name", "Image", "User", "Schema", "Url"};
        Object rowData[][] = new Object[connections.size()][columnNames.length];

        for (int i = 0; i < rowData.length; i++) {
            rowData[i][0] = connections.get(i).getConnectionName();
            rowData[i][1] = connections.get(i).getImmageName();
            rowData[i][2] = connections.get(i).getUser();
            rowData[i][3] = connections.get(i).getSchema();
            rowData[i][4] = connections.get(i).getUrl();
        }

        JTable table = new JTable(rowData, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);

    }

}
