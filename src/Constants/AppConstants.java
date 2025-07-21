package Constants;
import java.sql.*;
import java.util.*;
public class AppConstants {

    public static Scanner s = new Scanner(System.in);

    public static boolean run;
    public static boolean show;
    public static boolean customerValid;

    // Welcome
    public static String welcome = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSwadkart - Food Ordering System\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEk command, anek swaad!";

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

    // ID Generation Constants
    public static final String RESTAURANT_ID_PREFIX = "R-";
    public static final String DISH_ID_PREFIX = "D-";
    public static final String ORDER_ID_PREFIX = "ORD-";
    public static final String PAYMENT_ID_PREFIX = "PAY-";

    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;
    public static final double MIN_PRICE = 0.01;
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 99;

    // System Messages
    public static final String MSG_INVALID_CHOICE = "Invalid choice. Please try again.";
    public static final String MSG_OPERATION_SUCCESS = "Operation completed successfully!";
    public static final String MSG_NO_DATA = "No data found.";

    // Error Messages
    public static final String ERR_INVALID_INPUT = "Invalid input. Please try again.";
    public static final String ERR_USER_NOT_FOUND = "User not found.";
    public static final String ERR_INVALID_CREDENTIALS = "Invalid credentials.";
    public static final String ERR_RESTAURANT_NOT_FOUND = "Restaurant not found.";
    public static final String ERR_DISH_NOT_FOUND = "Dish not found.";
    public static final String ERR_ORDER_NOT_FOUND = "Order not found.";

    // Success Messages
    public static final String SUCCESS_REGISTRATION = "Registration successful!";
    public static final String SUCCESS_LOGIN = "Login successful!";
    public static final String SUCCESS_RESTAURANT_ADDED = "Restaurant added successfully!";
    public static final String SUCCESS_DISH_ADDED = "Dish added successfully!";
    public static final String SUCCESS_ORDER_PLACED = "Order placed successfully!";
    public static final String SUCCESS_PAYMENT = "Payment completed successfully!";
    public static final String SUCCESS_REVIEW = "Review submitted successfully!";
}
