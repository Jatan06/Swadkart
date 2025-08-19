package Admin;
import Constants.AppConstants;
import Dao.*;
import Services.LoginService;
import Services.ReviewService;
public class AdminMenu {
    public static boolean adminValidatorId(String id) {
        return id.equals(Admin.getAdminId());
    }

    public static boolean adminValidatorPassword(String password) {
        return password.equals(Admin.getAdminPassword());
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
                System.out.println("|  5. View Reviews              |");
                System.out.println("|  6. View Logins               |");
                System.out.println("|  7. Logout                    |");
                System.out.println("=================================");
                System.out.print("\nPlease enter your choice: ");
                try {
                    switch (AppConstants.s.nextInt()) {
                        case 1:
                            boolean b = true;
                            while (b) {
                                System.out.println("\n=================================");
                                System.out.println("|       Edit Restaurants        |");
                                System.out.println("=================================");
                                System.out.println("|  1. Add Restaurant            |");
                                System.out.println("|  2. Delete Restaurant         |");
                                System.out.println("|  3. Back                      |");
                                System.out.println("=================================");
                                System.out.print("\nPlease enter your choice: ");
                                try {
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
                                } catch (Exception e) {
                                    System.out.println("Enter numbers only.");
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
                            ReviewService.showReviewMenu();
                            break;
                        case 6:
                            LoginService.displayLogins();
                            break;
                        case 7:
                            System.out.println();
                            AppConstants.adminRun = false;
                            break;
                        default:
                            System.out.println("Please choose valid option !");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Enter number only.");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception :- "+e.getMessage());
        }
    }
}