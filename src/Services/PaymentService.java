package Services;

import Dao.PaymentDAO;
import Ds.LL;
import Models.Payment;
public class PaymentService {
    // Processes payment for the current user's cart stored in UserService.Cart
    // Returns true on successful payment, otherwise false.
    public static boolean paymentInterface() {
        LL cart = UserService.Cart;
        if (cart == null || cart.head == null) {
            System.out.println("Cart is empty. Nothing to pay for.");
            return false;
        }
        // 1) Calculate subtotal and display summary
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
        System.out.printf("Subtotal: %.2f%n", subtotal);
        // 2) Taxes (adjust as needed)
        final double taxRate = 0.05; // 5%
        double tax = round2(subtotal * taxRate);
        double total = round2(subtotal + tax);

        System.out.printf("Tax (%.0f%%): %.2f%n", taxRate * 100, tax);
        System.out.printf("Total Due: %.2f%n", total);

        // IMPORTANT: persist the total so PaymentDAO saves the correct amount
        if (Payment.payment != null) {
            Payment.payment.amount = total;
        }

        // 3) Select a payment method
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
                Payment.payment.paymentType = "cash";
                break;
            case "2":
                success = handleCard(total);
                Payment.payment.paymentType = "card";
                break;
            case "3":
                success = handleUpi(total);
                Payment.payment.paymentType = "upi";
                break;
            default:
                Payment.payment = null;
                System.out.println("Invalid choice. Payment cancelled.");
                return false;
        }

        // 3.5) Demo: Confirming order with the restaurant (like Zomato)
        if (success) {
            success = simulateRestaurantConfirmation();
            Payment.payment.paymentStatus = (success ? "success" : "failed");
        }

        // 4) On success: print receipt, clear cart, and reset order state
        if (success) {
            printReceipt(cart, subtotal, tax, total);
            cart.clearList();
            UserService.isEmpty = true; // allow selecting a restaurant again for the next order
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("\nException :- "+e);
            }
            try {
                PaymentDAO.savePaymentDetails(success);
            }
            catch (Exception e) {
                throw new RuntimeException("An error occurred in saving payment details", e);
            }
            System.out.println("Order placed successfully.");
        }

        return success;
    }

    private static boolean handleCash(double total) {
        System.out.print("Enter cash received: ");
        String in = UserService.scanner.next().trim();
        try {
            double received = Double.parseDouble(in);
            if (received < total) {
                System.out.printf("Insufficient amount. Need %.2f more.%n", round2(total - received));
                return false;
            }
            double change = round2(received - total);
            System.out.printf("Payment successful. Change: %.2f%n", change);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid amount. Payment failed.");
            return false;
        }
    }

    private static boolean handleCard(double total) {
        System.out.print("Enter card number (16 digits): ");
        String card = UserService.scanner.next().replaceAll("\\s+", "");
        if (card.length() < 12) {
            System.out.println("Invalid card number.");
            return false;
        }
        System.out.print("Enter expiry (MM/YY): ");
        String expiry = UserService.scanner.next().trim();
        System.out.print("Enter CVV: ");
        String cvv = UserService.scanner.next().trim();

        if (expiry.isEmpty() || cvv.length() < 3) {
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

    // Demo: Simulate restaurant confirmation with a simple progress animation and random outcome.
    private static boolean simulateRestaurantConfirmation() {
        System.out.print("\nConfirming order with restaurant ");
        // Simple “dot” animation
        for (int i = 0; i < 6; i++) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            System.out.print(".");
        }
        System.out.println();

        // 90% accept rate for demo
        boolean accepted = Math.random() < 0.99;
        if (accepted) {
            System.out.println("\nRestaurant confirmed your order! Preparing your food.");
            return true;
        } else {
            System.out.println("\nRestaurant declined the order due to high load. Payment will be auto-refunded (demo).");
            return false;
        }
    }

    private static void printReceipt(LL cart, double subtotal, double tax, double total) {
        System.out.println("\n====== RECEIPT ======");
        LL.Node current = cart.head;
        while (current != null) {
            double price = current.data.getPrice();
            int qty = current.quantity;
            double lineTotal = qty * price;
            System.out.printf("%s x%d @ %.2f = %.2f%n",
                    current.data.getName(), qty, price, lineTotal);
            current = current.next;
        }
        System.out.println("---------------------");
        System.out.printf("Subtotal: %.2f%n", subtotal);
        System.out.printf("Tax: %.2f%n", tax);
        System.out.println("---------------------");
        System.out.printf("TOTAL: %.2f%n", total);
        System.out.println("Thank you for your purchase!");
        System.out.println("=====================\n");
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
