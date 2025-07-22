package Menus;
import java.sql.*;
import java.io.*;
import Constants.*;
import Utils.*;
import Services.*;
import Dao.*;
import Db.*;
import Menus.*;
import Session.*;
import Models.*;
import java.util.*;
public class CustomerMenu {
    static void newCustomer() {
        // Add logic for a new customer here
        //SessionManager.NewRegistration(String id,Boolean isCreated); // (Call it in catch block also)-try isCreated  = true,catch isCreated = false.

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
    static void customerMenu() {
        try {
            AppConstants.run = true;
            while (AppConstants.run) {
                displayMenu();
                int option = getUserInput();
                processAction(option);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred in the Customer Menu", e);
        }
    }
    private static void displayMenu() {
        System.out.print("""
          \n======== Swadkart Menu ==========
            1. Browse Restaurants
            2. Browse Dishes by Restaurant
            3. Browse Dishes by Cuisine
            4. Add to Cart
            5. View Cart
            6. Place Order
            7. View Order History
            8. Log Out
          =================================
          Please enter your choice: 
            """);
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
                DishDAO.browseDishesByCategory(AppConstants.s.next());
                AppConstants.s.nextLine();
            }
            case 4 -> OrderDAO.addToCart();
            case 5 -> OrderDAO.viewCart();
            case 6 -> PaymentDAO.placeOrder();
            case 7 -> OrderDAO.orderHistory();
            case 8 -> AppConstants.run = false;
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
}