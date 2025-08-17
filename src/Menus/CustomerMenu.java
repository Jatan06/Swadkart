package Menus;
import java.sql.*;
import Constants.*;
import Utils.*;
import Services.*;
import Dao.*;
import Session.*;
import Ds.*;

public class CustomerMenu {
    //SessionManager.NewRegistration(String id,Boolean isCreated); // (Call it in catch block also)-try isCreated = true,catch isCreated = false.
    public static void newCustomer() {
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("\n\n===================================");
                System.out.println("|         Customer Menu           |");
                System.out.println("===================================");
                System.out.println("|  1. Register New Account        |");
                System.out.println("|  2. Return to Main Menu         |");
                System.out.println("===================================");
                System.out.print("Please select an option: ");
                int choice = AppConstants.s.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("\n========= Verification =========");
                        int users = UserDAO.countUsers();
                        users++;
                        String password = "";
                        System.out.print("Enter Phone No. : ");
                        String ph_no = "+91" + AppConstants.s.next();
                        while(true) {
                            if(UserDAO.phoneNumberExists(ph_no)) {
                                System.out.print("\nPhone number already exists. Please enter a different number : ");
                                ph_no = "+91" + AppConstants.s.next().trim();
                            }
                            else {
                                break;
                            }
                        }
                        // Validate mobile number using Validators class
                        while (!Validators.validateMobileNumber(ph_no)) {
                            System.out.print("\nEnter valid mobile number (e.g., 9876543210): ");
                            ph_no = "+91" + AppConstants.s.next().trim();
                        }
                        // Generate OTP once
                        String generatedOTP = OTPService.generateOTP();
                        // Send the same OTP
                        if(OTPService.sendOTP(ph_no, generatedOTP)) {
                            // Prompt user to enter OTP
                            System.out.print("\nEnter the OTP sent to your phone: ");
                            String userOTP = AppConstants.s.next();
                            AppConstants.s.nextLine();
                            int otpAttempts = 3;
                            boolean otpValidated = false;
                            while (otpAttempts > 0) {
                                if (userOTP.equals(generatedOTP)) {
                                    otpValidated = true;
                                    System.out.println("\n✅ Phone number verified successfully!");
                                    System.out.print("\nEnter password : ");
                                    password = AppConstants.s.nextLine();
                                    while (!Validators.validatePassword(password)) {
                                        System.out.println("Enter valid password :-  ");
                                        password = AppConstants.s.nextLine();
                                        if (!Validators.validatePassword(password)) {
                                            System.out.println("\nPassword must be at least 8 characters long and contain at least one digit, one lowercase letter and one '@' symbol.");
                                        }
                                    }
                                    System.out.print("\nRe-Enter password :- ");
                                    String repass = AppConstants.s.next();
                                    while (true) {
                                        if(!password.equals(repass)) {
                                            System.out.println("Please enter valid password which you have set :- ");
                                            repass = AppConstants.s.next();
                                        }
                                        else {
                                            break;
                                        }
                                    }
                                } else {
                                    if (--otpAttempts > 0) {
                                        System.out.print("\n❌ Incorrect OTP. Please try again (" + otpAttempts + " attempts left): ");
                                        userOTP = AppConstants.s.nextLine();
                                    } else {
                                        System.out.println("\n🚫 Phone number verification failed. Registration terminated.\n");
                                        return;
                                    }
                                }
                            }
                        }
                        else {
                            System.out.println("\n🚫 Phone number verification failed. Registration terminated.\n");
                            return;
                        }
                        System.out.println("\n========= Registration =========");
                        System.out.print("Enter Name : ");
                        String user_Name = AppConstants.s.nextLine();
                        while (true) {
                            if (Validators.validateName(user_Name))
                                break;
                            else {
                                System.out.println("Enter valid name : ");
                                user_Name = AppConstants.s.next();
                            }
                        }
                        System.out.print("Enter email : ");
                        String email = AppConstants.s.nextLine();
                        while (true) {
                            if (Validators.validateEmail(email))
                                break;
                            else {
                                System.out.println("Enter valid email id :");
                                email = AppConstants.s.next();
                            }
                        }
                        System.out.print("Enter address : ");
                        String address = AppConstants.s.nextLine();
                        while (true) {
                            if (Validators.validateAddress(address)) {
                                break;
                            }
                            else {
                                System.out.print("\nEnter valid address : ");
                                address = AppConstants.s.nextLine();
                            }
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
                        System.out.println("\nInvalid choice. Please try again.\n");
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException("An error occurred in the Customer Menu in new customer ", e);
            }
        }
    }

    public static boolean forgotPassword(String id) {
        return false;
    }

    // Safer, precise validator that only logs session on success
    public static boolean customerValidator(String id, String password) throws Exception {
        String sql = "SELECT password FROM users WHERE id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    // user id not found
                    return false;
                }
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
                // Surface the underlying cause clearly and stop the loop
                e.printStackTrace();
                throw new RuntimeException("An error occurred in the Customer Menu loop while processing an action", e);
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=================================");
        System.out.println("|              Menu              |");
        System.out.println("=================================");
        System.out.println("|  1. Browse Restaurants         |");
        System.out.println("|  2. Browse Dishes              |");
        System.out.println("|  3. Add to Cart                |");
        System.out.println("|  4. View Cart                  |");
        System.out.println("|  5. Place Order                |");
        System.out.println("|  6. View Order History         |");
        System.out.println("|  7. Profile                    |");
        System.out.println("|  8. Log Out                    |");
        System.out.println("=================================");
        System.out.print("\nPlease select an option: ");
    }

    private static int getUserInput() {
        while (true) {
            if (AppConstants.s.hasNextInt()) {
                int v = AppConstants.s.nextInt();
                AppConstants.s.nextLine(); // consume trailing newline to make nextLine safe everywhere
                return v;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                AppConstants.s.next(); // Clear invalid token
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
                Services.UserService.Cart = null;
                AppConstants.run = false;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
}
