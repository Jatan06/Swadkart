package Menus;
import java.sql.*;
import Constants.*;
import Utils.*;
import Services.*;
import Dao.*;
import Session.*;
import Ds.*;

public class CustomerMenu {
    public static void newCustomer() {
        boolean flag = true;
        while (flag) {
            try {
                displayNewCustomerMenu();
                int choice = AppConstants.s.nextInt();
                switch (choice) {
                    case 1:
                        System.out.print("\n\t\t\t"+AppConstants.BG_ANSI_BLACK+"============== Verification =============="+AppConstants.ANSI_RESET+"\n");
                        int users = UserDAO.countUsers();
                        users++;
                        String password = "";
                        System.out.print("\nEnter Phone No. : ");
                        String ph_no = AppConstants.s.next().trim();
                        // Validate mobile number using Validators class
                        while (!Validators.validateMobileNumber(ph_no)) {
                            System.out.print(AppConstants.TEXT_ANSI_RED+"\nEnter valid mobile number (e.g., 9876543210): "+AppConstants.ANSI_RESET);
                            ph_no = AppConstants.s.next().trim();
                        }
                        ph_no = "+91"+ph_no;
                        while(true) {
                            if(UserDAO.phoneNumberExists(ph_no)) {
                                System.out.print(AppConstants.TEXT_ANSI_RED+"\nPhone number already exists. Please enter a different number : "+AppConstants.ANSI_RESET);
                                ph_no = "+91" + AppConstants.s.next().trim();
                            }
                            else {
                                break;
                            }
                        }
                        // Generate OTP once
                        String generatedOTP = OTPService.generateOTP();
                        // Send the same OTP
                        if(OTPService.sendOTP(ph_no, generatedOTP)) {
                            // Prompt user to enter OTP
                            System.out.print("\nEnter the OTP sent to your phone: ");
                            String userOTP = AppConstants.s.next().trim();
                            AppConstants.s.nextLine();
                            int otpAttempts = 3;
                            boolean otpValidated = false;
                            while (otpAttempts > 0) {
                                if (userOTP.equals(generatedOTP)) {
                                    otpValidated = true;
                                    System.out.println("\n✅ Phone number verified successfully!");
                                    System.out.print("Enter password : ");
                                    password = AppConstants.s.nextLine().trim();
                                    while (!Validators.validatePassword(password)) {
                                        System.out.print(AppConstants.TEXT_ANSI_RED+"\nEnter valid password :-  "+AppConstants.ANSI_RESET);
                                        password = AppConstants.s.nextLine().trim();
                                        if (!Validators.validatePassword(password)) {
                                            System.out.println(AppConstants.TEXT_ANSI_RED+"\nPassword must be at least 8 characters long and contain at least one digit, one lowercase letter and one '@' symbol."+AppConstants.ANSI_RESET);
                                        }
                                    }
                                    System.out.print("\nRe-Enter password :- ");
                                    String repass = AppConstants.s.next().trim();
                                    while (true) {
                                        if(!password.equals(repass)) {
                                            System.out.print(AppConstants.TEXT_ANSI_RED+"Please enter valid password which you have set :- "+AppConstants.ANSI_RESET);
                                            repass = AppConstants.s.next();
                                        }
                                        else {
                                            otpAttempts = 0;
                                            break;
                                        }
                                    }
                                } else {
                                    if (--otpAttempts > 0) {
                                        System.out.print("\n❌ "+AppConstants.TEXT_ANSI_RED+"Incorrect OTP. Please try again (" + otpAttempts + " attempts left): "+AppConstants.ANSI_RESET);
                                        userOTP = AppConstants.s.nextLine().trim();
                                    } else {
                                        System.out.println("\n🚫"+AppConstants.TEXT_ANSI_RED+" Phone number verification failed. Registration terminated."+AppConstants.ANSI_RESET+"\n");
                                        return;
                                    }
                                }
                            }
                        }
                        else {
                            System.out.println("\n🚫"+AppConstants.TEXT_ANSI_RED+" Phone number verification failed. Registration terminated."+AppConstants.ANSI_RESET+"\n");
                            return;
                        }
                        System.out.print("\n\t\t\t"+AppConstants.BG_ANSI_BLACK+"============== Registration =============="+AppConstants.ANSI_RESET+"\n");
                        System.out.print("\nEnter Name : ");
                        String user_Name = AppConstants.s.nextLine();
                        while (!Validators.validateName(user_Name)) {
                            System.out.print("Enter valid name : ");
                            user_Name = AppConstants.s.nextLine();
                        }
                        System.out.print("Enter email : ");
                        String email = AppConstants.s.nextLine();
                        while (!Validators.validateEmail(email)) {
                            System.out.print("Enter valid email id : ");
                            email = AppConstants.s.nextLine().trim();
                        }
                        AppConstants.s.nextLine();
                        System.out.print("Enter address : ");
                        String address = AppConstants.s.nextLine();
                        while (!Validators.validateAddress(address)) {
                            System.out.print("Enter valid address : ");
                            address = AppConstants.s.nextLine();
                        }
                        String id;
                        if (users > 0 && users < 100) {
                            id = "u-00" + users;
                        } else if (users >= 100 && users < 1000) {
                            id = "u-0" + users;
                        } else {
                            id = "u-" + users;
                        }
                        UserDAO.insertNewUser(id, user_Name, email, ph_no, address,password);
                        SessionManager.NewRegistration(id,true);
                        CustomerMenu.customerMenu(id);
                    case 2:
                        System.out.println("\nReturning to Main Menu...\n");
                        flag = false;
                        break;
                    default:
                        System.out.println(AppConstants.TEXT_ANSI_RED+"\nInvalid choice. Please try again.\n"+AppConstants.ANSI_RESET);
                        break;
                }
            } catch (Exception e) {
                System.out.println(AppConstants.TEXT_ANSI_RED+"\nError Please try again!"+AppConstants.ANSI_RESET);
                AppConstants.s.nextLine();
            }
        }
    }

    public static boolean forgotPassword(String id) {
        System.out.print("\nEnter Phone No. : ");
        String ph_no = "+91" + AppConstants.s.nextLine().trim();
        while (true) {
            try {
                if (UserDAO.changePass(id, ph_no)) {
                    String generatedOTP = PasswordChangeOTPService.generateOTP();
                    if (PasswordChangeOTPService.sendOTP(ph_no, generatedOTP)) {
                        int otpAttempts = 3;
                        boolean otpValidated = false;
                        while (otpAttempts > 0) {
                            System.out.print("\nEnter the OTP sent to your phone: ");
                            String userOTP = AppConstants.s.nextLine().trim();
                            if (userOTP.equals(generatedOTP)) {
                                otpValidated = true;
                                break;
                            } else {
                                otpAttempts--;
                                if (otpAttempts > 0) {
                                    System.out.println("❌ Incorrect OTP. Please try again (" + otpAttempts + " attempts left).");
                                }
                            }
                        }
                        if (!otpValidated) {
                            System.out.println("\n🚫 Phone number verification failed. Operation terminated.\n");
                            return false;
                        }
                        System.out.println("\n✅ Phone number verified successfully!");
                        // New password setup with validation
                        String new_password;
                        while (true) {
                            System.out.print("\nEnter new password : ");
                            new_password = AppConstants.s.nextLine();
                            if (Validators.validatePassword(new_password)) {
                                break;
                            }
                            System.out.println("\nPassword must be at least 8 characters long and contain at least one digit, one lowercase letter and one '@' symbol.");
                        }
                        while (true) {
                            System.out.print("\nRe-Enter password :- ");
                            String repass = AppConstants.s.nextLine();
                            if (new_password.equals(repass)) {
                                UserDAO.setNewPass(id, new_password);
                                System.out.println("\n✅ Password has been updated successfully.");
                                return true;
                            } else {
                                System.out.println("Passwords do not match. Please try again.");
                            }
                        }
                    } else {
                        System.out.println("\n🚫 Phone number verification failed. Operation terminated.\n");
                        return false;
                    }
                } else {
                    System.out.println("Phone number " + ph_no + " does not match with your id " + id + ".");
                    System.out.print("Enter 'b' to go back, or any other key to try again: ");
                    String choice = AppConstants.s.nextLine().trim();
                    if (choice.equalsIgnoreCase("b")) {
                        return false;
                    }
                    // If a user wants to try again, ask for the phone again
                    System.out.print("\nEnter Phone No. : ");
                    ph_no = "+91" + AppConstants.s.nextLine().trim();
                    // continue the while(true)
                }
            } catch (java.sql.SQLException se) {
                System.out.println("A database error occurred while resetting the password. Please try again later.");
                return false;
            } catch (RuntimeException re) {
                // Preserve runtime exceptions like NPE/illegal state with a user-friendly path
                System.out.println("Unexpected error occurred while processing forgot password. Please try again.");
                return false;
            } catch (Exception e) {
                // Do not kill the app; fail this operation gracefully
                System.out.println("An error occurred while processing your request. Please try again.");
                return false;
            }
        }
    }

    public static boolean validateId(String id) {
        String sql = "SELECT id FROM users WHERE id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // user id not found
                    return true;
                }
                else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Exception arise in Menus/CustomerMenu/ValidateId(String id).");
            return false;
        }
    }

    public static String getId(String mobile_no) {
        String sql = "SELECT id FROM users WHERE phone_number = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, mobile_no);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("id");
                else return null;
            }
        } catch (Exception e) {
            System.out.println("Exception arise in Menus/CustomerMenu/getId(String mono).");
            return null;
        }
    }

    // Safer, precise validator that only logs session on success
    public static boolean customerValidator(String id, String password) throws Exception {
        String sql = "SELECT password FROM users WHERE id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                String storedPassword = rs.getString(1);
                boolean ok = storedPassword != null && storedPassword.equals(password);
                if (ok) {
                    AppConstants.customerValid = true;
                    SessionManager.Login(id);
                    return true;
                } else {
                    AppConstants.customerValid = false;
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception arise in Menus/customerMenu/customerValidator(String id,String password).");
            return false;
        }
    }

    public static void customerMenu(String id) {
        AppConstants.run = true;
        while (AppConstants.run) {
            try {
                displayMenu();
                int option = getUserInput();
                processAction(option, id);
            } catch (java.util.InputMismatchException ime) {
                System.out.println("Invalid input. Please enter a number.");
                AppConstants.s.nextLine(); // clear buffer
            } catch (Exception e) {
                System.out.println("Exception arise at CustomerMenu/customerMenu at line 303.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t*************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ User Menu +\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t*************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t1. Browse Restaurants\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t2. Browse Dishes\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t3. Add To Cart\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t4. View Cart\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t5. Place Order\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t6. Order History\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t7. Profile\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t8. Logout\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }

    private static void displayNewCustomerMenu() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-------------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t*****************\t\t\t\t    |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Customer Menu +\t\t\t\t    |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t*****************\t\t\t\t    |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t1. Register\t\t\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t2. Back\t\t\t\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-------------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }

    private static int getUserInput() {
        while (true) {
            if (AppConstants.s.hasNextInt()) {
                int v = AppConstants.s.nextInt();
                AppConstants.s.nextLine(); // consume trailing newline to make nextLine safe everywhere
                return v;
            } else {
                System.out.println(AppConstants.ERR_INVALID_INPUT);
                AppConstants.s.next();
            }
        }
    }

    private static void processAction(int option,String id) throws Exception {
        switch (option) {
            case 1 -> RestaurantDAO.browseRestaurants();
            case 2 -> DishDAO.browseDishes();
            case 3 -> UserService.addToCart();
            case 4 -> UserService.Cart.display();
            case 5 -> OrderDAO.placeOrder(id);
            case 6 -> OrderDAO.viewOrderAndOrderItems(id);
            case 7 -> UserDAO.profile(id);
            case 8 -> {
                System.out.println();
                UserService.Cart = null;
                AppConstants.run = false;
            }
            default -> System.out.println(AppConstants.ERR_INVALID_INPUT);
        }
    }
}