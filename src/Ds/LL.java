package Ds;
import Models.Dish;

class Node {
    Dish data;
    int quantity;
    Node next;

    public Node(Dish data,int quantity) {
        this.data = data;
        this.quantity = quantity;
        this.next = null;
    }
}

public class LL {
    private Node head;

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
        System.out.println("Dish inserted: " + dish.getName());
        System.out.println("Quantity : " + quantity);
    }

    // Display all dishes
    public void display() {
        if (head == null) {
            System.out.println("\nCart is empty, Nothing to display.");
            return;
        }

        Node current = head;
        System.out.println("Dish List:");
        while (current != null) {
            System.out.println("\nDish Id :- " +current.data.getDish_id()+"\nDish Name :- "+current.data.getName() + "\nQuantity " + current.quantity+"\nPer unit price :- "+current.data.getPrice()+"\nTotal Price :- "+current.quantity*current.data.getPrice()+"\n");
            current = current.next;
        }
    }

    // Delete by dishId
    public void delete(String dishId) {
        if (head == null) {
            System.out.println("Cart is empty. Nothing to delete.");
            return;
        }

        // Special case: head is the one to delete
        if (head.data.getDish_id().equalsIgnoreCase(dishId)) {
            System.out.println("Deleting: " + head.data.getDish_id());
            head = head.next;
            return;
        }

        Node current = head;
        Node prev = null;

        while (current != null && current.data.getDish_id() != dishId) {
            prev = current;
            current = current.next;
        }

        if (current == null) {
            System.out.println("Dish with ID " + dishId + " not found.");
            return;
        }

        // Delete node
        System.out.println("Deleting: " + current.data.getName());
        prev.next = current.next;
    }

    // Clear the entire list
    public void clearList() {
        head = null;
        System.out.println("All dishes have been removed from the list.");
    }
}