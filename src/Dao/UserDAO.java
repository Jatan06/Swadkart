package Dao;
import java.sql.*;
import Constants.*;
public class UserDAO {
    static public void insertNewUser(String id,String user_Name,String email,String ph_no,String address) throws Exception
    {
        String q="Insert into users values(?,?,?,?,?,NOW())";
        PreparedStatement pst= AppConstants.connection.prepareStatement(q);
        pst.setString(1,id);pst.setString(2,user_Name);
        pst.setString(3,email);pst.setString(4,ph_no);
        pst.setString(5,address);
        if(pst.executeUpdate()>0)
        {
            System.out.println("user added to the database!!");
        }
        else {
            System.out.println("user not added to the database!!");
        }
    }
    static public int countUsers() throws Exception
    {
        String q="select count(*) from users";
        Statement st=AppConstants.connection.createStatement();
        ResultSet rs=st.executeQuery(q);
        rs.next();
        return rs.getInt(1);
    }
}
