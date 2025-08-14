package Services;

import Dao.PaymentDAO;
import Ds.LL;
import Models.Payment;

public class PaymentService {

    public static boolean paymentInterface() {
        LL cart = UserService.Cart;
        if (cart == null || cart.head == null) {
            System.out.println("Cart is empty. Nothing to pay for.");
            return false;
        }

        // Calculate subtotal and display summary
        double subtotal = 0.0;
        System.out.println("\n--- Order Summary ---");
        LL.Node current = cart.head;
        while (current != null) {
            double price = current.data.getPrice();
            int qty = current.quantity;
            double lineTotal = qty * price;
            System.out.printf("• %s (x%d) @ %.2f = %.2f%n",
                    current.data.getName(), qty, price, lineTotal);
            subtotal += lineTotal;
            current = current.next;
        }

        double taxRate = 0.05;
        double tax = round2(subtotal * taxRate);
        double total = round2(subtotal + tax);

        System.out.printf("Subtotal: %.2f%n", subtotal);
        System.out.printf("Tax (%.0f%%): %.2f%n", taxRate * 100, tax);
        System.out.printf("Total Due: %.2f%n", total);

        // Set the amount in a Payment object
        if (Payment.payment != null) {
            Payment.payment.amount = total;
        }

        // Select a payment method
        System.out.println("\nSelect payment method:");
        System.out.println("1) Cash");
        System.out.println("2) Card");
        System.out.println("3) UPI");
        System.out.print("Enter choice (1-3): ");

        String choice = UserService.scanner.next().trim();
        boolean success = false;

        switch (choice) {
            case "1":
                success = handleCash(total);
                if (Payment.payment != null) Payment.payment.paymentType = "cash";
                break;
            case "2":
                success = handleCard(total);
                if (Payment.payment != null) Payment.payment.paymentType = "card";
                break;
            case "3":
                success = handleUpi(total);
                if (Payment.payment != null) Payment.payment.paymentType = "upi";
                break;
            default:
                System.out.println("Invalid choice. Payment cancelled.");
                return false;
        }

        // Simulate restaurant confirmation
        if (success) {
            success = simulateRestaurantConfirmation();
            if (Payment.payment != null) Payment.payment.paymentStatus = (success ? "success" : "failed");
        }

        // On success: print receipt, clear cart, save payment
        if (success) {
            printReceipt(cart, subtotal, tax, total);
            cart.clearList();
            UserService.isEmpty = true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            try {
                Payment p = Payment.payment; // capture current payment
                Payment.payment = null;      // reset global pointer immediately
                PaymentDAO.savePaymentDetails(success, p);
            } catch (Exception e) {
                throw new RuntimeException("Error saving payment details", e);
            }
        }
        else {
            System.out.println("Payment/order failed. Transaction rolled back.");
        }

        return success;
    }

    // ---------------- Payment Handlers ----------------

    private static boolean handleCash(double total) {
        System.out.print("Enter cash received: ");
        String in = UserService.scanner.next().trim();
        try {
            double received = Double.parseDouble(in);
            if (received < total) {
                System.out.printf("Insufficient amount. Need %.2f more.%n", round2(total - received));
                return false;
            }
            System.out.printf("Payment successful. Change: %.2f%n", round2(received - total));
            return true;
        } catch (Exception e) {
            System.out.println("Invalid amount. Payment failed.");
            return false;
        }
    }

    private static boolean handleCard(double total) {
        System.out.print("Enter card number (16 digits): ");
        String card = UserService.scanner.next().replaceAll("\\s+", "");
        if (card.length() != 16) {
            System.out.println("Invalid card number.");
            return false;
        }
        System.out.print("Enter expiry (MM/YY): ");
        String expiry = UserService.scanner.next().trim();
        System.out.print("Enter CVV: ");
        String cvv = UserService.scanner.next().trim();

        if (expiry.isEmpty() || cvv.length() != 3) {
            System.out.println("Invalid card details.");
            return false;
        }

        System.out.printf("Card charged %.2f. Payment successful. **** **** **** %s%n",
                total, card.substring(card.length() - 4));
        return true;
    }

    private static boolean handleUpi(double total) {
        System.out.print("Enter UPI ID (e.g., user@bank): ");
        String upi = UserService.scanner.next().trim();
        if (!upi.contains("@") || upi.startsWith("@") || upi.endsWith("@")) {
            System.out.println("Invalid UPI ID.");
            return false;
        }
        System.out.printf("UPI collect request for %.2f sent to %s...%n", total, upi);
        System.out.println("Payment successful.");
        return true;
    }

    // ---------------- Helper Methods ----------------

    private static boolean simulateRestaurantConfirmation() {
        System.out.print("\nConfirming order with restaurant ");
        for (int i = 0; i < 6; i++) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            System.out.print(".");
        }
        System.out.println();

        boolean accepted = Math.random() < 0.99;
        if (accepted) {
            System.out.println("\nRestaurant confirmed your order! Preparing your food.");
        } else {
            System.out.println("\nRestaurant declined the order. Payment will be auto-refunded (demo).");
        }
        return accepted;
    }

    private static void printReceipt(LL cart, double subtotal, double tax, double total) {
        System.out.println("\n====== RECEIPT ======");
        LL.Node current = cart.head;
        while (current != null) {
            double price = current.data.getPrice();
            int qty = current.quantity;
            System.out.printf("%s x%d @ %.2f = %.2f%n",
                    current.data.getName(), qty, price, round2(qty * price));
            current = current.next;
        }
        System.out.println("---------------------");
        System.out.printf("Subtotal: %.2f%n", subtotal);
        System.out.printf("Tax: %.2f%n", tax);
        System.out.println("---------------------");
        System.out.printf("TOTAL: %.2f%n", total);
        System.out.println("=====================\n");
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
