package Dao;

import Constants.AppConstants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RestaurantDAO {
    public static void browseRestaurants() throws Exception{
        PreparedStatement br = AppConstants.connection.prepareCall("select * from restaurants");
        ResultSet restaurants = br.executeQuery();
        // ANSI color codes for terminal
        final String RESET = "\u001B[0m";
        final String BOLD = "\u001B[1m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String RED = "\u001B[31m";
        final String BLUE = "\u001B[34m";
        final String CYAN = "\u001B[36m";

//        // Print header with styling
//        System.out.println("\n" + BOLD + CYAN + "=".repeat(130) + RESET);
        System.out.println(BOLD + BLUE + "\t\t\t\t                                    🍽️  RESTAURANTS  🍽️" + RESET+"\n\n");
//        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);

        // Print column headers
        System.out.printf(BOLD + "%-8s %-25s %-18s %-14s %-35s %-8s %-12s%n" + RESET,
                "ID", "Restaurant Name", "Cuisine", "Phone", "Address", "Rating", "Status");
//        System.out.println(CYAN + "-".repeat(130) + RESET);

        int count = 0;
        while (restaurants.next()) {
            String id = restaurants.getString("id");
            String name = restaurants.getString("name");
            String cuisine = restaurants.getString("cuisine");
            String phone = restaurants.getString("phone_no");
            String address = restaurants.getString("address");
            double rating = restaurants.getDouble("rating");

            // Truncate long text
            String displayName = name.length() > 25 ? name.substring(0, 22) + "..." : name;
            String displayAddress = address.length() > 35 ? address.substring(0, 32) + "..." : address;

            // Color code based on rating
            String ratingColor = "";
            String statusText = "";
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

            // Print restaurant data with color coding
            System.out.printf("%-8s %-25s %-18s %-14s %-35s %s%-8.1f%s %-12s%n",
                    id, displayName, cuisine, phone, displayAddress,
                    ratingColor, rating, RESET, statusText);
            count++;

            // Add separator every 15 rows
//            if (count % 15 == 0) {
//                System.out.println(CYAN + "-".repeat(130) + RESET);
//            }
        }

//        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);
//        System.out.println(BOLD + GREEN + "📊 Total Restaurants Found: " + count + RESET);
//        System.out.println(BOLD + BLUE + "💡 Sorted by Rating (Highest to Lowest)" + RESET);
//        System.out.println(BOLD + CYAN + "=".repeat(130) + RESET);

        restaurants.close();
        br.close();
    }

    // Additional utility method for cuisine-wise browsing
    public static void browseRestaurantsByCuisine() throws Exception {
        System.out.print("Enter cuisine type (or 'ALL' for all cuisines): ");
        String cuisineInput = AppConstants.s.nextLine().trim();

        String query;
        PreparedStatement br;

        if (cuisineInput.equalsIgnoreCase("ALL")) {
            query = "SELECT * FROM restaurants ORDER BY cuisine, rating DESC";
            br = AppConstants.connection.prepareStatement(query);
        } else {
            query = "SELECT * FROM restaurants WHERE cuisine LIKE ? ORDER BY rating DESC";
            br = AppConstants.connection.prepareStatement(query);
            br.setString(1, "%" + cuisineInput + "%");
        }

        ResultSet restaurants = br.executeQuery();

        // Similar formatting as above method
        final String RESET = "\u001B[0m";
        final String BOLD = "\u001B[1m";
        final String GREEN = "\u001B[32m";
        final String BLUE = "\u001B[34m";
        final String CYAN = "\u001B[36m";

        System.out.println("\n" + BOLD + CYAN + "=".repeat(120) + RESET);
        System.out.println(BOLD + BLUE + "🔍 FILTERED RESTAURANTS - " + cuisineInput.toUpperCase() + RESET);
        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);

        System.out.printf(BOLD + "%-8s %-25s %-18s %-14s %-35s %-8s%n" + RESET,
                "ID", "Restaurant Name", "Cuisine", "Phone", "Address", "Rating");
        System.out.println(CYAN + "-".repeat(120) + RESET);

        int count = 0;
        String currentCuisine = "";

        while (restaurants.next()) {
            String cuisine = restaurants.getString("cuisine");

            // Add cuisine separator when cuisine changes
            if (!cuisine.equals(currentCuisine) && cuisineInput.equalsIgnoreCase("ALL")) {
                if (count > 0) System.out.println();
                System.out.println(BOLD + BLUE + "📍 " + cuisine.toUpperCase() + " RESTAURANTS:" + RESET);
                System.out.println(CYAN + "-".repeat(60) + RESET);
                currentCuisine = cuisine;
            }

            String id = restaurants.getString("id");
            String name = restaurants.getString("name");
            String phone = restaurants.getString("phone_no");
            String address = restaurants.getString("address");
            double rating = restaurants.getDouble("rating");

            String displayName = name.length() > 25 ? name.substring(0, 22) + "..." : name;
            String displayAddress = address.length() > 35 ? address.substring(0, 32) + "..." : address;

            String ratingColor = rating >= 4.5 ? GREEN : rating >= 4.0 ? "\u001B[33m" : "";

            System.out.printf("%-8s %-25s %-18s %-14s %-35s %s%-8.1f%s%n",
                    id, displayName, cuisine, phone, displayAddress,
                    ratingColor, rating, RESET);
            count++;
        }

        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);
        System.out.println(BOLD + GREEN + "📊 Total Matching Restaurants: " + count + RESET);
        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);

        restaurants.close();
        br.close();
    }
}
