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
            displayProfileMenu();
            try {
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
                        System.out.println(AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
                }
            } catch (Exception e) {
                System.out.println(AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
                AppConstants.s.nextLine();
            }
        }
    }

    private static void displayProfileMenu() {
        System.out.println("\n\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t****************\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Profile Menu +\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t****************\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t1. Profile Section\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t2. Update Information\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t3. Back\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }

    public static boolean phoneNumberExists(String number) throws Exception {
        String query = "SELECT phone_number FROM users WHERE phone_number = ?;";
        PreparedStatement ps = AppConstants.connection.prepareStatement(query);
        ps.setString(1, number);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // true if phone number exists
    }

    public static boolean changePass(String uid,String ph_no) throws Exception {
        PreparedStatement ps = AppConstants.connection.prepareCall("SELECT * FROM users WHERE id = ? AND phone_number = ?;");
        ps.setString(1, uid);
        ps.setString(2, ph_no);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static void setNewPass(String id,String new_pass) {
        try {
            PreparedStatement ps = AppConstants.connection.prepareCall("UPDATE users SET password = ? WHERE id = ?;");
            ps.setString(1, new_pass);
            ps.setString(2, id);
            if(ps.executeUpdate()>0) {
                System.out.println(AppConstants.TEXT_ANSI_GREEN+"\nYour password has been updated"+AppConstants.ANSI_RESET);
            }
            else {
                System.out.println(AppConstants.TEXT_ANSI_RED+"\nPassword not updated, please try again !"+AppConstants.ANSI_RESET);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateProfile(String id) {
        CallableStatement up;
        try {
            System.out.print("\nEnter (name,email,address) to edit : ");
            String update = AppConstants.s.next();
            while(true) {
                if(Validators.validateColumnNameUser(update)) {
                    break;
                }
                else {
                    System.out.println(AppConstants.TEXT_ANSI_RED+"\nInvalid column name. Please enter a valid column name."+AppConstants.ANSI_RESET);
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
                        System.out.println(AppConstants.TEXT_ANSI_RED+"Enter valid name :- "+AppConstants.ANSI_RESET);
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
                        System.out.print(AppConstants.TEXT_ANSI_RED+"\nEnter valid email :- "+AppConstants.ANSI_RESET);
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
                    System.out.println(AppConstants.TEXT_ANSI_RED+"\nEmail not updated, please try again !"+AppConstants.ANSI_RESET);
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
                        System.out.print(AppConstants.TEXT_ANSI_RED+"\nEnter valid address :- "+AppConstants.ANSI_RESET);
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
                    System.out.println(AppConstants.TEXT_ANSI_RED+"\nAddress not updated, please try again !"+AppConstants.ANSI_RESET);
                }
                up.close();
            }
            else {
                System.out.print(AppConstants.TEXT_ANSI_RED+"\nPlease enter a valid column name.\n"+AppConstants.ANSI_RESET);
                return;
            }
            Thread.sleep(2000);
            System.out.print("\nDo You want to update another information (y/n) : ");
            String choice = AppConstants.s.next();
            while (true) {
                if (choice.equals("y") || choice.equals("Y")) {
                    updateProfile(id);
                } else if (choice.equals("n") || choice.equals("N")) {
                    System.out.println("\nReturning to Profile Menu...");
                    AppConstants.s.nextLine();
                    return;
                } else {
                    System.out.println(AppConstants.TEXT_ANSI_RED+"\nInvalid choice. Please try again.\n"+AppConstants.ANSI_RESET);
                }
            }
        }
        catch (Exception e) {
            System.out.print(AppConstants.TEXT_ANSI_RED+"\nException "+e+" arise in updateProfile. Please provide valid input."+AppConstants.ANSI_RESET);
        }
    }

    private static void viewProfile(String id) {
        try {
            String sql = "SELECT id, name, email, phone_number, address FROM users WHERE id = ?";
            try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet profile = ps.executeQuery()) {
                    if (profile.next()) {
                        final int width = 60;

                        String userId = nvl(profile.getString("id"));
                        String name = nvl(profile.getString("name"));
                        String email = nvl(profile.getString("email"));
                        String phone = nvl(profile.getString("phone_number"));
                        String address = nvl(profile.getString("address"));

                        String border = repeat('=', width);
                        System.out.println();
                        System.out.println(border);
                        System.out.println(center("User Profile", width));
                        System.out.println(border);
                        System.out.println(formatRow("User ID", userId, width));
                        System.out.println(formatRow("Name", name, width));
                        System.out.println(formatRow("Email", email, width));
                        System.out.println(formatRow("Phone Number", phone, width));
                        System.out.println(formatMultilineRow("Address", address, width));
                        System.out.println(border);
                    } else {
                        System.out.println("\nNo user profile found for the given ID: " + id);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred in viewProfile. Please try again.");
        }
    }

    // Helper to avoid null/blank printing
    private static String nvl(String s) {
        return (s == null || s.trim().isEmpty()) ? "-" : s.trim();
    }

    // Draw a repeated character line
    private static String repeat(char ch, int count) {
        StringBuilder sb = new StringBuilder(Math.max(count, 0));
        for (int i = 0; i < count; i++) sb.append(ch);
        return sb.toString();
    }

    // Center text within a fixed width using spaces
    private static String center(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) return text.substring(0, width);
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < padding; i++) sb.append(' ');
        sb.append(text);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }

    // Format a single-line row like: | Label: Value |
    private static String formatRow(String label, String value, int width) {
        final int labelWidth = 16; // fixed label width
        // "| " + label + " : " + value + " |" => reserve spaces accordingly
        int valueWidth = width - (2 + labelWidth + 3 + 2);
        if (value.length() > valueWidth) value = value.substring(0, Math.max(valueWidth - 3, 0)) + (valueWidth > 3 ? "..." : "");
        String lbl = (label.length() > labelWidth) ? label.substring(0, labelWidth) : label;
        return String.format("| %-16s : %-"+valueWidth+"s |", lbl, value);
    }

    // Format a multi-line row for long fields like address
    private static String formatMultilineRow(String label, String value, int width) {
        final int labelWidth = 16;
        int valueWidth = width - (2 + labelWidth + 3 + 2);
        java.util.List<String> lines = wrap(value, valueWidth);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String lbl = (i == 0) ? label : "";
            String row = String.format("| %-16s : %-"+valueWidth+"s |",
                    (lbl.length() > labelWidth ? lbl.substring(0, labelWidth) : lbl),
                    lines.get(i));
            sb.append(row).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    // Simple word-wrap for a given width
    private static java.util.List<String> wrap(String text, int width) {
        java.util.List<String> out = new java.util.ArrayList<>();
        if (text == null) {
            out.add("-");
            return out;
        }
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        for (String w : words) {
            if (line.length() == 0) {
                line.append(w);
            } else if (line.length() + 1 + w.length() <= width) {
                line.append(' ').append(w);
            } else {
                out.add(padRight(line.toString(), width));
                line.setLength(0);
                line.append(w);
            }
        }
        if (line.length() > 0) out.add(padRight(line.toString(), width));
        if (out.isEmpty()) out.add(padRight("-", width));
        return out;
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) return s;
        StringBuilder sb = new StringBuilder(width);
        sb.append(s);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }
}