package Db;
import Constants.AppConstants;
import java.sql.*;
public class DBConnection {
    public static boolean DbConnection() {
        try {
            AppConstants.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/swadkart", "root", "");
            return true;
        } catch (SQLException e) {
            System.out.println("\nException :- Java Connection to Swadkart Database is unsuccessful.");
//            e.printStackTrace();
            return false;
        }
    }
}