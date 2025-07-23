package Menus;
import Constants.AppConstants;
import Db.DBConnection;
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
            AppConstants.show = true;
            while (AppConstants.show) {
            try {
                    System.out.print("""               
                   ========== Main Menu =========
                    1. New Customer Registration
                    2. Existing Customer Login
                    3. Exit
                   ==============================
                   
                   Please enter your choice:  \s""");
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
                            else if(id.charAt(0)=='A' || id.charAt(0)=='a') {
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
                catch (Exception e) {
                    System.out.println("\nException :- InputMismatchException. Please provide valid input.");
                    AppConstants.s.nextLine();
                }
            }
        }
    }