package com.fastclient.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

import com.fastclient.model.ConnectionBean;
import com.fastclient.util.JarLoader;

public final class DBConnectionService {

    private static ConnectionBean connectionBean;

    public static Connection establishConnectionWith(final ConnectionBean connectionBean) {

        DBConnectionService.connectionBean = connectionBean;
        Connection conn = null;
        try {

            ClassLoader classLoader = new JarLoader().getClassLoaderForExtraModule();
            Driver d = (Driver)Class.forName(connectionBean.getDriver(), true, classLoader).newInstance();
            DriverManager.registerDriver(new DriverShim(d));
            return DriverManager.getConnection(connectionBean.getUrl(), connectionBean.getUser(), connectionBean.getPassword());


        }
        catch (Exception e) {
            System.err.println("Error trying to connect to DB");
            e.printStackTrace();
        }

        return conn;
    }

    public static String getSchema() {
        return connectionBean.getSchema();
    }

}