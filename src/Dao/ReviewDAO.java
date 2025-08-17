package Dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Constants.*;
import Models.*;
import Ds.*;
import java.sql.*;

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
    // Query helpers for browsing reviews
    public static List<Review> getAll() throws Exception {
        String sql = "SELECT r_id, u_id, o_id, d_id, review, feedback FROM reviews";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Review> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public static List<Review> findByRestaurantId(String restaurantId) throws Exception {
        String sql = "SELECT r_id, u_id, o_id, d_id, review, feedback FROM reviews WHERE r_id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public static List<Review> findByUserId(String userId) throws Exception {
        String sql = "SELECT r_id, u_id, o_id, d_id, review, feedback FROM reviews WHERE u_id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public static List<Review> findByOrderId(String orderId) throws Exception {
        String sql = "SELECT r_id, u_id, o_id, d_id, review, feedback FROM reviews WHERE o_id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public static List<Review> findByDishId(String dishId) throws Exception {
        String sql = "SELECT r_id, u_id, o_id, d_id, review, feedback FROM reviews WHERE d_id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, dishId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public static List<Review> findByRatingRange(double minInclusive, double maxInclusive) throws Exception {
        String sql = "SELECT r_id, u_id, o_id, d_id, review, feedback FROM reviews WHERE review BETWEEN ? AND ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setDouble(1, minInclusive);
            ps.setDouble(2, maxInclusive);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public static List<Review> findByKeyword(String keyword) throws Exception {
        String sql = "SELECT r_id, u_id, o_id, d_id, review, feedback FROM reviews WHERE LOWER(feedback) LIKE ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, "%" + (keyword == null ? "" : keyword.toLowerCase()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    // Maps the current row of ResultSet to Review model
    private static Review mapRow(ResultSet rs) throws Exception {
        String rId = rs.getString("r_id");
        String uId = rs.getString("u_id");
        String oId = rs.getString("o_id");
        String dId = rs.getString("d_id");
        double rating = rs.getDouble("review");
        String feedback = rs.getString("feedback");
        return new Review(rId, uId, oId, dId, rating, feedback);
    }
}