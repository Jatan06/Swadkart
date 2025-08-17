package Services;

import Constants.AppConstants;
import Dao.OrderDAO;
import Dao.ReviewDAO;
import Models.Review;
import java.util.List;
import java.util.Scanner;

// ... existing code ...
public class ReviewService {

    private final ReviewDAO reviewDAO;
    private static final Scanner scanner = new Scanner(System.in);

    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    /**
     * Displays a menu to browse and display reviews by different categories.
     * Options:
     * 1) All reviews
     * 2) By Restaurant ID
     * 3) By User ID
     * 4) By Order ID
     * 5) By Dish ID
     * 6) By Rating Range
     * 7) By Feedback Keyword
     * 0) Exit
     */
    public static void showReviewMenu() {
        while (true) {
            System.out.println("\n=== Review Browser ===");
            System.out.println("1) Show all reviews");
            System.out.println("2) Show by Restaurant ID");
            System.out.println("3) Show by User ID");
            System.out.println("4) Show by Order ID");
            System.out.println("5) Show by Dish ID");
            System.out.println("6) Show by Rating Range");
            System.out.println("7) Search by Feedback Keyword");
            System.out.println("0) Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        displayReviews(safeList(ReviewDAO.getAll()));
                        break;
                    case "2":
                        System.out.print("Enter Restaurant ID: ");
                        displayReviews(safeList(ReviewDAO.findByRestaurantId(scanner.nextLine().trim())));
                        break;
                    case "3":
                        System.out.print("Enter User ID: ");
                        displayReviews(safeList(ReviewDAO.findByUserId(scanner.nextLine().trim())));
                        break;
                    case "4":
                        System.out.print("Enter Order ID: ");
                        displayReviews(safeList(ReviewDAO.findByOrderId(scanner.nextLine().trim())));
                        break;
                    case "5":
                        System.out.print("Enter Dish ID: ");
                        displayReviews(safeList(ReviewDAO.findByDishId(scanner.nextLine().trim())));
                        break;
                    case "6":
                        double min = readDouble("Enter minimum rating (inclusive): ");
                        double max = readDouble("Enter maximum rating (inclusive): ");
                        if (min > max) {
                            System.out.println("Min rating cannot be greater than max rating.");
                        } else {
                            displayReviews(safeList(ReviewDAO.findByRatingRange(min, max)));
                        }
                        break;
                    case "7":
                        System.out.print("Enter keyword to search in feedback: ");
                        displayReviews(safeList(ReviewDAO.findByKeyword(scanner.nextLine().trim())));
                        break;
                    case "0":
                        System.out.println("Exiting Review Browser.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception ex) {
                System.out.println("Error while fetching reviews: " + ex.getMessage());
            }
        }
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
// ... existing code ...
