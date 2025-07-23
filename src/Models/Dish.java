package Models;

public class Dish {
    private String dish_id;
    private String name;
    private String cuisine;
    private String restaurant;
    private double rating;
    private double price;

    public Dish(String dish_id, String name, String cuisine, String restaurant, double rating, double price) {
        this.dish_id = dish_id;
        this.name = name;
        this.cuisine = cuisine;
        this.restaurant = restaurant;
        this.rating = rating;
        this.price = price;
    }

    public String getDish_id() {
        return dish_id;
    }

    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public double getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }
}
