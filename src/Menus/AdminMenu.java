package Menus;
import java.sql.*;
import java.io.*;
import Constants.*;
import Utils.*;
import Services.*;
import Dao.*;
import Db.*;
import Menus.*;
import Models.*;
import java.util.*;
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
            \n======= Admin Menu =======
                1. Edit Restaurants
                2. View Restaurants
                3. View Orders
                4. Logout
            ==========================
            Please enter your choice: """);
            switch (AppConstants.s.nextInt()) {
                case 1:
                    boolean b = true;
                    while (b) {
                        System.out.print("""               
                        \n==== Edit Restaurants ==== |
                        1. Add Restaurant            |
                        2. Delete Restaurant         |
                        3. Exit
                       ==========================
                       Please enter your choice: """);
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