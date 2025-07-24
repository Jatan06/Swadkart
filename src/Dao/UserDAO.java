package Dao;
import java.sql.*;
import Constants.*;
import Services.SpeakTextService;

public class UserDAO {
    public static void insertNewUser(String id,String user_Name,String email,String ph_no,String address,String password) throws Exception {
        String q="Insert into users values(?,?,?,?,?,NOW(),?)";
        PreparedStatement pst= AppConstants.connection.prepareStatement(q);
        pst.setString(1,id);pst.setString(2,user_Name);
        pst.setString(3,email);pst.setString(4,ph_no);
        pst.setString(5,address);pst.setString(6,password);
        if(pst.executeUpdate()>0)
        {
            System.out.println("\nYour user id is \""+id+".");
            SpeakTextService.speak(user_Name+" your registration is successful");
            Thread.sleep(3000);
        }
        else {
            System.out.println("user not added to the database!!");
        }
    }
    public static int countUsers() throws Exception {
        String q="select count(*) from users";
        Statement st=AppConstants.connection.createStatement();
        ResultSet rs=st.executeQuery(q);
        rs.next();
        return rs.getInt(1);
    }
    public static void profile() {
        // Pending
    }
    public static boolean phoneNumberExists(String number) throws Exception {
        String query = "SELECT phone_number FROM users WHERE phone_number = ?;";
        PreparedStatement ps = AppConstants.connection.prepareStatement(query);
        ps.setString(1, number);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // true if phone number exists
    }
}
