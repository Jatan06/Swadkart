package Menus;
import Constants.AppConstants;
import Dao.RestaurantDAO;
public class AdminMenu {
    static boolean adminValidator(String id, String password) {
        return id.equals(AppConstants.admin_id) && password.equals(AppConstants.admin_password);
    }
    static void adminMenu() {
        AppConstants.adminRun = true;
        try {
            while (AppConstants.adminRun) {
                displayMainMenu();
                int option = getUserInput();

                switch (option) {
                    case 1 -> editRestaurantsSubmenu();
                    case 2 -> RestaurantDAO.browseRestaurants();
                    case 3 -> System.out.println("\nFeature under development."); // Placeholder for "View Orders"
                    case 4 -> AppConstants.adminRun = false;
                    default -> System.out.println(AppConstants.MSG_INVALID_CHOICE);
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while processing Admin Menu: " + e.getMessage());
        }
    }
    private static void displayMainMenu() {
        System.out.println("""
                
                ==== Admin Menu ====
                1. Edit Restaurants
                2. View Restaurants
                3. View Orders
                4. Log Out
                ====================
                Please enter your choice: 
                """);
    }
    private static void editRestaurantsSubmenu() throws Exception {
        boolean editing = true;
        while (editing) {
            displayEditRestaurantMenu();
            int subOption = getUserInput();

            switch (subOption) {
                case 1 -> {
                    RestaurantDAO.addRestaurant();
                    System.out.println(AppConstants.SUCCESS_RESTAURANT_ADDED);
                }
                case 2 -> {
                    RestaurantDAO.deleteRestaurant();
                    System.out.println("Restaurant deleted successfully!"); // Assuming no constant exists for this.
                }
                case 3 -> editing = false;
                default -> System.out.println(AppConstants.MSG_INVALID_CHOICE);
            }
        }
    }
    private static void displayEditRestaurantMenu() {
        System.out.println("""
                
                ==== Edit Restaurants ====
                1. Add Restaurant
                2. Delete Restaurant
                3. Go Back
                =========================
                Please enter your choice: 
                """);
    }
    private static int getUserInput() {
        while (true) {
            if (AppConstants.s.hasNextInt()) {
                return AppConstants.s.nextInt();
            } else {
                System.out.println(AppConstants.ERR_INVALID_INPUT);
                AppConstants.s.next(); // Clears invalid input
            }
        }
    }
}