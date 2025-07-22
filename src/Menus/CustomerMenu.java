package Menus;
import Constants.*;
import Dao.*;
import Utils.*;
import java.sql.*;
import java.util.*;
public class CustomerMenu {
    static void newCustomer() {

    }
    public static boolean customerValidator(String id,String password) throws Exception {
        Statement s = AppConstants.connection.createStatement();
        ResultSet rs = s.executeQuery("select * from users;");
        while (rs.next()) {
            if(rs.getString(1).equals(id) && rs.getString(7).equals(password)) {
                AppConstants.customerValid = true;
                break;
            }
            else {
                AppConstants.customerValid = false;
            }
        }
        return AppConstants.customerValid;
    }
    static void customerMenu() {
        try {
            AppConstants.run = true;
            while (AppConstants.run) {
                System.out.print("""
                        Enter 1 to browse Restaurants.
                        Enter 2 to browse Dishes by Restaurant.
                        Enter 3 to browse Dishes by Cuisine.
                        Enter 4 to Add to cart.
                        Enter 5 to View cart.
                        Enter 6 to Place order.
                        Enter 7 to for Order history
                        Enter 8 to logout.
                        Enter : \s""");
                int n = AppConstants.s.nextInt();
                switch (n) {
                    case 1:
                        RestaurantDAO.browseRestaurants();
                        break;
                    case 2:
                        System.out.print("\nEnter Restaurant name :  ");
                        AppConstants.s.nextLine();
                        String res_name = AppConstants.s.nextLine();
                        DishDAO.browseDishesByRestaurant(res_name);
                        break;
                    case 3:
                        System.out.print("\nEnter dish category :  ");
                        DishDAO.browseDishesByCategory(AppConstants.s.next());
                        AppConstants.s.nextLine();
                        break;
                    case 4:
                        OrderDAO.addToCart();
                        break;
                    case 5:
                        OrderDAO.viewCart();
                        break;
                    case 6:
                        PaymentDAO.placeOrder();
                        break;
                    case 7:
                        OrderDAO.orderHistory();
                        break;
                    case 8:
                        AppConstants.run = false;
                        break;
                    default:
                        System.out.println("Please choose valid option !");
                        break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
