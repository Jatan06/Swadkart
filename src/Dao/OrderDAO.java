package Dao;

import Constants.*;
import java.sql.*;
import java.util.*;
import Models.*;
import Services.*;
import Ds.*;
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
        PreparedStatement ps = AppConstants.connection.prepareCall("SELECT * FROM orders");
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
            if (Payment.payment.paymentType == null) Payment.payment.paymentType = "UNKNOWN";
            if (Payment.payment.paymentStatus == null) Payment.payment.paymentStatus = "PENDING";

            // 4) Process payment
            boolean paymentSuccess = PaymentService.paymentInterface();

            // 6) Commit and finalize
            if(paymentSuccess) {
                AppConstants.connection.commit();
                Thread.sleep(1000);
                String op = "\nOrder placed and payment completed successfully!";
                Sound.playWav("/zomato_app.wav");
                System.out.println(op);
                Thread.sleep(1000);
                System.out.print("\nWould you like to give review (y/n) :- ");
                if(AppConstants.s.next().trim().equalsIgnoreCase("y")) {
                    UserService.Cart.displayTabular();
                    boolean review = true;
                    while (review) {
                        System.out.println("would you like to give all dishes (Enter 'all') review or enter dish id :- ");
                        String dishId = AppConstants.s.next().trim();
                        if (dishId.equalsIgnoreCase("all")) {
                            ReviewDAO.insertReview(UserService.Cart, uid);
                            review = false;
                        } else {
                            ReviewDAO.insertReviewByDishId(UserService.Cart, dishId, uid);
                            System.out.println("Would you like to give review for another dish (y/n) :-");
                            if(AppConstants.s.next().trim().equalsIgnoreCase("y")) {
                                continue;
                            } else {
                                review = false;
                            }
                        }
                    }
                }
                UserService.Cart.clearList();
                UserService.isEmpty = true;
            }
            else {
                AppConstants.connection.rollback(sp);
                System.out.println("Order placed but payment failed!");
            }
        } catch (Exception ex) {
            try {
                if (sp != null) AppConstants.connection.rollback(sp);
            } catch (Exception ignore) {
            }
            throw ex;
        } finally {
            try {
                AppConstants.connection.setAutoCommit(previousAutoCommit);
            } catch (Exception ignore) {
            }
        }
    }

    // New: Display Orders and Order Items with smart FK handling and formatting
    public static void viewOrderAndOrderItems() throws Exception {
        String sqlOrders = "SELECT * FROM orders GROUP BY uid";
        String sqlOrderItems = "SELECT * FROM order_items GROUP BY o_id";

        try (Statement st = AppConstants.connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(sqlOrders)) {
                printResultSetAsTable("ORDERS", rs);
            }
            try (ResultSet rs = st.executeQuery(sqlOrderItems)) {
                printResultSetAsTable("ORDER ITEMS", rs);
            }
        }
    }

    public static void viewOrderAndOrderItems(String uid) throws Exception {
        // Single, FK-based joined view across orders, order_items, and payment
        String sql = ""
                + "SELECT "
                + "  o.o_id               AS o_id, "
                + "  o.rid                AS rid, "
                + "  o.order_time_stamp   AS order_time_stamp, "
                + "  GROUP_CONCAT(CONCAT(oi.did, ' (Qty: ', oi.quantity, ')') SEPARATOR ', ') AS items, "
                + "  p.payment_id         AS payment_id, "
                + "  p.paymentType        AS paymentType, "
                + "  p.paymentStatus      AS paymentStatus, "
                + "  p.amount             AS amount "
                + "FROM orders o "
                + "LEFT JOIN order_items oi "
                + "  ON oi.o_id = o.o_id "
                + "LEFT JOIN payment p "
                + "  ON p.o_id = o.o_id "
                + " AND p.u_id = o.uid "
                + " AND p.r_id = o.rid "
                + "WHERE o.uid = ? "
                + "GROUP BY o.o_id, o.rid, o.order_time_stamp, p.payment_id, p.paymentType, p.paymentStatus, p.amount "
                + "ORDER BY o.o_id, p.payment_id";


        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                printResultSetAsTable("ORDERS AND TRANSACTIONS", rs);
            }
        }
    }

    /**
     * Finds the latest order id (o_id) for a given user, restaurant, and dish.
     * Returns null if no matching order exists.
     */
    public static String findOrderIdByUserRestaurantAndDish(String userId, String restaurantId, String dishId) throws Exception {
        final String sql =
                "SELECT o.o_id AS o_id " +
                "FROM orders o " +
                "JOIN order_items oi ON oi.o_id = o.o_id " +
                "WHERE o.uid = ? AND o.rid = ? AND oi.did = ? " +
                "ORDER BY o.order_time_stamp DESC " +
                "LIMIT 1";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, restaurantId);
            ps.setString(3, dishId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("o_id") : null;
            }
        }
    }

    // ---------- Helpers for tabular display ----------

    private static void printResultSetAsTable(String title, ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();

        List<String[]> rows = new ArrayList<>();
        int[] widths = new int[colCount];
        String[] headers = new String[colCount];
        int[] types = new int[colCount];

        for (int i = 1; i <= colCount; i++) {
            headers[i - 1] = md.getColumnLabel(i);
            types[i - 1] = md.getColumnType(i);
            widths[i - 1] = Math.max(3, headers[i - 1].length());
        }

        while (rs.next()) {
            String[] row = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                String colName = headers[i - 1];
                int sqlType = types[i - 1];

                Object val = rs.getObject(i);
                String txt = formatValue(colName, sqlType, val);
                row[i - 1] = txt;
                widths[i - 1] = Math.max(widths[i - 1], txt.length());
            }
            rows.add(row);
        }

        // Build format strings
        String[] colFmts = new String[colCount];
        for (int i = 0; i < colCount; i++) {
            boolean rightAlign = isNumeric(types[i]);
            colFmts[i] = rightAlign ? "%"+widths[i]+"s" : "%-"+widths[i]+"s";
        }

        String sep = buildSeparator(widths);

        StringBuilder headerFmt = new StringBuilder("| ");
        for (int i = 0; i < colCount; i++) {
            headerFmt.append(String.format("%%-%ds", widths[i])).append(" | ");
        }
        headerFmt.append("%n");

        StringBuilder rowFmt = new StringBuilder("| ");
        for (int i = 0; i < colCount; i++) {
            rowFmt.append(colFmts[i]).append(" | ");
        }
        rowFmt.append("%n");

        // Print table
        System.out.println();
        System.out.println(centerTitleInSeparator(title, sep));
        System.out.println(sep);
        System.out.printf(headerFmt.toString(), (Object[]) headers);
        System.out.println(sep);

        if (rows.isEmpty()) {
            System.out.printf("| %s |%n", padRight("No records found.", sep.length() - 4));
            System.out.println(sep);
            return;
        }

        for (String[] row : rows) {
            System.out.printf(rowFmt.toString(), (Object[]) row);
        }
        System.out.println(sep);
    }

    private static String formatValue(String colName, int sqlType, Object val) {
        if (val == null) {
            // For foreign keys, show "-"
            if (looksLikeForeignKey(colName)) return "-";
            return "-";
        }

        // Money-like formatting
        if (looksLikeMoney(colName) && val instanceof Number) {
            return String.format(java.util.Locale.US, "%,.2f", ((Number) val).doubleValue());
        }

        if (isNumeric(sqlType)) {
            return val.toString();
        }

        if (val instanceof java.sql.Timestamp || val instanceof java.sql.Date || val instanceof java.sql.Time) {
            return val.toString();
        }

        String s = val.toString();
        return (s == null || s.isEmpty()) ? "-" : s;
    }

    private static boolean looksLikeForeignKey(String colName) {
        String n = colName.toLowerCase(java.util.Locale.ROOT);
        return n.endsWith("_id")
                || n.equals("o_id") || n.equals("uid") || n.equals("did") || n.equals("rid")
                || n.equals("order_id") || n.equals("item_id") || n.equals("payment_id");
    }

    private static boolean looksLikeMoney(String colName) {
        String n = colName.toLowerCase(java.util.Locale.ROOT);
        return n.contains("amount") || n.contains("price") || n.contains("total");
    }

    private static boolean isNumeric(int sqlType) {
        return switch (sqlType) {
            case Types.BIT, Types.BOOLEAN,
                 Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT,
                 Types.FLOAT, Types.REAL, Types.DOUBLE,
                 Types.NUMERIC, Types.DECIMAL -> true;
            default -> false;
        };
    }

    private static String buildSeparator(int[] widths) {
        int total = 1; // starting '|'
        for (int w : widths) total += 1 + w + 1 + 1; // " " + content + " " + "|"
        return repeat('-', total);
    }

    private static String centerTitleInSeparator(String title, String sep) {
        int len = sep.length();
        String t = " " + title + " ";
        if (t.length() >= len) return t.substring(0, len);
        int pad = (len - t.length()) / 2;
        return repeat('-', pad) + t + repeat('-', len - pad - t.length());
    }

    private static String repeat(char c, int count) {
        if (count <= 0) return "";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }

    private static String padRight(String s, int w) {
        if (s.length() >= w) return s;
        StringBuilder sb = new StringBuilder(w);
        sb.append(s);
        for (int i = s.length(); i < w; i++) sb.append(' ');
        return sb.toString();
    }
}