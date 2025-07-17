package Menus;
import Constants.AppConstants;
import java.util.*;
public class MainMenu {
    static {
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSwadkart - Food Ordering System\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEk command, anek swaad!");
    };
    static Scanner s = new Scanner(System.in);
    public static void show() {
        try {
            AppConstants.bool = true;
            while (AppConstants.bool) {
                System.out.print("\nEnter \"" + 1 + "\" for Register\nEnter \"" + 2 + "\" for login\nEnter \"" + 3 + "\" for Exit\n\nEnter :- ");
                // 1 for register 2 for login (Admin & User) 3 for exit.
                int n = s.nextInt();
                switch (n) {
                    case 1:
                        CustomerMenu.newCustomer();
                        // This calls registration (newCustomer) method for a new customer.
                        break;
                    case 2:
                        System.out.print("\nEnter id :- ");
                        String id = AppConstants.s.next();
                        System.out.print("Enter password :- ");
                        String password = AppConstants.s.next();
                        // Logic for sending validator that customer or admin that returns string as "Customer" or "Admin" and using if-else to call methods respectively.
                        break;
                    case 3:
                        System.out.println("\nSwadKart signing off... pet full, mood chill! \uD83D\uDE0E\uD83C\uDF55");
                        AppConstants.bool = false;
                        break;
                    default:
                        System.out.println("Invalid Choice Please try again !");
                        break;
                }
            }
        }
        catch (Exception e) {

        }
    }
}