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
        if (UserService.Cart == null || UserService.Cart.head == null) {
            System.out.println("\nCart is empty. Nothing to place.");
            return;
        }

        // Derive a restaurant for this order from the first cart item
        LL.Node head = UserService.Cart.head;
        String restaurantId = head.data.getRestaurantId(head.data.getRestaurant());
        if (restaurantId == null || restaurantId.isBlank()) {
            throw new IllegalStateException("Restaurant ID could not be determined from cart.");
        }

        boolean previousAutoCommit = AppConstants.connection.getAutoCommit();
        AppConstants.connection.setAutoCommit(false);
        Savepoint sp = null;

        Integer orderId = null;

        try {
            sp = AppConstants.connection.setSavepoint();

            // 1) Insert record into the order table.
            // If your DB provides a default timestamp, you can omit order_time.
            String insertOrderSQL = "INSERT INTO orders(uid, rid, order_time_stamp) VALUES(?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement psOrder = AppConstants.connection.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, uid);
                psOrder.setString(2, restaurantId);
                psOrder.executeUpdate();
                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to obtain generated order ID.");
                    }
                }
            }

            // 2) Insert all cart items into order_items (batched)
            String insertItemSQL = "INSERT INTO order_items(o_id, did, quantity) VALUES(?, ?, ?)";
            try (PreparedStatement psItem = AppConstants.connection.prepareStatement(insertItemSQL)) {
                LL.Node n = head;
                while (n != null) {
                    psItem.setInt(1, orderId);
                    psItem.setString(2, n.data.getDish_id());
                    psItem.setInt(3, n.quantity);
                    psItem.addBatch();
                    n = n.next;
                }
                psItem.executeBatch();
            }

            // 3) Prepare payment info before invoking payment routine
            Payment.payment = new Payment();
            Payment.payment.user_id = uid;
            Payment.payment.order_id = String.valueOf(orderId);
            Payment.payment.restaurant_id = restaurantId;
            // Optional: if your flow requires defaults before PaymentService sets them
            if (Payment.payment.paymentType == null) Payment.payment.paymentType = "UNKNOWN";
            if (Payment.payment.paymentStatus == null) Payment.payment.paymentStatus = "PENDING";

            // 4) Process payment
            boolean paymentSuccess = PaymentService.paymentInterface();

            // 6) Commit and finalize
            AppConstants.connection.commit();
            Thread.sleep(1000);
            Sound.playWav("/zomato_app.wav"); // starting slash is important
            Thread.sleep(500);
            System.out.println("Order placed and payment completed successfully!");

            // Clear the cart only after a successful commit
            UserService.Cart.clearList();
            UserService.isEmpty = true;

        } catch (Exception ex) {
            try {
                if (sp != null) AppConstants.connection.rollback(sp);
            } catch (Exception ignore) {
            }
            // Re-throw so the caller can log/ handle or print a friendly message here
            throw ex;
        } finally {
            try {
                AppConstants.connection.setAutoCommit(previousAutoCommit);
            } catch (Exception ignore) {
            }
        }
    }
}