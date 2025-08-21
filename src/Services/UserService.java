package Services;
import Constants.AppConstants;
import Dao.DishDAO;
import Dao.RestaurantDAO;
import Utils.Validators;
import Ds.*;

import java.util.Objects;
import java.util.Scanner;

public class UserService {
    static Scanner scanner = AppConstants.s;
    private static boolean running = true;
    public static boolean isEmpty = true;
    public static LL Cart = new LL();

    public static void addToCart() throws Exception {
        String r_id = null;
        // Select restaurant if cart is empty
        if (isEmpty) {
            RestaurantDAO.getRestaurantIdAndName();
            while (!Validators.validateRestaurantId(r_id)) {
                System.out.print("\nEnter Restaurant Id or 'b' to go back :- ");
                r_id = scanner.next().trim();
                if (r_id.equalsIgnoreCase("b")) {
                    return;
                }
                // Ensure all digits
                boolean validDigits = true;
                for (int i = 0; i < r_id.length(); i++) {
                    if (!Character.isDigit(r_id.charAt(i))) {
                        validDigits = false;
                        break;
                    }
                }
                if (!validDigits) {
                    System.out.println(AppConstants.TEXT_ANSI_RED + "Enter valid id (e.g 10,12) or 'b' to go back :- " + AppConstants.ANSI_RESET);
                    r_id = null;
                    continue;
                }
                // Format r_id
                if (r_id.length() == 1) r_id = "r-000" + r_id;
                else if (r_id.length() == 2) r_id = "r-00" + r_id;
                else if (r_id.length() == 3) r_id = "r-0" + r_id;
                else r_id = "r-" + r_id;
            }
            isEmpty = false;
        }
        // Check restaurant exists
        if (!RestaurantDAO.checkRestaurantId(r_id)) {
            System.out.print(AppConstants.TEXT_ANSI_RED + "\nInvalid Restaurant Id. Try again, or 'b' to go back :- " + AppConstants.ANSI_RESET);
            if (scanner.next().equalsIgnoreCase("b")) return;
            r_id = scanner.next().trim();
        }
        while (running) {
            displayCartMenu();
            String option = scanner.next().trim();
            try {
                processAction(option, r_id);
            } catch (Exception e) {
                System.out.println(AppConstants.ERR_INVALID_INPUT);
                scanner.nextLine();
            }
        }
        running = true;
    }

    private static void processAction(String option,String r_id) throws Exception{
        switch (option) {
            case "1": // Add Dish
                    String restaurantName = DishDAO.getRestaurantNameById(r_id);
                    DishDAO.browseDishesByRestaurant(restaurantName);
                    System.out.print("\nEnter Dish id (vd-xxx) to add or 'b' to go back :- ");
                    String dishInput = scanner.next().trim();
                    if (dishInput.equalsIgnoreCase("b")) {
                        isEmpty = true;
                        return;
                    }
                    if (dishInput.length() == 1) dishInput = "VD00" + dishInput;
                    else if (dishInput.length() == 2) dishInput = "VD0" + dishInput;
                    else if (dishInput.length() == 3) dishInput = "VD" + dishInput;
                    Models.Dish dish = DishDAO.getDishByIdAndRestaurant(dishInput, restaurantName);
                    if (dish == null) {
                        System.out.println(AppConstants.TEXT_ANSI_RED + "Invalid Dish ID or not in this restaurant!" + AppConstants.ANSI_RESET);
                        break;
                    }
                    System.out.print("\nEnter quantity or 'b' to go back: ");
                    String qtyInput = scanner.next().trim();
                    if (qtyInput.equalsIgnoreCase("b")) {
                        isEmpty = true;
                        return;
                    }
                    while (!Validators.validateQuantity(qtyInput)) {
                        System.out.print(AppConstants.TEXT_ANSI_RED + "Invalid quantity. Try again or 'b' to go back :- " + AppConstants.ANSI_RESET);
                        qtyInput = scanner.next().trim();
                        if (qtyInput.equalsIgnoreCase("b")) {
                            isEmpty = true;
                            return;
                        }
                    }
                    int quantity = Integer.parseInt(qtyInput);
                    while (quantity > 50) {
                        System.out.println(AppConstants.TEXT_ANSI_YELLOW + "Maximum quantity allowed is 50. Contact restaurant for more." + AppConstants.ANSI_RESET);
                        System.out.print("\nEnter quantity <=50 or 'b' to go back :- ");
                        qtyInput = scanner.next().trim();
                        if (qtyInput.equalsIgnoreCase("b")) {
                            isEmpty = true;
                            return;
                        }
                        while (!Validators.validateQuantity(qtyInput)) {
                            System.out.println("\nEnter valid quantity or 'b' to go back to Main Menu :- ");
                            qtyInput = scanner.next().trim();
                            if (qtyInput.equalsIgnoreCase("b")) {
                                isEmpty = true;
                                return;
                            }
                        }
                        quantity = Integer.parseInt(qtyInput);
                    }
                    Cart.insert(dish, quantity);
                    break;
            case "2": // Remove Dish
                    if (Cart.head == null) {
                        System.out.println(AppConstants.TEXT_ANSI_RED + "Cart is empty, nothing to remove." + AppConstants.ANSI_RESET);
                        break;
                    }
                    Cart.displayTabular();

                    System.out.print("\nEnter Dish id to remove or 'b' to go back to Main Menu :- ");
                    String removeId = scanner.next().trim();
                    if (removeId.equalsIgnoreCase("b")) return;

                    if (removeId.length() == 1) removeId = "VD00" + removeId;
                    else if (removeId.length() == 2) removeId = "VD0" + removeId;
                    else if (removeId.length() == 3) removeId = "VD" + removeId;

                    while (!Cart.checkDish(removeId)) {
                        System.out.print(AppConstants.TEXT_ANSI_RED + "Dish not in cart. Try again or 'b' to go back to Main Menu :- " + AppConstants.ANSI_RESET);
                        removeId = scanner.next().trim();
                        if (removeId.equalsIgnoreCase("b")) addToCart();
                        if (removeId.length() == 1) removeId = "VD00" + removeId;
                        else if (removeId.length() == 2) removeId = "VD0" + removeId;
                        else if (removeId.length() == 3) removeId = "VD" + removeId;
                    }

                    System.out.print("Enter quantity to remove or 'b' to go back to Main Menu :- ");
                    String quant = scanner.next().trim();
                    if (quant.equalsIgnoreCase("b")) {
                        isEmpty = true;
                        return;
                    }

                    while (!Validators.validateQuantity(quant) || !Cart.delete(removeId, Integer.parseInt(quant))) {
                        System.out.print(AppConstants.TEXT_ANSI_RED + "Invalid quantity, try again or 'b' to go back to Main Menu :- " + AppConstants.ANSI_RESET);
                        quant = scanner.next().trim();
                        if (quant.equalsIgnoreCase("b")) {
                            isEmpty = true;
                            return;
                        }
                    }
                    break;
            case "3": // Display Cart
                    Cart.displayTabular();
                    break;
            case "4": // Back
                    isEmpty = true;
                    running = false;
                    break;
            default:
                    System.out.println(AppConstants.TEXT_ANSI_RED + "Invalid input! Try again." + AppConstants.ANSI_RESET);
        }
    }

    private static void displayCartMenu() {
        System.out.println("\n\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t*************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Cart Menu +\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t*************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t1. Add Dish\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t2. Remove Dish\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t3. Cart\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t4. Back\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }
}