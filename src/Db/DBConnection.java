package Db;

import Constants.AppConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    public static boolean DbConnection() {
        Properties props = new Properties();

        // Try to load db.properties from project root
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.out.println("\nError: db.properties file not found.");
            System.out.println("Please copy db.properties.example to db.properties and fill in your MySQL credentials.");
            return false;
        }

        String url  = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        try {
            AppConstants.connection = DriverManager.getConnection(url, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("\nException :- Java Connection to Swadkart Database is unsuccessful.");
            System.out.println("Check your db.properties — URL: " + url + ", User: " + user);
//            e.printStackTrace();
            return false;
        }
    }
}
