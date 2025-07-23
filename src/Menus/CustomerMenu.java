package Menus;
import java.sql.*;
import Constants.*;
import Utils.*;
import Services.*;
import Dao.*;
import Session.*;
public class CustomerMenu {
        //SessionManager.NewRegistration(String id,Boolean isCreated); // (Call it in catch block also)-try isCreated  = true,catch isCreated = false.
    public static void newCustomer() throws Exception {
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("\n\n===================================");
                System.out.println("|         Customer Menu           |");
                System.out.println("===================================");
                System.out.println("|  1. Register New Account         |");
                System.out.println("|  2. Return to Main Menu          |");
                System.out.println("===================================");
                System.out.print("Please select an option: ");
                int choice = AppConstants.s.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("\n========= Verification =========");
                        // Logic for registering a new account
                        int users = UserDAO.countUsers();
                        users++;
                        System.out.print("Enter Phone No. : ");
                        AppConstants.s.nextLine(); // This seems unnecessary unless you're clearing a buffer
                        String ph_no = AppConstants.s.nextLine();
                        ph_no = "+91" + ph_no;
                        // Validate mobile number using Validators class
                        while (!Validators.validateMobileNumber(ph_no)) {
                            System.out.print("\nEnter valid mobile number (e.g., +919876543210): ");
                            ph_no = AppConstants.s.nextLine();
                        }
                        // Generate OTP once
                        String generatedOTP = OTPService.generateOTP();
                        // Send the same OTP
                        if(OTPService.sendOTP(ph_no, generatedOTP)) {
                            // Prompt user to enter OTP
                            System.out.print("\nEnter the OTP sent to your phone: ");
                            String userOTP = AppConstants.s.nextLine();
                            int otpAttempts = 3;
                            boolean otpValidated = false;
                            while (otpAttempts > 0) {
                                if (userOTP.equals(generatedOTP)) {
                                    otpValidated = true;
                                    System.out.println("\n✅ Phone number verified successfully!");
                                    break;
                                } else {
                                    otpAttempts--;
                                    if (otpAttempts > 0) {
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
                            if (Validators.validateAddress(address))
                                break;
                            else {
                                System.out.print("\nEnter valid address : ");
                                address = AppConstants.s.nextLine();
                            }
                        }
                        String id = "";
                        if (users > 0 && users < 100) {
                            id = "u-00" + users;
                        } else if (users >= 100 && users < 1000) {
                            id = "u-0" + users;
                        } else {
                            id = "u-" + users;
                        }
                        UserDAO.insertNewUser(id, user_Name, email, ph_no, address);
                        SessionManager.NewRegistration(id,true);
                        SpeakTextService.speak("Registration successful");
                        break;
                    case 2:
                        System.out.println("\nReturning to Main Menu...\n");
                        flag = false;
                        break;
                    default:
                        System.out.println("\nInvalid choice. Please try again.\n");
                }
            } catch (Exception e) {
                System.out.println("\nException: InputMismatchException in new Customer Menu. Please provide valid input.");
                AppConstants.s.nextLine(); // Clear scanner buffer
            }
        }
    }
    public static boolean customerValidator(String id, String password) throws Exception {
        Statement s = AppConstants.connection.createStatement();
        ResultSet rs = s.executeQuery("select * from users;");
        while (rs.next()) {
            if (rs.getString(1).equals(id) && rs.getString(7).equals(password)) {
                AppConstants.customerValid = true;
                break;
            } else {
                AppConstants.customerValid = false;
            }
        }
        SessionManager.Login(id);
        return AppConstants.customerValid;
    }
    public static void customerMenu() {
        try {
            AppConstants.run = true;
            while (AppConstants.run) {
                displayMenu();
                int option = getUserInput();
                processAction(option);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred in the Customer Menu in customer validator ", e);
        }
    }
    private static void displayMenu() {
        System.out.println("\n=================================");
        System.out.println("|              Menu              |");
        System.out.println("=================================");
        System.out.println("|  1. Browse Restaurants         |");
        System.out.println("|  2. Browse Dishes by Restaurant|");
        System.out.println("|  3. Browse Dishes by Cuisine   |");
        System.out.println("|  4. Add to Cart                |");
        System.out.println("|  5. View Cart                  |");
        System.out.println("|  6. Place Order                |");
        System.out.println("|  7. View Order History         |");
        System.out.println("|  8. Profile                    |");
        System.out.println("|  9. Log Out                    |");
        System.out.println("=================================");
        System.out.print("\nPlease enter your choice: ");
    }
    private static int getUserInput() {
        while (true) {
            if (AppConstants.s.hasNextInt()) {
                return AppConstants.s.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                AppConstants.s.next(); // Clear invalid input
            }
        }
    }
    private static void processAction(int option) throws Exception {
        switch (option) {
            case 1 -> RestaurantDAO.browseRestaurants();
            case 2 -> {
                System.out.print("Enter Restaurant name: ");
                AppConstants.s.nextLine();
                String resName = AppConstants.s.nextLine();
                DishDAO.browseDishesByRestaurant(resName);
            }
            case 3 -> {
                System.out.print("Enter dish category: ");
                DishDAO.browseDishesByCuisine(AppConstants.s.next());
                AppConstants.s.nextLine();
            }
            case 4 -> OrderDAO.addToCart();
            case 5 -> OrderDAO.viewCart();
            case 6 -> PaymentDAO.placeOrder();
            case 7 -> OrderDAO.orderHistory();
            case 8 -> UserDAO.profile();
            case 9 -> {
                System.out.println();
                AppConstants.run = false;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
}