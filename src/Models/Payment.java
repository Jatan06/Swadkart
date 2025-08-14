package Models;

public class Payment {
    public static Payment payment = new Payment();
    public String paymentType;
    public String paymentStatus;
    public double amount;
    public String user_id;
    public String order_id;
    public String restaurant_id;
    Payment() {}
    public Payment(String paymentType, String paymentStatus, double amount, String user_id, String order_id, String restaurant_id) {
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.user_id = user_id;
        this.order_id = order_id;
        this.restaurant_id = restaurant_id;
    }
}
