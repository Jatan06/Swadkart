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

    public static boolean isEmpty = true;

    public static LL Cart = new LL();

    public static void addToCart() throws Exception {
        String r_id = null;
        if(isEmpty){
            RestaurantDAO.getRestaurantIdAndName();
            while(!Validators.validateRestaurantId(r_id)) {
                System.out.print("\nEnter Restaurant Id or 'b' to go back: ");
                r_id = AppConstants.s.next().trim();
                if(r_id.equalsIgnoreCase("b"))  {
                    return;
                }
                for (int i =0;i<r_id.length();i++) {
                    if(!Character.isDigit(r_id.charAt(i))) {
                        System.out.println(AppConstants.TEXT_ANSI_RED+"Enter valid id (e.g 10,12) or enter 'b' to go back:- "+AppConstants.ANSI_RESET);
                        r_id = scanner.next();
                        if(r_id.equalsIgnoreCase("b")) return;
                        break;
                    }
                }
                if(r_id.length()==1) {
                    r_id = "r-000"+r_id;
                } else if (r_id.length()==2) {
                    r_id = "r-00"+r_id;
                }
                else if (r_id.length()==3) {
                    r_id = "r-0"+r_id;
                }
                else if (r_id.length()==4) {
                    r_id = "r-"+r_id;
                }
                else {
                    System.out.println(AppConstants.TEXT_ANSI_RED+"Enter valid r-id. Try again by enter any key "+AppConstants.ANSI_RESET+" or enter 'b' to go back:- ");
                    String c = AppConstants.s.next();
                    if(c.equalsIgnoreCase("b")) return;
                }
            }
            isEmpty= false;
        }
        if(!RestaurantDAO.checkRestaurantId(r_id)) {
            System.out.print(AppConstants.TEXT_ANSI_RED+"\nInvalid Restaurant Id. Try again,"+AppConstants.ANSI_RESET+" or enter 'b' to go back :- ");
            if(AppConstants.s.next().equalsIgnoreCase("b")) {
                return;
            }
            else {
                addToCart();
            }
        }
        boolean running = true;
        while (running) {
            displayCartMenu();
            String option = scanner.next();
            switch (option) {
                case "1":
                    DishDAO.browseDishesByRestaurant(Objects.requireNonNull(DishDAO.getRestaurantNameById(r_id)));
                    System.out.print("\nEnter Dish (vd-xxx/xxx) id to add or enter 'b' to go back: ");
                    String dishInput = scanner.next();
                    if(dishInput.equalsIgnoreCase("b")) addToCart();
                    else if(dishInput.length()==1) {
                        dishInput = "VD00"+dishInput;
                    }
                    else if(dishInput.length()==2) {
                        dishInput = "VD0"+dishInput;
                    }
                    else if(dishInput.length()==3) {
                        dishInput = "VD"+dishInput;
                    }
                    String restaurantName = DishDAO.getRestaurantNameById(r_id);
                    Models.Dish dish = DishDAO.getDishByIdAndRestaurant(dishInput, restaurantName);
                    if (dish == null) {
                        System.out.println(AppConstants.TEXT_ANSI_RED+"\nInvalid Dish ID or dish does not belong to this restaurant. Try again!"+AppConstants.ANSI_RESET);
                        break;
                    }
                    System.out.print("\nEnter quantity or 'b' to go back: ");
                    String qtyInput = scanner.next();
                    if(qtyInput.equalsIgnoreCase("b")) addToCart();
                    while (!Validators.validateQuantity(qtyInput)) {
                        System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid quantity. Try again "+AppConstants.ANSI_RESET+" or enter 'b' to go back :- ");
                        qtyInput = scanner.next();
                        if(qtyInput.equalsIgnoreCase("b")) addToCart();
                    }
                    int quantity = Integer.parseInt(qtyInput);
                    while(quantity>50) {
                        System.out.println(AppConstants.TEXT_ANSI_YELLOW+"\nMaximum quantity allowed is 50. Please contact the restaurant for more dishes. ("+RestaurantDAO.getRestaurantNameById(r_id)+"-"+RestaurantDAO.getRestaurantPhoneNoById(r_id)+")"+AppConstants.ANSI_RESET);
                        System.out.print("\nEnter 'b' to go back or "+AppConstants.TEXT_ANSI_RED+" enter quantity less than 50 :- "+AppConstants.ANSI_RESET);
                        qtyInput = AppConstants.s.next();
                        if(qtyInput.equalsIgnoreCase("b")) addToCart();
                        else quantity = Integer.parseInt(qtyInput);
                    }
                    Cart.insert(dish, quantity);
                    break;
                case "2":
                    if (Cart.head == null) {
                        System.out.println(AppConstants.TEXT_ANSI_RED + "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCart is empty, nothing to delete." + AppConstants.ANSI_RESET);
                        AppConstants.s.nextLine();
                        continue;
                    }
                    Cart.displayTabular();
                    System.out.print("\nEnter Dish id (vd-xxx/xxx) to remove or enter 'b' to go back: ");
                    String removeId = scanner.next();
                    if(removeId.equalsIgnoreCase("b")) addToCart();
                    else if(removeId.length()==1) {
                        removeId = "VD00"+removeId;
                    }
                    else if(removeId.length()==2) {
                        removeId = "VD0"+removeId;
                    }
                    else if(removeId.length()==3) {
                        removeId = "VD"+removeId;
                    }
                    else {
                        while (!Cart.checkDish(removeId)) {
                            System.out.println(AppConstants.TEXT_ANSI_RED+"Dish Id not found in cart. Try again "+AppConstants.ANSI_RESET+"or enter 'b' to go back :- ");
                            removeId = scanner.next().trim();
                            if (removeId.equalsIgnoreCase("b")) addToCart();
                            else {
                                if (removeId.equalsIgnoreCase("b")) return;
                                else if (removeId.length() == 1) {
                                    removeId = "VD00" + removeId;
                                } else if (removeId.length() == 2) {
                                    removeId = "VD0" + removeId;
                                } else if (removeId.length() == 3) {
                                    removeId = "VD" + removeId;
                                }
                            }
                        }
                    }
                    while (!Cart.checkDish(removeId)) {
                        System.out.println(AppConstants.TEXT_ANSI_RED+"Dish Id not found in cart. Try again "+AppConstants.ANSI_RESET+" or enter 'b' to go back :- ");
                        removeId = scanner.next().trim();
                        if (removeId.equalsIgnoreCase("b")) addToCart();
                        else {
                            if (removeId.length() == 1) {
                                removeId = "VD00" + removeId;
                            } else if (removeId.length() == 2) {
                                removeId = "VD0" + removeId;
                            } else if (removeId.length() == 3) {
                                removeId = "VD" + removeId;
                            }
                        }
                    }
                    System.out.print("\nEnter quantity to remove or 'b' to go back :- ");
                    String quant = scanner.next().trim();
                    if(quant.equalsIgnoreCase("b")) addToCart();
                    while (!Validators.validateQuantity(quant)) {
                        System.out.print(AppConstants.TEXT_ANSI_WHITE+"Invalid quantity. Try again "+AppConstants.ANSI_RESET+"or enter 'b' to go back :- ");
                        quant = scanner.next();
                        if(quant.equalsIgnoreCase("b")) addToCart();
                    }
                    while(!Cart.delete(removeId,Integer.parseInt(quant))) {
                        System.out.print(AppConstants.TEXT_ANSI_RED+"\nInvalid quantity, please try again "+AppConstants.ANSI_RESET+"or enter 'b' to go back :- ");
                        quant = scanner.next().trim();
                        if(quant.equalsIgnoreCase("b")) addToCart();
                    }
                    break;
                case "3":
                    Cart.display();
                    break;
                case "4":
                    isEmpty = true;
                    running = false;
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
            }
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