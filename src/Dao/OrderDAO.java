package Dao;

import Constants.*;
import java.sql.*;
import java.util.*;
import Models.*;
import Services.*;
import Ds.*;
public class OrderDAO {
    public static double total;

    public static void placeOrder(String uid) throws Exception {
        if (UserService.Cart == null || UserService.Cart.head == null) {
            System.out.println(AppConstants.TEXT_ANSI_RED +
                    "\n\t\t\tCart is empty, nothing to place order." +
                    AppConstants.ANSI_RESET);
            return;
        }

        // --- First confirmation ---
        if (!confirmOrderPrompt()) {
            System.out.println("\n" + AppConstants.BG_ANSI_BLACK + AppConstants.TEXT_ANSI_RED +
                    "Order cancelled. Do you want to clear your cart ? (y/n) :- " +
                    AppConstants.ANSI_RESET);
            while (true) {
                String token = AppConstants.s.next().trim();
                if (token.equalsIgnoreCase("y")) {
                    UserService.Cart.clearList();
                    return;
                } else if (token.equalsIgnoreCase("n")) {
                    return;
                } else {
                    System.out.print("\nEnter y/n only :- ");
                }
            }
        }

        // Restaurant from first cart item
        LL.Node head = UserService.Cart.head;
        String restaurantId = head.data.getRestaurantId(head.data.getRestaurant());
        if (restaurantId == null || restaurantId.isBlank()) {
            throw new IllegalStateException("Restaurant ID could not be determined from cart.");
        }

        // --- Payment (no DB yet) ---
        Payment.payment = new Payment();
        Payment.payment.user_id = uid;
        Payment.payment.restaurant_id = restaurantId;
        Payment.payment.paymentStatus = "PENDING";

        boolean paymentSuccess = PaymentService.paymentInterface();
        if (!paymentSuccess) {
            System.out.println("Payment failed. Order not placed.");
            return;
        }

        // --- Second confirmation ---
        if (!confirmAfterPayment()) {
            System.out.println("Order cancelled. Payment refunded.");
            UserService.Cart.clearList();
            UserService.isEmpty = true;
            return;
        }

        // --- DB writes AFTER second confirm ---
        boolean prevAuto = AppConstants.connection.getAutoCommit();
        AppConstants.connection.setAutoCommit(false);
        Savepoint sp = null;
        Integer orderId = null;

        try {
            sp = AppConstants.connection.setSavepoint();

            // orders
            String insertOrderSQL = "INSERT INTO orders(uid, rid, order_time_stamp) VALUES(?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement psOrder = AppConstants.connection.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, uid);
                psOrder.setString(2, restaurantId);
                psOrder.executeUpdate();
                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) orderId = rs.getInt(1);
                    else throw new SQLException("Failed to obtain generated order ID.");
                }
            }

            // order_items
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

            AppConstants.connection.commit();

            // --- Success UX ---
            Thread.sleep(10000);
            System.out.println("\n✅ Order placed successfully!");
            try { Sound.playWav("/zomato_app.wav"); } catch (Exception ignore) {}

            // --- Cash collection at delivery (now that order exists) ---
            if ("1".equals(PaymentService.choice)) {
                double totalDue = OrderDAO.total; // set during paymentInterface
                boolean paid = false;
                while (!paid) {
                    System.out.print("\nEnter cash received: ");
                    String in = AppConstants.s.next().trim();
                    try {
                        double received = Double.parseDouble(in);
                        if (received < totalDue) {
                            double shortAmt = round2(totalDue - received);
                            System.out.printf(AppConstants.TEXT_ANSI_RED + "\n❌ Insufficient amount." +
                                    AppConstants.ANSI_RESET + " Need ₹%.2f more.%n", shortAmt);
                        } else {
                            double change = round2(received - totalDue);
                            System.out.printf(AppConstants.TEXT_ANSI_GREEN + "\n✅ Cash received. " +
                                    AppConstants.ANSI_RESET + "Change: ₹%.2f%n", change);
                            paid = true;
                            Payment.payment.paymentStatus = "paid";
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(AppConstants.TEXT_ANSI_RED + "\n❌ Invalid amount." +
                                AppConstants.ANSI_RESET + " Please enter a valid number.");
                    }
                }
            }

            // payment row now (linked with orderId)
            Payment.payment.order_id = String.valueOf(orderId);
            PaymentDAO.savePaymentDetails(true, Payment.payment);

            // --- Review flow (your original behavior) ---
            System.out.print("\nWould you like to give review (y/n) :- ");
            if (AppConstants.s.next().trim().equalsIgnoreCase("y")) {
                UserService.Cart.displayTabular(); // show what was ordered
                boolean review = true;
                while (review) {
                    System.out.print("\nwould you like to give all dishes (Enter 'all') review or enter dish id or enter 'b' to go back :- ");
                    String dishId = AppConstants.s.next().trim();
                    if (dishId.equalsIgnoreCase("b")) break;
                    if (dishId.equalsIgnoreCase("all") || dishId.equalsIgnoreCase("a")) {
                        ReviewDAO.insertReview(UserService.Cart, uid, String.valueOf(orderId));
                        review = false;
                    } else {
                        if(dishId.length()==1) {dishId = "VD-000"+dishId;}
                        else if(dishId.length()==2) {dishId = "VD-00"+dishId;}
                        else if(dishId.length()==3) {dishId = "VD-0"+dishId;}
                        else if(dishId.length()==4) {dishId = "VD-"+dishId;}
                        ReviewDAO.insertReviewByDishId(UserService.Cart, dishId, uid, String.valueOf(orderId));

                        System.out.print("\nWould you like to give review for another dish (y/n) or enter 'b' to go back :- ");
                        while (true) {
                            String t = AppConstants.s.next().trim();
                            if (t.equalsIgnoreCase("b")) { review = false; break; }
                            else if (t.equalsIgnoreCase("y")) { break; }
                            else if (t.equalsIgnoreCase("n")) { review = false; break; }
                            else System.out.print("\nInvalid input. Please enter 'y' or 'n' or 'b' :- ");
                        }
                    }
                }
            }

            // finally clear
            UserService.Cart.clearList();
            UserService.isEmpty = true;

        } catch (Exception ex) {
            try { if (sp != null) AppConstants.connection.rollback(sp); } catch (Exception ignore) {}
            throw ex;
        } finally {
            try { AppConstants.connection.setAutoCommit(prevAuto); } catch (Exception ignore) {}
        }
    }

    private static boolean confirmOrderPrompt() {
        System.out.print("\nAre you sure you want to confirm the order (y/n): ");
        while (true) {
            String token = AppConstants.s.next().trim();
            if (token.equalsIgnoreCase("y")) return true;
            if (token.equalsIgnoreCase("n")) return false;
            System.out.print("\nEnter y/n only: ");
        }
    }

    private static boolean confirmAfterPayment() {
        System.out.print(AppConstants.TEXT_ANSI_GREEN +
                "Payment successful." + AppConstants.ANSI_RESET +
                "\n\nConfirm order to finalize (y/n) :- ");
        while (true) {
            String token = AppConstants.s.next().trim();
            if (token.equalsIgnoreCase("y")) return true;
            if (token.equalsIgnoreCase("n")) return false;
            System.out.print("Enter y/n only: ");
        }
    }


    public static void viewOrderAndOrderItems() throws Exception {
        String sqlOrders = "SELECT * FROM orders GROUP BY uid";
        String sqlOrderItems = "SELECT * FROM order_items GROUP BY o_id";

        String sqlJoin = "SELECT o_id AS order-id,uid as user-id,rid as restaurant-id,order_time_stamp AS order-time,GROUP_CONCAT(CONCAT(did, ' (Qty: ', quantity, ')') SEPARATOR ', ') AS items FROM orders o JOIN order_items oi ON oi.o_id = o.o_id GROUP BY o_id,uid,rid,order_time_stamp ORDER BY o_id,order-time_stamp DESC";

        try (Statement st = AppConstants.connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(sqlOrderItems)) {
                printResultSetAsTable("ORDERS", rs);
            }
//            try (ResultSet rs = st.executeQuery(sqlOrderItems)) {
//                printResultSetAsTable("ORDER ITEMS", rs);
//            }
        }
    }

    public static void viewOrderAndOrderItems(String uid) throws Exception {
        // Single, FK-based joined view across orders, order_items, and payment
        String sql = "SELECT "
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

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}