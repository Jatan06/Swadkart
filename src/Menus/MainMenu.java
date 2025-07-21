package Menus;
import Constants.AppConstants;
import Db.DBConnection;
import java.sql.*;
import java.util.*;
import Constants.AppConstants;
public class MainMenu {
    public static void run() {
        if(DBConnection.DbConnection()) {
            System.out.println(AppConstants.welcome);
            show();
        }
        else {
            return;
        }
    }
    public static void show() {
        try {
            AppConstants.show = true;
            while (AppConstants.show) {
                System.out.print("""
                        \nEnter "1" to Register(User only)
                        Enter "2" to login
                        Enter "3" to Exit
                        Enter : \s""");
                int n = AppConstants.s.nextInt();
                switch (n) {
                    case 1:
                        CustomerMenu.newCustomer();
                        break;
                    case 2:
                        System.out.print("\nEnter id : ");
                        String id = AppConstants.s.next();
                        System.out.print("Enter password : ");
                        String password = AppConstants.s.next();
                        if(id.charAt(0)=='U' || id.charAt(0)=='u') {
                            if (CustomerMenu.customerValidator(id,password)) {
                                CustomerMenu.customerMenu();
                            }
                            else {
                                System.out.println("\nInvalid User id or User password please try again !");
                            }
                        }
                        else if(id.charAt(0)=='A') {
                            if(AdminMenu.adminValidator(id,password)) {
                                AdminMenu.adminMenu();
                            }
                            else {
                                System.out.println("\nInvalid Admin id or Admin password please try again !");
                            }
                        }
                        else {
                            System.out.println("\nInvalid id !");
                        }
                        // Logic for sending validator that customer or admin that returns string as "Customer" or "Admin" and using if-else to call methods respectively.
                        break;
                    case 3:
                        System.out.println("\nSwadKart signing off... pet full, mood chill! \uD83D\uDE0E\uD83C\uDF55");
                        AppConstants.show = false;
                        break;
                    default:
                        System.out.println("Invalid Choice Please try again !");
                        break;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception :- InputMismatchException. Please provide valid input.");
        }
    }
}