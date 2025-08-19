//UserService.java
//UserService.java
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
    public static int quanti;
    public static void addToCart() throws Exception {
        String r_id = null;
        if(isEmpty){
            RestaurantDAO.getRestaurantIdAndName();
            while(!Validators.validateRestaurantId(r_id)) {
                System.out.print("\nEnter Restaurant Id or 'b' to go back: ");
                r_id = scanner.next();
                for (int i =0;i<r_id.length();i++) {
                    if(!Character.isDigit(r_id.charAt(i))) {
                        System.out.println("Enter valid id (e.g 10,12) :- ");
                        r_id = scanner.next();
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
                    System.out.println("Enter valid r-id.");
                }
            }
            if(r_id.equalsIgnoreCase("b")) return;
            isEmpty= false;
        }
        if(!RestaurantDAO.checkRestaurantId(r_id)) {
            System.out.print("\nInvalid Restaurant Id. Try again. or enter 'b' to go back :- ");
            if(AppConstants.s.next().equalsIgnoreCase("b")) {
                return;
            }
            else {
                addToCart();
            }
        }
        boolean running = true;
        while (running) {
            System.out.println("\n--- Cart Menu ---");
            System.out.println("1. Add dish to cart");
            System.out.println("2. Remove dish from cart");
            System.out.println("3. View cart");
            System.out.println("4. Quit");
            System.out.print("Select an option: ");
            String option = scanner.next();
            switch (option) {
                case "1":
                    DishDAO.browseDishesByRestaurant(Objects.requireNonNull(DishDAO.getRestaurantNameById(r_id)));
                    System.out.print("Enter Dish id to add: ");
                    String dishInput = scanner.next();
                    if(dishInput.length()==1) {
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
                        System.out.println("Invalid Dish ID or dish does not belong to this restaurant. Try again!!");
                        break;
                    }
                    System.out.print("Enter quantity: ");
                    String qtyInput = scanner.next();
                    while (!Validators.validateQuantity(qtyInput)) {
                        System.out.print("Invalid quantity. Try again :- ");
                        qtyInput = scanner.next();
                    }
                    int quantity = Integer.parseInt(qtyInput);
                    Cart.insert(dish, quantity);
                    break;
                case "2":
                    Cart.display();
                    System.out.print("Enter Dish id to remove: ");
                    String removeId = scanner.next();
                    if(removeId.length()==1) {
                        removeId = "VD00"+removeId;
                    }
                    else if(removeId.length()==2) {
                        removeId = "VD0"+removeId;
                    }
                    else if(removeId.length()==3) {
                        removeId = "VD"+removeId;
                    }
                    System.out.print("Enter quantity to remove :- ");
                    String quant = scanner.next();
                    while (!Validators.validateQuantity(quant)) {
                        System.out.print("Invalid quantity. Try again :- ");
                        quant = scanner.next();
                    }
                    Cart.delete(removeId,Integer.parseInt(quant));
                    break;
                case "3":
                    Cart.display();
                    break;
                case "4":
                    isEmpty = true;
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}