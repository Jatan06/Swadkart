package Dao;
import java.sql.*;
import java.io.*;
import Constants.*;
import Utils.*;
import Services.*;
import Dao.*;
import Db.*;
import Menus.*;
import Models.*;
import java.util.*;
public class OrderDAO {
    public static void orderHistory() throws Exception {
    PreparedStatement ps = AppConstants.connection.prepareCall("SELECT * FROM orders");
    ResultSet rs = ps.executeQuery();

    // ANSI escape codes for styling
    final String RESET = "\u001B[0m";
    final String BOLD = "\u001B[1m";
    final String GREEN = "\u001B[32m";
    final String YELLOW = "\u001B[33m";
    final String CYAN = "\u001B[36m";

    // Print table header
    System.out.println("\n" + BOLD + CYAN + "=".repeat(95) + RESET);
    System.out.println(BOLD + CYAN + "\t\t\t Orders Table" + RESET);
    System.out.println(BOLD + CYAN + "=".repeat(95) + RESET);

    System.out.printf(BOLD + "%-8s %-12s %-12s %-12s %-25s%n" + RESET,
            "OID", "User ID", "Dish ID", "Rest ID", "Created At");
    System.out.println(CYAN + "-".repeat(95) + RESET);

    int count = 0; // Row counter

    // Process the ResultSet
    while (rs.next()) {
        String orderId = rs.getString("oid");
        String userId = rs.getString("uid");
        String dishId = rs.getString("did");
        String restaurantId = rs.getString("rid");
        String createdAt = rs.getTimestamp("created_at").toString();

        // Print record
        System.out.printf("%-8s %-12s %-12s %-12s %-25s%n",
                orderId, userId, dishId, restaurantId, createdAt);
        count++;

        // Add separator every 20 rows
        if (count % 20 == 0) {
            System.out.println(CYAN + "-".repeat(95) + RESET);
        }
    }

    // Handle empty table case
    if (count == 0) {
        System.out.println(BOLD + CYAN + "No orders found." + RESET); // Empty message
    }

    // Footer
    System.out.println(BOLD + CYAN + "=".repeat(95) + RESET);
    System.out.println(BOLD + GREEN + "📊 Total Orders: " + count + RESET);
    System.out.println(BOLD + CYAN + "=".repeat(95) + RESET);
}
}
