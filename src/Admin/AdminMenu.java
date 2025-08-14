package Admin;
import Constants.AppConstants;
import Dao.*;
public class AdminMenu {
    public static boolean adminValidator(String id,String password) {
        if(id.equals(Admin.getAdminId()) && password.equals(Admin.getAdminPassword())) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void adminMenu(){
        AppConstants.adminRun = true;
        try {
        while (AppConstants.adminRun) {
            System.out.println("\n=================================");
            System.out.println("|          Admin Menu           |");
            System.out.println("=================================");
            System.out.println("|  1. Edit Restaurants          |");
            System.out.println("|  2. View Restaurants          |");
            System.out.println("|  3. View Orders               |");
            System.out.println("|  4. View Transaction          |");
            System.out.println("|  5. Logout                    |");
            System.out.println("=================================");
            System.out.print("\nPlease enter your choice: ");
            switch (AppConstants.s.nextInt()) {
                case 1:
                    boolean b = true;
                    while (b) {
                        System.out.println("\n=================================");
                        System.out.println("|       Edit Restaurants        |");
                        System.out.println("=================================");
                        System.out.println("|  1. Add Restaurant            |");
                        System.out.println("|  2. Delete Restaurant         |");
                        System.out.println("|  3. Exit                      |");
                        System.out.println("=================================");
                        System.out.print("\nPlease enter your choice: ");
                        int n = AppConstants.s.nextInt();
                        if (n == 1) {
                            RestaurantDAO.addRestaurant();
                        } else if (n == 2) {
                            RestaurantDAO.deleteRestaurant();
                        } else if (n == 3) {
                            b = false;
                        } else {
                            System.out.println("\nInvalid Input !");
                        }
                    }
                    break;
                case 2:
                    RestaurantDAO.browseRestaurants();
                    break;
                case 3:
                    OrderDAO.viewOrderAndOrderItems();
                    break;
                case 4:
                    PaymentDAO.viewTransactions();
                    break;
                case 5:
                    System.out.println();
                    AppConstants.adminRun = false;
                    break;
                default:
                    System.out.println("Please choose valid option !");
                    break;
            }
        }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}