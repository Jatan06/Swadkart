package Services;

import Constants.AppConstants;
import Dao.DishDAO;
import Utils.Validators;
import Ds.LL;

public class UserService {
    LL Cart = new LL();
    public static void addToCart() throws Exception {
        String r_id = null;
        while(!Validators.validateRestaurantName(r_id)) {
            System.out.println("Enter Restaurant Id: ");
            r_id = AppConstants.s.next();
        }
        DishDAO.browseDishesByRestaurant(r_id);
        System.out.println("\nEnter Dish id: ");
        while(true) {
            try {
                int dishid = AppConstants.s.nextInt();
                //Validate Dish id and dish from selected retaurant
                //add dish and quantity to linked list
                break;
            } catch (Exception e) {
                System.out.println("Enter Valid Integer Dish id only");
            }
        }
    }
}
