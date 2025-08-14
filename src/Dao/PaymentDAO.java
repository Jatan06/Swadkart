package Dao;
import Constants.AppConstants;
import Services.*;
import Ds.*;
import Models.*;
import java.sql.*;

public class PaymentDAO {
    public static void savePaymentDetails(boolean success) throws Exception {
        AppConstants.connection.setAutoCommit(false);
        Payment p = Payment.payment;
        PreparedStatement ps = AppConstants.connection.prepareStatement("INSERT INTO payment (paymentType,paymentStatus,amount,u_id,o_id,r_id) VALUES(?,?,?,?,?,?)");
        ps.setString(1,p.paymentType);
        ps.setString(2,p.paymentStatus);
        ps.setDouble(3,p.amount);
        ps.setString(4,p.user_id);
        ps.setString(5,p.order_id);
        ps.setString(6,p.restaurant_id);
        if(ps.executeUpdate()<=0){
            throw new Exception("Insertion in payment table fails");
        }
        else {
            if(success) {
                AppConstants.connection.commit();
            }
            else {
                AppConstants.connection.rollback();
                throw new Exception("Payment not completed. Please try again.");
            }
        }
    }
}
