//UserService.java
package Services;

import Constants.AppConstants;
import Dao.DishDAO;
import Utils.Validators;
import Ds.LL;

import java.util.Scanner;

public class UserService {
    static Scanner scanner = AppConstants.s;
    static Boolean isEmpty = true;
    public static LL Cart = new LL();
    public static void addToCart() throws Exception {
        String r_id = null;
        if(isEmpty){
            while(!Validators.validateRestaurantName(r_id)) {
                System.out.println("Enter Restaurant Id: ");
                r_id = scanner.next();
            }
            isEmpty= false;
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
                    DishDAO.browseDishesByRestaurant(r_id);
                    System.out.print("Enter Dish id to add: ");
                    String dishInput = scanner.next();
                    Models.Dish dish = DishDAO.getDishByIdAndRestaurant(dishInput, r_id);
                    if (dish == null) {
                        System.out.println("Invalid Dish ID or dish does not belong to this restaurant. Try again.");
                        break;
                    }
                    System.out.print("Enter quantity: ");
                    String qtyInput = scanner.next();
                    if (!Validators.validateQuantity(qtyInput)) {
                        System.out.println("Invalid quantity. Try again.");
                        break;
                    }
                    int quantity = Integer.parseInt(qtyInput);
                    Cart.insert(dish, quantity);
                    break;
                case "2":
                    Cart.display();
                    System.out.print("Enter Dish id to remove: ");
                    String removeId = scanner.next();
                    Cart.delete(removeId);
                    break;
                case "3":
                    Cart.display();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
