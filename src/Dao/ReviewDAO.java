package Dao;
import Constants.*;
import Models.*;
import Ds.*;

import java.sql.PreparedStatement;

public class ReviewDAO {
    public static void insertReview(Ds.LL cart,String uid) throws Exception {
        Ds.LL.Node temp = cart.head;
        while (temp != null) {
            AppConstants.connection.setAutoCommit(false);
            System.out.print("\nRate dish "+temp.data.getName()+" :- ");
            double rating = AppConstants.s.nextDouble();
            while (true) {
                if (rating >= 1 && rating <= 5)
                    break;
                else {
                    System.out.println("Enter valid rating (1-5) : ");
                    rating = AppConstants.s.nextDouble();
                }
            }
            AppConstants.s.nextLine();
            System.out.print("\nWrite your feedback :- ");
            String feedback = AppConstants.s.nextLine();
            Review.rev = new Review(temp.data.getRestaurantId(temp.data.getRestaurant()),uid,OrderDAO.findOrderIdByUserRestaurantAndDish(uid,temp.data.getRestaurantId(temp.data.getRestaurant()),temp.data.getDish_id()),temp.data.getDish_id(),rating,feedback);
            String rev = "INSERT INTO reviews(r_id,u_id,o_id,d_id,review,feedback) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = AppConstants.connection.prepareCall(rev);
            ps.setString(1,Review.rev.getRestaurant_id());
            ps.setString(2,Review.rev.getUser_id());
            ps.setString(3,Review.rev.getOrder_id());
            ps.setString(4,Review.rev.getDish_id());
            ps.setDouble(5,Review.rev.getReview());
            ps.setString(6,Review.rev.getFeedback());
            if(ps.executeUpdate()<=0) {
                System.out.println("\nReview not added, please try again for dish name "+temp.data.getName()+" id :- "+temp.data.getDish_id());
                AppConstants.connection.rollback();
            }
            else {
                AppConstants.connection.commit();
            }
            temp = temp.next;
        }
    }
    public static void insertReviewByDishId(Ds.LL cart,String dishId,String uid) throws Exception{
        Ds.LL.Node temp = cart.head;
        while (temp != null) {
            if(temp.data.getDish_id().equalsIgnoreCase(dishId)) {
                AppConstants.connection.setAutoCommit(false);
                System.out.print("\nRate dish "+temp.data.getName()+" :- ");
                double rating = AppConstants.s.nextDouble();
                while (true) {
                    if (rating >= 1 && rating <= 5)
                        break;
                    else {
                        System.out.println("Enter valid rating (1-5) : ");
                        rating = AppConstants.s.nextDouble();
                    }
                }
                System.out.print("\nWrite your feedback :- ");
                String feedback = AppConstants.s.nextLine();
                AppConstants.s.nextLine();
                Review.rev = new Review(temp.data.getRestaurantId(temp.data.getRestaurant()),uid,OrderDAO.findOrderIdByUserRestaurantAndDish(uid,temp.data.getRestaurantId(temp.data.getRestaurant()),temp.data.getDish_id()),temp.data.getDish_id(),rating,feedback);
                String rev = "INSERT INTO reviews(r_id,u_id,o_id,d_id,review,feedback) VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = AppConstants.connection.prepareCall(rev);
                ps.setString(1,Review.rev.getRestaurant_id());
                ps.setString(2,Review.rev.getUser_id());
                ps.setString(3,Review.rev.getOrder_id());
                ps.setString(4,Review.rev.getDish_id());
                ps.setDouble(5,Review.rev.getReview());
                ps.setString(6,Review.rev.getFeedback());
                if(ps.executeUpdate()<=0) {
                    System.out.println("\nReview not added, please try again !");
                    AppConstants.connection.rollback();
                }
                else {
                    AppConstants.connection.commit();
                }
                break;
            }
            temp = temp.next;
        }
    }
}
