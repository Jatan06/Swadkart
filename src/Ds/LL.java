package Ds;
import Constants.AppConstants;
import Models.Dish;

import java.util.Objects;

public class LL {
    public class Node {
        public Dish data;
        public int quantity;
        public Node next;

        public Node(Dish data,int quantity) {
            this.data = data;
            this.quantity = quantity;
            this.next = null;
        }
    }
    public Node head;

    // Insert at the end
    public void insert(Dish dish, int quantity) {
        Node newNode = new Node(dish, quantity);

        if (head == null) {
            head = newNode;
            System.out.println("\nDish added to cart : " + dish.getName());
            System.out.println("\nQuantity : " + quantity);
            return;
        }
        Node current = head;
        while (true) {
            if (current.data.getDish_id().equalsIgnoreCase(dish.getDish_id())) {
                // Update quantity if dish already exists
                current.quantity += quantity;
                if (current.quantity > 50) {
                    System.out.println("\nSorry we are not placing more than 50 quantity for a dish in cart.");
                    current.quantity -= quantity; // rollback
                    return;
                }
                System.out.println("\nQuantity : " + quantity + " added to dish " + current.data.getName());
                return;
            }
            if (current.next == null) break; // reached end
            current = current.next;
        }
        // If not found in the list, add new node at the end
        current.next = newNode;
        System.out.println("\nDish added to cart : " + dish.getName());
        System.out.println("\nQuantity : " + quantity);
    }


    // Display all dishes
    public void display() {
        if (head == null) {
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+ AppConstants.TEXT_ANSI_RED+"Cart is empty, Nothing to display."+AppConstants.ANSI_RESET);
            return;
        }

        Node current = head;
        System.out.print("\n\t\t\t"+AppConstants.BG_ANSI_BLACK+"============== Cart =============="+AppConstants.ANSI_RESET+"\n");
        while (current != null) {
            System.out.println("\nRestaurant :- "+current.data.getRestaurant()+"\tDish Id :- " +current.data.getDish_id()+"\tDish Name :- "+current.data.getName() + "\tQuantity :- " + current.quantity+"\tPer unit price :- "+current.data.getPrice()+"\tTotal Price :- "+current.quantity*current.data.getPrice()+"\n");
            current = current.next;
        }
    }

    // Display all dishes in a tabular format with all attributes
    public void displayTabular() {
        if (head == null) {
            System.out.println(AppConstants.TEXT_ANSI_RED+"\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCart is empty, Nothing to display."+AppConstants.ANSI_RESET);
            return;
        }
        System.out.println("\n");

        // Column widths and header
        String headerFmt = "%-10s %-22s %-14s %-20s %8s %8s %14s %14s%n";
        String rowFmt    = "%-10s %-22s %-14s %-20s %8.2f %8d %14.2f %14.2f%n";

        String line = repeat('-', 10 + 1 + 22 + 1 + 14 + 1 + 20 + 1 + 8 + 1 + 8 + 1 + 14 + 1 + 14);

        System.out.println(line);
        System.out.printf(headerFmt, "Dish ID", "Name", "Cuisine", "Restaurant", "Rating", "Qty", "Unit Price", "Total Price");
        System.out.println(line);

        Node current = head;
        double grandTotal = 0.0;
        while (current != null) {
            Dish d = current.data;
            double total = current.quantity * d.getPrice();
            grandTotal += total;

            System.out.printf(
                rowFmt,
                safe(d.getDish_id()),
                safe(d.getName()),
                safe(d.getCuisine()),
                safe(d.getRestaurant()),
                d.getRating(),
                current.quantity,
                d.getPrice(),
                total
            );
            current = current.next;
        }
        System.out.println(line);
        System.out.printf("%-10s %-22s %-14s %-20s %8s %8s %14s %14.2f%n",
                "", "", "", "Grand Total:", "", "", "", grandTotal);
        System.out.println(line);
    }

    // Helper: repeat a char n times
    private String repeat(char ch, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(ch);
        return sb.toString();
    }

    // Helper: avoid printing null
    private String safe(String s) {
        return s == null ? "-" : s;
    }
    
    // Delete by dishId
    public boolean delete(String dishId, int quantity) {
        if (head == null) {
            System.out.println("Cart is empty. Nothing to delete.");
            return false;
        }

        // Normalize input
        dishId = dishId.trim();

        // --- Special case: head node ---
        if (head.data.getDish_id().equalsIgnoreCase(dishId)) {
            if (quantity > head.quantity) {
                System.out.println("\n❌ You tried to remove more than available (" + head.quantity + ").");
                return false;
            }
            if (quantity < head.quantity) {
                head.quantity -= quantity;
                System.out.println("\nRemoved " + quantity + " from dish: " + head.data.getName() +
                        " (Remaining: " + head.quantity + ")");
                return true;
            } else { // quantity == head.quantity
                System.out.println("\nRemoved dish: " + head.data.getName() + " completely from cart.");
                head = head.next;
                return true;
            }
        }

        // --- General case: search in list ---
        Node current = head.next;
        Node prev = head;

        while (current != null && !current.data.getDish_id().equalsIgnoreCase(dishId)) {
            prev = current;
            current = current.next;
        }
        if (current == null) {
            System.out.println("❌ Dish with ID " + dishId + " not found in cart.");
            return false;
        }
        if (quantity > current.quantity) {
            System.out.println("\n❌ You tried to remove more than available (" + current.quantity + ").");
            return false;
        }
        if (quantity < current.quantity) {
            current.quantity -= quantity;
            System.out.println("\nRemoved " + quantity + " from dish: " + current.data.getName() +
                    " (Remaining: " + current.quantity + ")");
            return true;
        } else { // quantity == current.quantity
            System.out.println("\nRemoved dish: " + current.data.getName() + " completely from cart.");
            prev.next = current.next;
            return true;
        }
    }


    public boolean checkDish(String dishId) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.getDish_id().equalsIgnoreCase(dishId)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    // Clear the entire list
    public void clearList() {
        head = null;
    }
}