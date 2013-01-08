package com.fastclient.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.fastclient.jdbc.LoadSettings;
import com.fastclient.model.ConnectionBean;
import com.fastclient.model.ConnectionBeanEmpty;

@SuppressWarnings("serial")
public class DBSelector extends JPanel {

    private static final String IMAGES_PATH = "images/";
    private JLabel picture;
    private static ConnectionBean connectionBean;
    private JButton connectButton;
    private JComboBox connectionList;
    protected static JFrame frame;
    private List<ConnectionBean> connections;

    public DBSelector() {

        super(new BorderLayout());

        ConnectionBean[] beans = getDBConnectionBeans();
        connectionList = new JComboBox(getDBConnectionBeans());
        connectionList.setSelectedIndex(0);
        connectionList.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                ConnectionBean conn = (ConnectionBean) cb.getSelectedItem();
                
                if (conn.isEmpty()) {
                    System.out.println("new connection...");
                    new ConnectionEditor();
                    frame.setVisible(false);
                }else{
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

        connectButton = new JButton("connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("connecting to data base: " + getDBConnectionBean().getConnectionName());
                new Main(getDBConnectionBean());
                frame.setVisible(false);

            }
        });

        add(connectionList, BorderLayout.PAGE_START);
        add(picture, BorderLayout.CENTER);
        add(connectButton, BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Get DataBase Connections
     * 
     * @return
     */
    private ConnectionBean[] getDBConnectionBeans() {
        
        if(connections==null){
            connections = LoadSettings.getInstance().load();
            connections.add(new ConnectionBeanEmpty());
        }
        
        return connections.toArray(new ConnectionBean[0]);
    }

    private ConnectionBean getDBConnectionBean() {

        if (connectionBean == null) {
            connectionBean = getDBConnectionBeans()[connectionList.getSelectedIndex()];
        }
        return connectionBean;
    }

    protected void updateLabel(ConnectionBean name) {
        ImageIcon icon = createImageIcon(IMAGES_PATH + name.getImmageName());
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

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        frame = new JFrame("Fastclient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new DBSelector();
        newContentPane.setOpaque(true); // content panes must be opaque
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}