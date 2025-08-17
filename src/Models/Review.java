package Models;

public class Review {
    public static Review rev;
    private String restaurant_id;
    private String user_id;
    private String order_id;
    private String dish_id;
    private Double review;
    private String feedback;
    public Review(String restaurant_id, String user_id, String order_id, String dish_id, double review, String feedback) {
        this.restaurant_id = restaurant_id;
        this.user_id = user_id;
        this.order_id = order_id;
        this.dish_id = dish_id;
        this.review = review;
        this.feedback = feedback;
    }
    public static Review getRev() {
        return rev;
    }
    public String getRestaurant_id() {
        return restaurant_id;
    }
    public String getUser_id() {
        return user_id;
    }
    public String getOrder_id() {
        return order_id;
    }
    public String getDish_id() {
        return dish_id;
    }
    public Double getReview() {
        return review;
    }
    public String getFeedback() {
        return feedback;
    }
}