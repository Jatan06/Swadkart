package Dao;

import Constants.AppConstants;
import Models.Payment;
import java.sql.PreparedStatement;

public class PaymentDAO {
    public static void savePaymentDetails(boolean success,Payment p) throws Exception {
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
}
