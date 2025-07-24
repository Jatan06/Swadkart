package Dao;
import java.sql.*;
import Constants.*;
import Services.SpeakTextService;

public class UserDAO {
    public static void insertNewUser(String id,String user_Name,String email,String ph_no,String address) throws Exception {
        String q="Insert into users values(?,?,?,?,?,NOW(),?)";
        PreparedStatement pst= AppConstants.connection.prepareStatement(q);
        pst.setString(1,id);pst.setString(2,user_Name);
        pst.setString(3,email);pst.setString(4,ph_no);
        pst.setString(5,address);pst.setString(6,user_Name.split(" ")+ "@123");
        if(pst.executeUpdate()>0)
        {
            System.out.println("\nYour user id is \""+id+"\" and password is "+user_Name.split(" ")+ "@123");
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
        int i = 0;
        Statement s = AppConstants.connection.createStatement();
        ResultSet rs = s.executeQuery("SELECT phone_number FROM users;");
        while (rs.next()) {
            if (rs.getString(4).equals(number)) {
                i = 1;
                break;
            }
        }
        return i == 1;
    }
}
