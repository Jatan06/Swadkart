package Dao;
import java.sql.*;
import java.io.*;
import Constants.*;
import Ds.*;
import Utils.*;
import Services.*;
import Db.*;
import Menus.*;
import Models.*;
import java.util.*;

public class OrderDAO {
    public static int totalOrders;
    public static void getOrderNumber() throws Exception {
        String q = "SELECT COUNT(o_id) FROM orders;";
        PreparedStatement ps = AppConstants.connection.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        rs.next();
        totalOrders = rs.getInt(1);
        totalOrders++;
    }
    public static void orderHistory() throws Exception {
        PreparedStatement ps = AppConstants.connection.prepareCall("SELECT o_id, uid, did, rid, order_time, quantity FROM orders");
        ResultSet rs = ps.executeQuery();

        // ANSI escape codes for styling
        final String RESET = "\u001B[0m";
        final String BOLD = "\u001B[1m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String CYAN = "\u001B[36m";

        // Print table header
        System.out.println("\n" + BOLD + CYAN + "=".repeat(120) + RESET);
        System.out.println(BOLD + CYAN + "\t\t\t Orders Table" + RESET);
        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);

        System.out.printf(BOLD + "%-10s %-12s %-12s %-12s %-28s %-10s%n" + RESET,
                "OID", "User ID", "Dish ID", "Rest ID", "Order Time", "Quantity");
        System.out.println(CYAN + "-".repeat(120) + RESET);

        int count = 0; // Row counter

        // Process the ResultSet
        while (rs.next()) {
            String orderId = rs.getString("o_id");
            String userId = rs.getString("uid");
            String dishId = rs.getString("did");
            String restaurantId = rs.getString("rid");
            String orderTime = String.valueOf(rs.getTimestamp("order_time"));
            int quantity = rs.getInt("quantity");

            // Print record
            System.out.printf("%-10s %-12s %-12s %-12s %-28s %-10d%n",
                    orderId, userId, dishId, restaurantId, orderTime, quantity);
            count++;

            // Add a separator every 20 rows
            if (count % 20 == 0) {
                System.out.println(CYAN + "-".repeat(120) + RESET);
            }
        }

        // Handle empty table case
        if (count == 0) {
            System.out.println(BOLD + CYAN + "No orders found." + RESET); // Empty message
        }

        // Footer
        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);
        System.out.println(BOLD + GREEN + "📊 Total Orders: " + count + RESET);
        System.out.println(BOLD + CYAN + "=".repeat(120) + RESET);
    }
    public static void placeOrder(String uid) throws Exception {
        // Basic guard: ensure the cart is not null/empty to avoid NPEs
        if (Services.UserService.Cart == null || Services.UserService.Cart.head == null) {
            System.out.println("\nCart is empty. Nothing to place.");
            return;
        }

        Dish ob = null;
        // Ensure Payment.payment exists elsewhere before this method is called
        Payment.payment.user_id = uid;

        AppConstants.connection.setAutoCommit(false);

        LL.Node n = UserService.Cart.head;
        long lastInsertedOrderId = -1L;

        while (n != null) {
            ob = n.data;

            final String addOrder = "INSERT INTO orders(uid, did, rid, quantity) VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = AppConstants.connection.prepareStatement(
                    addOrder, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, uid);
                ps.setString(2, ob.getDish_id());
                ps.setString(3, ob.getRestaurantId(ob.getRestaurant()));
                ps.setInt(4, n.quantity);

                int affected = ps.executeUpdate();
                if (affected <= 0) {
                    System.out.printf("\nInsertion in orders table failed for uid,did: %s,%s", uid, ob.getDish_id());
                    AppConstants.connection.rollback();
                    return;
                }

                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) {
                        lastInsertedOrderId = gk.getLong(1);
                    }
                }
            }

            n = n.next;
        }

        if (ob == null) {
            // Should not happen due to earlier guard, but be safe
            AppConstants.connection.rollback();
            System.out.print("\nNo items were processed for the order.");
            return;
        }

        // Set payment metadata from the last processed dish and actual generated order id
        Payment.payment.restaurant_id = ob.getRestaurantId(ob.getRestaurant());
        if (lastInsertedOrderId <= 0) {
            // Could not retrieve generated key; this is a hard failure
            AppConstants.connection.rollback();
            throw new IllegalStateException("Failed to retrieve generated order ID for payment linkage.");
        }
        Payment.payment.order_id = String.valueOf(lastInsertedOrderId);

        // Process payment
        if (PaymentService.paymentInterface()) {
            Sound.playWav("/zomato_app.wav");
            Thread.sleep(1000);
            AppConstants.connection.commit();
        } else {
            AppConstants.connection.rollback();
            System.out.print("\nPayment not completed. Please try again.");
        }
    }
}