package Dao;
import java.sql.*;
import Constants.*;
import Services.SpeakTextService;
import Utils.Validators;

public class UserDAO {
    public static void insertNewUser(String id,String user_Name,String email,String ph_no,String address,String password) throws Exception {
        String q="Insert into users values(?,?,?,?,?,NOW(),?)";
        PreparedStatement pst= AppConstants.connection.prepareStatement(q);
        pst.setString(1,id);pst.setString(2,user_Name);
        pst.setString(3,email);pst.setString(4,ph_no);
        pst.setString(5,address);pst.setString(6,password);
        if(pst.executeUpdate()>0)
        {
            System.out.println("\nYour user id is \""+id+"\"");
            SpeakTextService.speak(user_Name+" your registration is successful");
            Thread.sleep(3000);
        }
        else {
            System.out.println("\nUser not added to the database!!");
        }
    }
    public static int countUsers() throws Exception {
        String q="select count(*) from users";
        Statement st=AppConstants.connection.createStatement();
        ResultSet rs=st.executeQuery(q);
        rs.next();
        return rs.getInt(1);
    }
    public static void profile(String id) {
        boolean prof = true;
        while (prof) {
            System.out.println("\n\n===================================");
            System.out.println("|         Profile Menu            |");
            System.out.println("===================================");
            System.out.println("|  1. View Profile                |");
            System.out.println("|  2. Update Profile              |");
            System.out.println("|  3. Back To Menu                |");
            System.out.println("===================================");
            System.out.print("\nPlease select an option: ");
            switch (AppConstants.s.nextInt()) {
                case 1:
                    viewProfile(id);
                    break;
                case 2:
                    updateProfile(id);
                    break;
                case 3:
                    prof = false;
                    break;
                default:
                    System.out.println(AppConstants.ERR_INVALID_INPUT);
            }
        }
    }
    public static boolean phoneNumberExists(String number) throws Exception {
        String query = "SELECT phone_number FROM users WHERE phone_number = ?;";
        PreparedStatement ps = AppConstants.connection.prepareStatement(query);
        ps.setString(1, number);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // true if phone number exists
    }
    private static void updateProfile(String id) {
        CallableStatement up;
        try {
            System.out.print("\nEnter that you want to edit (name,email,address) : ");
            String update = AppConstants.s.next();
            while(true) {
                if(Validators.validateColumnNameUser(update)) {
                    break;
                }
                else {
                    System.out.println("\nInvalid column name. Please enter a valid column name.");
                    update = AppConstants.s.next();
                }
            }
            AppConstants.s.nextLine();
            if(update.equalsIgnoreCase("name")) {
                System.out.print("\nEnter your new name : ");
                String new_name = AppConstants.s.nextLine();
                while (true){
                    if(Validators.validateName(new_name)) {
                        break;
                    }
                    else {
                        System.out.println("Enter valid name :- ");
                        new_name = AppConstants.s.nextLine();
                    }
                }
                up = AppConstants.connection.prepareCall("UPDATE users SET name = ? WHERE id = ?;");
                up.setString(1, new_name);
                up.setString(2, id);
                if(up.executeUpdate()>0) {
                    SpeakTextService.speak("\nYour name has been updated");
                }
                else {
                    System.out.println("\nName not updated, please try again !");
                }
                up.close();
            }
            else if (update.equalsIgnoreCase("email")) {
                System.out.print("\nEnter your new email : ");
                String new_email = AppConstants.s.nextLine();
                while (true){
                    if(Validators.validateEmail(new_email)) {
                        break;
                    }
                    else {
                        System.out.print("\nEnter valid email :- ");
                        new_email = AppConstants.s.nextLine();
                    }
                }
                up = AppConstants.connection.prepareCall("UPDATE users SET email = ? WHERE id = ?;");
                up.setString(1, new_email);
                up.setString(2, id);
                if(up.executeUpdate()>0) {
                    SpeakTextService.speak("\nYour email has been updated");
                }
                else {
                    System.out.println("\nEmail not updated, please try again !");
                }
                up.close();
            }
            else if (update.equalsIgnoreCase("address")) {
                System.out.print("\nEnter your new address : ");
                String new_address = AppConstants.s.nextLine();
                while (true){
                    if(Validators.validateAddress(new_address)) {
                        break;
                    }
                    else {
                        System.out.print("\nEnter valid address :- ");
                        new_address = AppConstants.s.nextLine();
                    }
                }
                up = AppConstants.connection.prepareCall("UPDATE users SET address = ? WHERE id = ?;");
                up.setString(1, new_address);
                up.setString(2, id);
                if(up.executeUpdate()>0) {
                    SpeakTextService.speak("\nYour address has been updated");
                }
                else {
                    System.out.println("\nAddress not updated, please try again !");
                }
                up.close();
            }
            else {
                System.out.print("\nPlease enter a valid column name.\n");
                updateProfile(id);
            }
            Thread.sleep(2000);
            System.out.print("\nDo You want to update another information (y/n) : ");
            String choice = AppConstants.s.next();
            if(choice.equals("y") || choice.equals("Y")) {
                updateProfile(id);
            }
            else if(choice.equals("n") || choice.equals("N")) {
                System.out.println("\nReturning to Profile Menu...");
                AppConstants.s.nextLine();
                profile(id);
            }
            else {
                System.out.println("\nInvalid choice. Please try again.\n");
            }
        }
        catch (Exception e) {
            System.out.print("\nException "+e+" arise in updateProfile. Please provide valid input.");
        }
    }
    private static void viewProfile(String id) {
        try {
            // Prepare the SQL query to fetch user details
            CallableStatement vp = AppConstants.connection.prepareCall("SELECT * FROM users WHERE id = ?;");
            vp.setString(1, id);
            // Execute the query and retrieve the profile details
            ResultSet profile = vp.executeQuery();
            // Check if the profile exists
            if (profile.next()) {
                System.out.println("\n===================================");
                System.out.println("          User Profile            ");
                System.out.println("===================================");
                System.out.println("User ID:         " + profile.getString("id"));
                System.out.println("Name:            " + profile.getString("name"));
                System.out.println("Email:           " + profile.getString("email"));
                System.out.println("Phone Number:    " + profile.getString("phone_number"));
                System.out.println("Address:         " + profile.getString("address"));
                System.out.println("===================================\n");
            } else {
                System.out.println("\nNo user profile found for the given ID: " + id);
            }
            profile.close();
            vp.close();
        }
        catch (Exception e) {
            System.out.println("An unexpected error occurred in updateProfile. Please provide valid input.");
        }
    }
}