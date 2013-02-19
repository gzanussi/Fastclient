package com.fastclient.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.fastclient.model.ConnectionBean;
import com.fastclient.model.ConnectionBeanEmpty;
import com.fastclient.util.LoaderSettings;
import com.fastclient.util.ScreenUtil;

public class DBSelector {

    private JLabel picture;
    private static ConnectionBean connectionBean;
    private JButton connectButton;
    private JComboBox<ConnectionBean> connectionList;
    protected JFrame frame;
    private List<ConnectionBean> connections;

    public DBSelector() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ConnectionBean[] beans = getDBConnectionBeans();
        connectionList = new JComboBox<ConnectionBean>();
        refreshConnectionList();
        connectionList.setSelectedIndex(0);
        connectionList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                @SuppressWarnings("unchecked")
                JComboBox<ConnectionBean> cb = (JComboBox<ConnectionBean>) e.getSource();
                ConnectionBean conn = (ConnectionBean) cb.getSelectedItem();

                if (conn.isEmpty()) {
                    showConnectionEditor();
                }
                else {
                    updateLabel(conn);
                    connectionBean = conn;
                }

            }
        });

        picture = new JLabel();
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
        picture.setHorizontalAlignment(JLabel.CENTER);
        updateLabel(beans[connectionList.getSelectedIndex()]);
        picture.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        picture.setPreferredSize(new Dimension(177, 122 + 10));

        final DBSelector dbSelector = this;

        connectButton = new JButton("connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("connecting to data base: " + getDBConnectionBean().getConnectionName());
                new Main(getDBConnectionBean(), dbSelector);
                frame.setVisible(false);

            }
        });

        panel.add(connectionList, BorderLayout.PAGE_START);
        panel.add(picture, BorderLayout.CENTER);
        panel.add(connectButton, BorderLayout.PAGE_END);

        frame = new JFrame("Fastclient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        ScreenUtil.centerScreen(frame);
        if (beans.length == 1) {
            showConnectionEditor();
        }

    }

    /**
     * Get DataBase Connections
     * 
     * @return
     */
    private ConnectionBean[] getDBConnectionBeans() {

        connections = new ArrayList<ConnectionBean>(LoaderSettings.getInstance().load());
        connections.add(new ConnectionBeanEmpty());

        return connections.toArray(new ConnectionBean[0]);
    }

    private ConnectionBean getDBConnectionBean() {

        if (connectionBean == null) {
            connectionBean = getDBConnectionBeans()[connectionList.getSelectedIndex()];
        }
        return connectionBean;
    }

    protected void updateLabel(ConnectionBean name) {
        ImageIcon icon = createImageIcon(LoaderSettings.IMAGE_DIRECTORY +LoaderSettings.FILE_SEPARATOR +  name.getImageName());
        picture.setIcon(icon);
        picture.setToolTipText("A drawing of a " + name.getConnectionName());
        if (icon != null) {
            picture.setText(null);
        }
        else {
            picture.setText("Image not found");
        }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * @param path
     * @return
     */
    protected static ImageIcon createImageIcon(String path) {

        if (path != null) {
            return new ImageIcon(path);
        }
        else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void showConnectionEditor() {
        frame.setVisible(false);
        new ConnectionEditor(this);
    }

    public void refreshConnectionList() {
        connectionList.setModel(new DefaultComboBoxModel<ConnectionBean>(getDBConnectionBeans()));
    }

    public void showWindow() {
        this.frame.setVisible(true);
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DBSelector();
            }
        });
    }

}