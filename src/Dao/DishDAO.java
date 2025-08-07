//DishDAO.java
package Dao;
import java.sql.*;
import Constants.*;
public class DishDAO {
    public static void browseDishesByRestaurant(String res_name) throws Exception {
        String [] res_name_arr = res_name.split(" ");

        PreparedStatement bd = AppConstants.connection.prepareStatement(
                "SELECT * FROM dishes WHERE restaurant = ?;"
        );
        bd.setString(1,res_name);
        ResultSet rs = bd.executeQuery();

        // ANSI color codes
        final String RESET = "\u001B[0m";
        final String BOLD = "\u001B[1m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String RED = "\u001B[31m";
        final String BLUE = "\u001B[34m";
        final String CYAN = "\u001B[36m";

        if(rs!=null) {
            // Print header
            System.out.println("\n" + BOLD + CYAN + " ".repeat(130) + RESET);
            System.out.println(BOLD + BLUE + "\t\t\t\t                                🍽  DISHES  🍽" + RESET + "\n");
            System.out.println(BOLD + CYAN + " ".repeat(130) + RESET);

            // Print column headers
            System.out.printf(BOLD + "%-10s %-25s %-10s %-25s %-8s %-10s %-12s%n" + RESET,
                    "Dish ID", "Dish Name", "Type", "Restaurant", "Rating", "Price(₹)", "Status");
            System.out.println(CYAN + "-".repeat(130) + RESET);

            int count = 0;
            while (rs.next()) {
                String id = rs.getString("dish_id");
                String name = rs.getString("name");
                String type = rs.getString("cuisine");
                String restaurant = rs.getString("restaurant");
                double rating = rs.getDouble("rating");
                int price = rs.getInt("price");

                // Truncate names if too long
                String displayName = name.length() > 25 ? name.substring(0, 22) + "..." : name;
                String displayRestaurant = restaurant.length() > 25 ? restaurant.substring(0, 22) + "..." : restaurant;

                // Determine status
                String ratingColor = "";
                String status = "";
                if (rating >= 4.5) {
                    ratingColor = GREEN;
                    status = "🌟 Top Rated";
                } else if (rating >= 4.0) {
                    ratingColor = YELLOW;
                    status = "👍 Good";
                } else if (rating >= 3.5) {
                    ratingColor = "";
                    status = "👌 Average";
                } else {
                    ratingColor = RED;
                    status = "👎 Low Rated";
                }

                // Display row
                System.out.printf("%-10s %-25s %-10s %-25s %s%-8.1f%s %-10d %-12s%n",
                        id, displayName, type, displayRestaurant,
                        ratingColor, rating, RESET, price, status);

                count++;
            }
        }
        else {
            System.out.println(RED + "Restaurant: " + res_name + " not found" + RESET);
        }
    }
    public static void browseDishesByCuisine(String dish_category) throws Exception {
        PreparedStatement bd = AppConstants.connection.prepareStatement(
                "SELECT * FROM dishes WHERE cuisine = ?;"
        );
        bd.setString(1, dish_category);
        ResultSet rs = bd.executeQuery();
        // ANSI color codes
        final String RESET = "\u001B[0m";
        final String BOLD = "\u001B[1m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String RED = "\u001B[31m";
        final String BLUE = "\u001B[34m";
        final String CYAN = "\u001B[36m";
        if(rs!=null) {
            // Print header
            System.out.println("\n" + BOLD + CYAN + "=".repeat(130) + RESET);
            System.out.println(BOLD + BLUE + "\t\t\t\t                                🍽  DISHES  🍽" + RESET + "\n");
            System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);

            // Print column headers
            System.out.printf(BOLD + "%-10s %-25s %-10s %-25s %-8s %-10s %-12s%n" + RESET,
                    "Dish ID", "Dish Name", "Type", "Restaurant", "Rating", "Price(₹)", "Status");

            int count = 0;
            while (rs.next()) {
                String id = rs.getString("dish_id");
                String name = rs.getString("name");
                String type = rs.getString("cuisine");
                String restaurant = rs.getString("restaurant");
                double rating = rs.getDouble("rating");
                int price = rs.getInt("price");

                // Truncate names if too long
                String displayName = name.length() > 25 ? name.substring(0, 22) + "..." : name;
                String displayRestaurant = restaurant.length() > 25 ? restaurant.substring(0, 22) + "..." : restaurant;

                // Determine status
                String ratingColor = "";
                String status = "";
                if (rating >= 4.5) {
                    ratingColor = GREEN;
                    status = "🌟 Top Rated";
                } else if (rating >= 4.0) {
                    ratingColor = YELLOW;
                    status = "👍 Good";
                } else if (rating >= 3.5) {
                    ratingColor = "";
                    status = "👌 Average";
                } else {
                    ratingColor = RED;
                    status = "👎 Low Rated";
                }

                // Display row
                System.out.printf("%-10s %-25s %-10s %-25s %s%-8.1f%s %-10d %-12s%n",
                        id, displayName, type, displayRestaurant,
                        ratingColor, rating, RESET, price, status);

                count++;
                if (count % 15 == 0) {
                    System.out.println(CYAN + "-".repeat(130) + RESET);
                }
            }
        }
        else {
            System.out.println(RED + "No dish found with the category: " + dish_category + RESET);
        }
    }
    public static Models.Dish getDishByIdAndRestaurant(String dishId, String restaurantId) {
        try {
            java.sql.PreparedStatement stmt = AppConstants.connection.prepareStatement(
                    "SELECT * FROM dishes WHERE dish_id = ? AND restaurant = ?;");
            stmt.setString(1, dishId);
            stmt.setString(2, restaurantId);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Models.Dish(
                        rs.getString("dish_id"),
                        rs.getString("name"),
                        rs.getString("cuisine"),
                        rs.getString("restaurant"),
                        rs.getDouble("rating"),
                        rs.getDouble("price")
                );
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}