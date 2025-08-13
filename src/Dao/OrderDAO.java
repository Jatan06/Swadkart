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
    public static void placeOrder(String uid) throws Exception{
        AppConstants.connection.setAutoCommit(false);
        LL.Node n = UserService.Cart.head;
        while (n!=null) {
            Dish ob = n.data;
            String addOrder = "INSERT INTO orders(uid,did,rid,quantity) VALUES (?,?,?,?);";
            PreparedStatement ps = AppConstants.connection.prepareCall(addOrder);
            ps.setString(1,uid);
            ps.setString(2,ob.getDish_id());
            ps.setString(3,ob.getRestaurantId(ob.getRestaurant()));
            ps.setInt(4,n.quantity);
            if(ps.executeUpdate()<=0) {
                System.out.printf("\nInsertion in orders table fails for uid,did: "+uid+","+ob.getDish_id());
                break;
            }
            n = n.next;
        }
        if(PaymentService.paymentInterface()) {
            Sound.playWav("/zomato_app.wav");
            Thread.sleep(1000);
            AppConstants.connection.commit();
        }
        else {
            AppConstants.connection.rollback();
            System.out.printf("\nPayment not completed.Please try again.");
            return;
        }
    }
}