package com.example.operations;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DataConnector {



    public static Connection getConnection() throws Exception {

        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/application.properties"));

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);




    }

    public static void main(String[] args) {
        try {
            Connection connection = DataConnector.getConnection();
            System.out.println(" CONNECTED TO DATABASE SUCCESSFULLY");
            connection.close();
        } catch (Exception e) {
            System.out.println(" CONNECTION FAILED");
            e.printStackTrace();
        }
    }
}
