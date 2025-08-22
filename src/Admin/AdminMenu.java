package Admin;
import Constants.AppConstants;
import Dao.*;
import Services.LoginService;
import Services.ReviewService;
public class AdminMenu {
    public static boolean adminValidatorId(String id) {
        return id.equals(Admin.getAdminId());
    }

    public static boolean adminValidatorPassword(String password) {
        return password.equals(Admin.getAdminPassword());
    }

    public static void adminMenu(){
        AppConstants.adminRun = true;
        try {
            while (AppConstants.adminRun) {
                displayAdminMenu();
                try {
                    int choice = AppConstants.s.nextInt();
                    switch (choice) {
                        case 1 -> {
                                    boolean b = true;
                                    while (b) {
                                        displayEditRestMenu();
                                        try {
                                            int n = AppConstants.s.nextInt();
                                            if (n == 1) {
                                                RestaurantDAO.addRestaurant();
                                            } else if (n == 2) {
                                                RestaurantDAO.deleteRestaurant();
                                            } else if (n == 3) {
                                                RestaurantDAO.updateRestaurant();
                                            } else if (n == 4) {
                                                RestaurantDAO.restaurantStats();
                                            } else if (n == 5) {
                                                b = false;
                                            } else {
                                                System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + AppConstants.TEXT_ANSI_RED + AppConstants.ERR_INVALID_INPUT + AppConstants.ANSI_RESET);
                                            }
                                        } catch (Exception e) {
                                            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + AppConstants.TEXT_ANSI_RED + AppConstants.ERR_INVALID_INPUT + AppConstants.ANSI_RESET);
                                            AppConstants.s.nextLine();
                                        }
                                    }
                                }
                        case 2 -> RestaurantDAO.browseRestaurants();
                        case 3 -> OrderDAO.viewOrderAndOrderItems();
                        case 4 -> PaymentDAO.viewTransactions();
                        case 5 -> ReviewService.showReviewMenu();
                        case 6 -> LoginService.displayLogins();
                        case 7 -> AppConstants.adminRun = false;
                        default -> System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
                    }
                } catch (Exception e) {
                    System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
                    AppConstants.s.nextLine();
                }
            }
        }
        catch (Exception e) {
            System.out.println(AppConstants.TEXT_ANSI_RED+"Exception :- "+e.getMessage()+AppConstants.ANSI_RESET);
        }
    }

    private static void displayAdminMenu() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t**************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Admin Menu +\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t**************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t1. Update Restaurants\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t2. View Restaurants\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t3. View Orders\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t4. View Transaction\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t5. View Reviews\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t6. View Logins\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t7. Logout\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option :- ");
    }

    private static void displayEditRestMenu() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-------------------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t********************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Edit Restaurants +\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t********************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t1. Add Restaurant\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t2. Delete Restaurant\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t3. Update Restaurant\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t4. View Restaurant Stats\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t5. Back\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-------------------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option :- ");
    }
}