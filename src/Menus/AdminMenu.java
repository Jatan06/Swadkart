package Menus;

import Constants.AppConstants;
import Dao.RestaurantDAO;

public class AdminMenu {
    static boolean adminValidator(String id,String password) {
        if(id.equals(AppConstants.admin_id) && password.equals(AppConstants.admin_password)) {
            return true;
        }
        else {
            return false;
        }
    }
    static void adminMenu(){
        AppConstants.adminRun = true;
        try {
        while (AppConstants.adminRun) {
            System.out.print("""
                    \nEnter "1" to Edit Restaurants.
                    Enter "2" to View Restaurants.
                    Enter "3" to View Orders.
                    Enter "4" to Logout.
                    \nEnter : \s""");
            switch (AppConstants.s.nextInt()) {
                case 1:
                    boolean b = true;
                    while (b) {
                        System.out.println("Enter \"1\" to Add Restaurant.");
                        System.out.println("Enter \"2\" to Delete Restaurant.");
                        System.out.println("Enter \"3\" to Exit.");
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
                    break;
                case 4:
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
