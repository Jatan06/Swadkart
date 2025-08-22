package Services;
import Constants.AppConstants;
import Dao.DishDAO;
import Dao.OrderDAO;
import Dao.RestaurantDAO;
import Dao.ReviewDAO;
import Models.Review;
import java.util.List;
import java.util.Scanner;
public class ReviewService {

    private final ReviewDAO reviewDAO;
    private static final Scanner scanner = new Scanner(System.in);

    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public static void showReviewMenu() {
        while (true) {
            menu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        displayReviews(safeList(ReviewDAO.getAll()));
                        break;
                    case "2":
                        RestaurantDAO.getRestaurantIdAndName();
                        System.out.print("Enter Restaurant ID or 'b' to go back: ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("b")) return;
                        displayReviews(safeList(ReviewDAO.findByRestaurantId(scanner.nextLine().trim())));
                        break;
                    case "3":

                        System.out.print("Enter User ID: ");
                        displayReviews(safeList(ReviewDAO.findByUserId(scanner.nextLine().trim())));
                        break;
                    case "4":
                        OrderDAO.viewOrderAndOrderItems();
                        System.out.print("Enter Order ID or 'b' to go back :- ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("b")) return;
                        displayReviews(safeList(ReviewDAO.findByOrderId(scanner.nextLine().trim())));
                        break;
                    case "5":
                        DishDAO.browseDishIdAndRestaurant();
                        System.out.print("Enter Dish ID or 'b' to go back :- ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("b")) return;
                        displayReviews(safeList(ReviewDAO.findByDishId(scanner.nextLine().trim())));
                        break;
                    case "6":
                        double min = readDouble("Enter minimum rating (inclusive): ");
                        double max = readDouble("Enter maximum rating (inclusive): ");
                        while(min > max) {
                            System.out.println("\nMin rating cannot be greater than max rating.\n or enter 't' or enter 'b' to go back :- ");
                            if(scanner.nextLine().trim().equalsIgnoreCase("b")) return;
                            min = readDouble("Enter minimum rating (inclusive): ");
                            max = readDouble("Enter maximum rating (inclusive): ");
                        }
                        displayReviews(safeList(ReviewDAO.findByRatingRange(min, max)));
                        break;
                    case "7":
                        System.out.print("Enter keyword to search in feedback or 'b' to go back :- ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("b")) return;
                        displayReviews(safeList(ReviewDAO.findByKeyword(scanner.nextLine().trim())));
                        break;
                    case "8":
                        System.out.println("Exiting Review Browser.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception ex) {
                System.out.println("Please try again.");
                scanner.nextLine();
            }
        }
    }

    private static void menu() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+ AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t****************\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Reviews Menu +\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t****************\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t1. Show all reviews\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t2. By Restaurant Id\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t3. By User Id\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t4. By Order Id\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t5. By Dish Id\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t6. By Rating Range\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t7. Search By Feedback Word\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t8. Back\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option :- ");
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static List<Review> safeList(List<Review> list) {
        return list == null ? java.util.Collections.emptyList() : list;
    }

    private static void displayReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            System.out.println("No reviews found.");
            return;
        }
        System.out.println("\n--- Reviews (" + reviews.size() + ") ---");
        for (Review r : reviews) {
            System.out.printf(
                "Restaurant: %s | User: %s | Order: %s | Dish: %s | Rating: %.2f | Feedback: %s%n",
                nullToDash(r.getRestaurant_id()),
                nullToDash(r.getUser_id()),
                nullToDash(r.getOrder_id()),
                nullToDash(r.getDish_id()),
                r.getReview() == null ? 0.0 : r.getReview(),
                nullToDash(r.getFeedback())
            );
        }
    }

    private static String nullToDash(String s) {
        return (s == null || s.isEmpty()) ? "-" : s;
    }
}