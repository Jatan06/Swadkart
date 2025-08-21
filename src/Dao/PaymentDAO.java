package Dao;

import Constants.AppConstants;
import Models.Payment;
import Utils.Validators;

import java.sql.*;

public class PaymentDAO {
    public static void savePaymentDetails(boolean success, Payment p) throws Exception {
        if (p == null) throw new Exception("Payment object is null.");

        String sql = "INSERT INTO payment (paymentType,paymentStatus,amount,u_id,o_id,r_id) VALUES(?,?,?,?,?,?)";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, p.paymentType);
            ps.setString(2, p.paymentStatus);
            ps.setDouble(3, p.amount);
            ps.setString(4, p.user_id);
            ps.setInt(5, Integer.parseInt(p.order_id));
            ps.setString(6, p.restaurant_id);

            if (ps.executeUpdate() <= 0) throw new Exception("Insertion in payment table failed.");
        }

        if (!success) throw new Exception("Payment not completed. Transaction should be rolled back by caller.");
    }

    public static void viewTransactions() throws Exception {
        String sql = "SELECT payment_id, paymentType, paymentStatus, amount, u_id, o_id, r_id FROM payment";

        // Column headers and widths
        String[] headers = {"payment_id", "paymentType", "paymentStatus", "amount", "u_id", "o_id", "r_id"};
        int[] widths =    {11,           14,             15,              12,       12,     8,      12};

        // Precompute format strings (right-align numerics, left-align text)
        String rowFmt = String.format("| %%%ds | %%-%ds | %%-%ds | %%%ds | %%-%ds | %%%ds | %%-%ds |%%n",
                widths[0], widths[1], widths[2], widths[3], widths[4], widths[5], widths[6]);

        // Separator line
        int totalWidth = 2 + 2; // starting and ending '|' plus a newline elsewhere
        for (int w : widths) totalWidth += w + 3; // space + content + space, plus '|' later
        String sep = repeat('-', totalWidth - 1); // -1 to account for the newline

        try (Statement st = AppConstants.connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // Print header
            System.out.println(sep);
            System.out.printf(rowFmt,
                    headers[0], headers[1], headers[2], headers[3], headers[4], headers[5], headers[6]);
            System.out.println(sep);

            // Print rows
            while (rs.next()) {
                // Read values with null-safety
                String paymentId = String.valueOf(rs.getInt("payment_id"));
                String paymentType = nz(rs.getString("paymentType"));
                String paymentStatus = nz(rs.getString("paymentStatus"));
                String amount = String.format(java.util.Locale.US, "%,.2f", rs.getDouble("amount"));
                String uId = nz(rs.getString("u_id"));
                int oIdVal = rs.getInt("o_id");
                String oId = rs.wasNull() ? "-" : String.valueOf(oIdVal);
                String rId = nz(rs.getString("r_id"));

                System.out.printf(rowFmt, paymentId, paymentType, paymentStatus, amount, uId, oId, rId);
            }

            System.out.println(sep);
        }
    }

    public static void revenueCall() {
        System.out.print("\nEnter restaurant id :- ");
        String resId = AppConstants.s.next().trim();
        if(resId.length()==1) {
            resId = "r-000"+resId;
        }
        else if(resId.length()==2) {
            resId = "r-00"+resId;
        }
        else if(resId.length()==3) {
            resId = "r-0"+resId;
        } else if (resId.length()==4) {
            resId = "r-"+resId;
        } else if (Validators.validateRestaurantId(resId)) {
            double tot_revenue = revenueByRestaurantId(resId);
            System.out.println("Total revenue for restaurant "+RestaurantDAO.getRestaurantNameById(resId)+" is "+tot_revenue);
            return;
        } else {
            System.out.println(AppConstants.TEXT_ANSI_RED+"Invalid restaurant id."+AppConstants.ANSI_RESET+" or enter 'b' to go back. :- ");
            if(AppConstants.s.next().trim().equalsIgnoreCase("b")) {
                return;
            }
        }
        if(Validators.validateRestaurantId(resId)) {
            double tot_revenue = revenueByRestaurantId(resId);
            System.out.println("Total revenue for restaurant "+RestaurantDAO.getRestaurantNameById(resId)+" is "+tot_revenue);
        }
        else return;
    }

    private static double revenueByRestaurantId(String resId) {
        double totalRevenue = 0.0;
        String sql = "{ ? = CALL showRevenueByRestaurantId(?) }";
        try (CallableStatement cs = AppConstants.connection.prepareCall(sql)) {
            // First ? is return value
            cs.registerOutParameter(1, java.sql.Types.DECIMAL);
            // Second ? is input parameter
            cs.setString(2, resId);
            cs.execute();
            totalRevenue = cs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    private static String repeat(char c, int count) {
        if (count <= 0) return "";
        StringBuilder sb = new StringBuilder(count);
        sb.append(String.valueOf(c).repeat(count));
        return sb.toString();
    }

    private static String nz(String s) {
        return (s == null || s.isEmpty()) ? "-" : s;
    }
}
