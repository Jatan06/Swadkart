package Ds;
import Constants.AppConstants;
import Models.Dish;

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
    public void insert(Dish dish,int quantity) {
        Node newNode = new Node(dish,quantity);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null)
                current = current.next;
            current.next = newNode;
        }
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
        System.out.println("Dish List:");
        while (current != null) {
            System.out.println("\nDish Id :- " +current.data.getDish_id()+"\nDish Name :- "+current.data.getName() + "\nQuantity :- " + current.quantity+"\nPer unit price :- "+current.data.getPrice()+"\nTotal Price :- "+current.quantity*current.data.getPrice()+"\n");
            current = current.next;
        }
    }

    // Display all dishes in a tabular format with all attributes
    public void displayTabular() {
        if (head == null) {
            System.out.println("\nCart is empty, Nothing to display.");
            return;
        }

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
    public boolean delete(String dishId,int quantity) {
        if (head == null) {
            System.out.println("Cart is empty. Nothing to delete.");
            return false;
        }

        // Special case: head is the one to delete
        if (head.data.getDish_id().equalsIgnoreCase(dishId)) {
            if(quantity> head.quantity) return false;
            System.out.println("\nQuantity " +quantity+" for dish : " + head.data.getName());
            if (quantity< head.quantity) {
                head.quantity = head.quantity - quantity;
                return true;
            }
            else if(quantity== head.quantity){
                head = head.next;
                return true;
            }
        }

        Node current = head;
        Node prev = null;

        while (current != null && current.data.getDish_id() != dishId) {
            prev = current;
            current = current.next;
        }

        if (current == null) {
            System.out.println("Dish with ID " + dishId + " not found.");
            return false;
        }

        // Delete node
        if(quantity> current.quantity){
            return false;
        }
        System.out.println("\nRemoved quantity " +quantity+" for dish : " + current.data.getName());
        if(quantity< current.quantity) {
            current.quantity = current.quantity - quantity;
            return true;
        }
        else if (quantity==current.quantity) {
            prev.next = current.next;
            return true;
        }

        return false;
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