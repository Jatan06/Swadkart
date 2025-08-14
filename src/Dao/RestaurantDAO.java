package Dao;
import Admin.Admin;
import Constants.*;
import java.sql.*;
public class RestaurantDAO {

public static void browseRestaurants() throws Exception {
    try (PreparedStatement br = AppConstants.connection.prepareStatement("SELECT * FROM restaurants");
         ResultSet restaurants = br.executeQuery()) {

        // ANSI color codes for terminal
        final String RESET = "\u001B[0m";
        final String BOLD = "\u001B[1m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String RED = "\u001B[31m";
        final String BLUE = "\u001B[34m";
        final String CYAN = "\u001B[36m";

        // Fixed column widths to keep a stable table
        final int W_ID = 8, W_NAME = 25, W_CUISINE = 18, W_PHONE = 14, W_ADDRESS = 35, W_RATING = 8, W_STATUS = 12;

        // Header
        System.out.println("\n" + BOLD + CYAN + "=".repeat(130) + RESET);
        System.out.println(BOLD + BLUE + "\t\t\t                                       🍽️  RESTAURANTS  🍽️" + RESET);
        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);

        // Column headers
        System.out.printf(
                BOLD + "%-" + W_ID + "s %-" + W_NAME + "s %-" + W_CUISINE + "s %-" + W_PHONE + "s %-" + W_ADDRESS + "s %-" + W_RATING + "s %-" + W_STATUS + "s%n" + RESET,
                "ID", "Restaurant Name", "Cuisine", "Phone", "Address", "Rating", "Status"
        );
        System.out.println(CYAN + "-".repeat(130) + RESET);

        int count = 0;

        while (restaurants.next()) {
            String id = nz(restaurants.getString("id"));
            String name = nz(restaurants.getString("name"));
            String cuisine = nz(restaurants.getString("cuisine"));
            String phone = nz(restaurants.getString("phone_no"));
            String address = nz(restaurants.getString("address"));
            double rating = restaurants.getDouble("rating");
            if (restaurants.wasNull()) rating = 0.0;

            // Truncate long text to fit columns
            String displayName = truncate(name, W_NAME);
            String displayAddress = truncate(address, W_ADDRESS);

            // Color/status based on rating
            String ratingColor;
            String statusText;
            if (rating >= 4.5) {
                ratingColor = GREEN;
                statusText = "⭐ Excellent";
            } else if (rating >= 4.0) {
                ratingColor = YELLOW;
                statusText = "👍 Good";
            } else if (rating >= 3.5) {
                ratingColor = "";
                statusText = "👌 Average";
            } else {
                ratingColor = RED;
                statusText = "👎 Below Avg";
            }

            System.out.printf(
                    "%-" + W_ID + "s %-" + W_NAME + "s %-" + W_CUISINE + "s %-" + W_PHONE + "s %-" + W_ADDRESS + "s %s%" + (W_RATING - 3) + ".1f%s %-" + W_STATUS + "s%n",
                    id, displayName, cuisine, phone, displayAddress,
                    ratingColor, rating, RESET, statusText
            );

            count++;
            if (count % 15 == 0) {
                System.out.println(CYAN + "-".repeat(130) + RESET);
            }
        }

        if (count == 0) {
            System.out.printf("| %s |%n", padRight("No restaurants found.", 126));
        }

        // Footer
        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);
        System.out.println(BOLD + GREEN + "📊 Total Restaurants: " + count + RESET);
        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);
    }
}
private static String nz(String s) {
    return (s == null || s.isBlank()) ? "-" : s;
}
private static String truncate(String s, int width) {
    if (s == null) return "-";
    if (s.length() <= width) return s;
    // Leave room for "..."
    return (width >= 3) ? s.substring(0, width - 3) + "..." : s.substring(0, width);
}
private static String padRight(String s, int w) {
    if (s.length() >= w) return s;
    StringBuilder sb = new StringBuilder(w);
    sb.append(s);
    while (sb.length() < w) sb.append(' ');
    return sb.toString();
}

//    public static void browseRestaurants() throws Exception{
//        PreparedStatement br = AppConstants.connection.prepareCall("select * from restaurants");
//        ResultSet restaurants = br.executeQuery();
//        // ANSI color codes for terminal
//        final String RESET = "\u001B[0m";
//        final String BOLD = "\u001B[1m";
//        final String GREEN = "\u001B[32m";
//        final String YELLOW = "\u001B[33m";
//        final String RED = "\u001B[31m";
//        final String BLUE = "\u001B[34m";
//        final String CYAN = "\u001B[36m";
//        // Print header with styling
//        System.out.println("\n" + BOLD + CYAN + "=".repeat(130) + RESET);
//        System.out.println(BOLD + BLUE + "\t\t\t                                       🍽️  RESTAURANTS  🍽️" + RESET);
//        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);
//        // Print column headers
//        System.out.printf(BOLD + "%-8s %-25s %-18s %-14s %-35s %-8s %-12s%n" + RESET,
//                "ID", "Restaurant Name", "Cuisine", "Phone", "Address", "Rating", "Status");
//        System.out.println(CYAN + "-".repeat(130) + RESET);
//        int count = 0;
//        while (restaurants.next()) {
//            String id = restaurants.getString("id");
//            String name = restaurants.getString("name");
//            String cuisine = restaurants.getString("cuisine");
//            String phone = restaurants.getString("phone_no");
//            String address = restaurants.getString("address");
//            double rating = restaurants.getDouble("rating");
//
//            // Truncate long text
//            String displayName = name.length() > 25 ? name.substring(0, 22) + "..." : name;
//            String displayAddress = address.length() > 35 ? address.substring(0, 32) + "..." : address;
//
//            // Color code based on rating
//            String ratingColor = "";
//            String statusText = "";
//            if (rating >= 4.5) {
//                ratingColor = GREEN;
//                statusText = "⭐ Excellent";
//            } else if (rating >= 4.0) {
//                ratingColor = YELLOW;
//                statusText = "👍 Good";
//            } else if (rating >= 3.5) {
//                ratingColor = "";
//                statusText = "👌 Average";
//            } else {
//                ratingColor = RED;
//                statusText = "👎 Below Avg";
//            }
//
//            // Print restaurant data with color coding
//            System.out.printf("%-8s %-25s %-18s %-14s %-35s %s%-8.1f%s %-12s%n",
//                    id, displayName, cuisine, phone, displayAddress,
//                    ratingColor, rating, RESET, statusText);
//            count++;
//            // Add a separator every 15 rows
//            if (count % 15 == 0) {
//                System.out.println(CYAN + "-".repeat(130) + RESET);
//            }
//        }
//        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);
//        System.out.println(BOLD + GREEN + "📊 Total Restaurants: " + count + RESET);
//// System.out.println(BOLD + BLUE + "💡 Sorted by Rating (Highest to Lowest)" + RESET);
//        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);
//        restaurants.close();
//        br.close();
//    }
//    public static void browseRestaurantsByCuisine() throws Exception {
//        System.out.print("Enter cuisine type (or 'ALL' for all cuisines): ");
//        String cuisineInput = AppConstants.s.nextLine().trim();
//
//        String query;
//        PreparedStatement br;
//
//        if (cuisineInput.equalsIgnoreCase("ALL")) {
//            query = "SELECT * FROM restaurants ORDER BY cuisine, rating DESC";
//            br = AppConstants.connection.prepareStatement(query);
//        } else {
//            query = "SELECT * FROM restaurants WHERE cuisine LIKE ? ORDER BY rating DESC";
//            br = AppConstants.connection.prepareStatement(query);
//            br.setString(1, "%" + cuisineInput + "%");
//        }
//
//        ResultSet restaurants = br.executeQuery();
//
//        // Similar formatting as above method
//        final String RESET = "\u001B[0m";
//        final String BOLD = "\u001B[1m";
//        final String GREEN = "\u001B[32m";
//        final String BLUE = "\u001B[34m";
//        final String CYAN = "\u001B[36m";
//
//        System.out.println("\n" + BOLD + CYAN + "=".repeat(120) + RESET);
//        System.out.println(BOLD + BLUE + "🔍 FILTERED RESTAURANTS - " + cuisineInput.toUpperCase() + RESET);
//        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);
//
//        System.out.printf(BOLD + "%-8s %-25s %-18s %-14s %-35s %-8s%n" + RESET,
//                "ID", "Restaurant Name", "Cuisine", "Phone", "Address", "Rating");
//        System.out.println(CYAN + "-".repeat(120) + RESET);
//
//        int count = 0;
//        String currentCuisine = "";
//
//        while (restaurants.next()) {
//            String cuisine = restaurants.getString("cuisine");
//
//            // Add cuisine separator when cuisine changes
//            if (!cuisine.equals(currentCuisine) && cuisineInput.equalsIgnoreCase("ALL")) {
//                if (count > 0) System.out.println();
//                System.out.println(BOLD + BLUE + "📍 " + cuisine.toUpperCase() + " RESTAURANTS:" + RESET);
//                System.out.println(CYAN + "-".repeat(60) + RESET);
//                currentCuisine = cuisine;
//            }
//
//            String id = restaurants.getString("id");
//            String name = restaurants.getString("name");
//            String phone = restaurants.getString("phone_no");
//            String address = restaurants.getString("address");
//            double rating = restaurants.getDouble("rating");
//
//            String displayName = name.length() > 25 ? name.substring(0, 22) + "..." : name;
//            String displayAddress = address.length() > 35 ? address.substring(0, 32) + "..." : address;
//
//            String ratingColor = rating >= 4.5 ? GREEN : rating >= 4.0 ? "\u001B[33m" : "";
//
//            System.out.printf("%-8s %-25s %-18s %-14s %-35s %s%-8.1f%s%n",
//                    id, displayName, cuisine, phone, displayAddress,
//                    ratingColor, rating, RESET);
//            count++;
//        }
//
//        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);
//        System.out.println(BOLD + GREEN + "📊 Total Matching Restaurants: " + count + RESET);
//        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);
//
//        restaurants.close();
//        br.close();
//    }

    public static void addRestaurant() throws Exception {
        AppConstants.s.nextLine();
        System.out.print("\nEnter Restaurant ID: ");
        String id = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Restaurant Name: ");
        String name = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Cuisine Type: ");
        String cuisine = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Phone Number: ");
        String phone = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Address: ");
        String address = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Rating: ");
        double rating = AppConstants.s.nextDouble();
        CallableStatement br = AppConstants.connection.prepareCall("insert into restaurants values (?,?,?,?,?,?)");
        br.setString(1, id);
        br.setString(2, name);
        br.setString(3, cuisine);
        br.setString(4, phone);
        br.setString(5, address);
        br.setDouble(6, rating);
        if (br.executeUpdate() > 0) {
            System.out.println("\nRestaurant added successfully.");
        }
        else {
            System.out.println("\nError adding restaurant.");
        }
    }
    public static void deleteRestaurant() throws Exception {
        AppConstants.connection.setAutoCommit(false);
        AppConstants.s.nextLine();
        System.out.print("\nEnter Restaurant ID: ");
        String id = AppConstants.s.nextLine().trim();
        CallableStatement br = AppConstants.connection.prepareCall("delete from restaurants where id = ?");
        br.setString(1, id);
        if (br.executeUpdate() > 0) {
            System.out.print("\nAre you sure you want to delete this restaurant? (y/n): ");
            if(AppConstants.s.next().trim().equalsIgnoreCase("y")) {
                System.out.print("\nEnter Password");
                if(AppConstants.s.next().equals(Admin.getAdminPassword())) {
                    AppConstants.connection.commit();
                    System.out.println("\nRestaurant deleted successfully.");
                }
                else {
                    AppConstants.connection.rollback();
                    System.out.println("\nIncorrect password. Restaurant not deleted!");
                }
            }
            else {
                System.out.println("\nRestaurant not deleted.");
            }
        }
        else {
            System.out.println("Error deleting restaurant.");
        }
    }
    static public String getRestaurantIdByName(String name) {
        String id = null;
        try {
            PreparedStatement ps = AppConstants.connection.prepareCall("select id from restaurants where name = ?;");
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                id = rs.getString("id");
            }
        } catch (SQLException e) {
            System.out.printf("Exception :- "+e);
        }
        return id;
    }
}