package Constants;
import java.sql.*;
import java.util.*;
public class AppConstants {

    public static Scanner s = new Scanner(System.in);

    public static boolean adminRun = true;
    public static boolean run;
    public static boolean show;
    public static boolean customerValid;

    // Welcome
    public static String welcome = """
            
            \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\uD83C\uDF2E\uD83C\uDF55\uD83C\uDF71\uD83C\uDF5CSwadkart - Food Ordering System\uD83C\uDF5B\uD83C\uDF54\uD83E\uDD57\uD83C\uDF69
            \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\uD83E\uDD62 Where every bite meets delight!
            
            """;

    // Database Variables
    public static Connection connection;

    // Payment Methods
    public static final String PAYMENT_CASH = "cash";
    public static final String PAYMENT_CARD = "card";
    public static final String PAYMENT_UPI = "upi";

    // Payment Status
    public static final String PAYMENT_STATUS_PENDING = "pending";
    public static final String PAYMENT_STATUS_COMPLETED = "completed";
    public static final String PAYMENT_STATUS_FAILED = "failed";

    // Error Messages
    public static final String ERR_INVALID_INPUT = "Invalid input. Please try again.";
    public static final String ERR_USER_NOT_FOUND = "\nUser not found.";
    public static final String ERR_INVALID_CREDENTIALS = "\nInvalid credentials.";
    public static final String ERR_RESTAURANT_NOT_FOUND = "\nRestaurant not found.";
    public static final String ERR_DISH_NOT_FOUND = "\nDish not found.";
    public static final String ERR_ORDER_NOT_FOUND = "\nOrder not found.";

    // Success Messages
    public static final String SUCCESS_REGISTRATION = "\nRegistration successful!";
    public static final String SUCCESS_LOGIN = "Login successful!";
    public static final String SUCCESS_RESTAURANT_ADDED = "\nRestaurant added successfully!";
    public static final String SUCCESS_DISH_ADDED = "\nDish added successfully!";
    public static final String SUCCESS_ORDER_PLACED = "\nOrder placed successfully!";
    public static final String SUCCESS_PAYMENT = "\nPayment completed successfully!";
    public static final String SUCCESS_REVIEW = "\nReview submitted successfully!";

    //SessionManager Messages
    public static final String LOG_SUCCESS = "Login Successful";
    public static final String LOG_FAIL = "Login Unsuccessful";
    public static final String NEW_USER_SUCCESS  = "New User Added";
    public static final String NEW_USER_FAIL  = "Error while creating new User";
    public static final String HEADER  = "          ============   SWADKART   ============";

    public final static String ANSI_RESET = "\u001B[0m";
    public final static String TEXT_ANSI_BLACK = "\u001B[30m";
    public final static String TEXT_ANSI_RED = "\u001B[31m";
    public final static String TEXT_ANSI_GREEN = "\u001B[32m";
    public final static String TEXT_ANSI_YELLOW = "\u001B[33m";
    public final static String TEXT_ANSI_BLUE = "\u001B[34m";
    public final static String TEXT_ANSI_PURPLE = "\u001B[35m";
    public final static String TEXT_ANSI_CYAN = "\u001B[36m";
    public final static String TEXT_ANSI_WHITE = "\u001B[37m";
    public final static String BG_ANSI_BLACK = "\u001B[40m";
    public final static String BG_ANSI_RED = "\u001B[41m";
    public final static String BG_ANSI_GREEN = "\u001B[42m";
    public final static String BG_ANSI_YELLOW = "\u001B[43m";
    public final static String BG_ANSI_BLUE = "\u001B[44m";
    public final static String BG_ANSI_PURPLE = "\u001B[45m";
    public final static String BG_ANSI_CYAN = "\u001B[46m";
    public final static String BG_ANSI_WHITE = "\u001B[47m";
}
