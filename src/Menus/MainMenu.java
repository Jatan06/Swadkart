package Menus;
import Constants.AppConstants;
import Db.DBConnection;
import Services.*;
import Services.SpeakTextService;

import java.sql.Statement;

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
                System.out.println("===================================");
                System.out.println("|           Main Menu             |");
                System.out.println("===================================");
                System.out.println("|  1. New Customer Registration   |");
                System.out.println("|  2. Existing Customer Login     |");
                System.out.println("|  3. Exit                        |");
                System.out.println("===================================");
                System.out.print("\nPlease select an option: ");
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
                            //Thank you for using SwadKart. Goodbye!
                            SpeakTextService.speak("SwadKart signing off... pet full, mood chill!");
                            Thread.sleep(4000);
                            System.out.println("\nSwadKart signing off... pet full, mood chill! \uD83D\uDE0E\uD83C\uDF55");
                            AppConstants.show = false;
                            break;
                        default:
                            System.out.println("Invalid Choice Please try again !");
                            break;
                    }
                }
                catch (Exception e) {
                e.printStackTrace();
//                    System.out.println("\nException :- InputMismatchException. Please provide valid input.");
                    AppConstants.s.nextLine();
                }
            }
        }
    }