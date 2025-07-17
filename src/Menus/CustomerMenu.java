package Menus;

import Constants.*;
import java.util.*;
public class CustomerMenu {
    static void newCustomer() {
        // New Customer Registration logic
    }
    static void customerMenu() {
        AppConstants.bool = true;
        while (AppConstants.bool) {
            System.out.print("\nEnter 1 to browse Restaurants.\n" +
                                "Enter 2 to view menu.\n" +
                                "Enter 3 to Add to cart.\n" +
                                "Enter 4 to place order.\n" +
                                "Enter 5 to track orders.\n" +
                                "Enter 6 to Add reviews.\n" +
                                "Enter 7 to logout.\n\nEnter :- ");
            int n = AppConstants.s.nextInt();
            switch (n) {
                case 1 :
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    AppConstants.bool = false;
                    break;
                default:
                    System.out.println("Please choose valid option !");
                    break;
            }
        }
    }
}
