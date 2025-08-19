package Services;

import Constants.AppConstants;
import Dao.OrderDAO;
import Dao.PaymentDAO;
import Ds.LL;
import Models.Payment;

public class PaymentService {
    public static String choice;
    public static boolean paymentInterface() {
        LL cart = UserService.Cart;
        if (cart == null || cart.head == null) {
            System.out.println("Cart is empty. Nothing to pay for.");
            return false;
        }

        // Calculate subtotal and display summary
        double subtotal = 0.0;

        // Build a nice table: Item | Qty | Price | Line Total
        String[] headers = { "Item", "Qty", "Price", "Line Total" };
        int[] widths = {
                Math.max(4, headers[0].length()),
                Math.max(3, headers[1].length()),
                Math.max(5, headers[2].length()),
                Math.max(10, headers[3].length())
        };

        java.util.List<String[]> rows = new java.util.ArrayList<>();
        LL.Node current = cart.head;
        while (current != null) {
            String item = current.data.getName();
            int qty = current.quantity;
            double price = current.data.getPrice();
            double lineTotal = qty * price;

            String sItem = item;
            String sQty = String.valueOf(qty);
            String sPrice = String.format(java.util.Locale.US, "%.2f", price);
            String sLine = String.format(java.util.Locale.US, "%.2f", lineTotal);

            widths[0] = Math.max(widths[0], sItem.length());
            widths[1] = Math.max(widths[1], sQty.length());
            widths[2] = Math.max(widths[2], sPrice.length());
            widths[3] = Math.max(widths[3], sLine.length());

            rows.add(new String[] { sItem, sQty, sPrice, sLine });
            subtotal += lineTotal;

            current = current.next;
        }

        // Build line separator
        StringBuilder sep = new StringBuilder();
        sep.append('+');
        for (int w : widths) {
            sep.append("-".repeat(w + 2)).append('+');
        }

        // Build row formats (left for text, right for numbers)
        String rowFmt = "| %-" + widths[0] + "s | %" + widths[1] + "s | %" + widths[2] + "s | %" + widths[3] + "s |%n";
        System.out.println("\n" + "-".repeat(Math.max(18, sep.length())));
        System.out.println("Order Summary");
        System.out.println("-".repeat(Math.max(18, sep.length())));
        System.out.println(sep.toString());
        System.out.printf(rowFmt,
                headers[0], headers[1], headers[2], headers[3]);
        System.out.println(sep.toString());
        for (String[] r : rows) {
            System.out.printf(rowFmt, r[0], r[1], r[2], r[3]);
        }
        System.out.println(sep.toString());

        double taxRate = 0.05;
        double tax = round2(subtotal * taxRate);
        double total = round2(subtotal + tax);
        OrderDAO.total = total;

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

        boolean success = false;
        boolean payment_selection = true;
        while (payment_selection) {
            System.out.print("\nEnter choice (1-3) or enter 'exit' to go back : ");
            choice = UserService.scanner.next().trim();
            switch (choice) {
                case "1":
                    success = handleCash(total);
                    if (Payment.payment != null) Payment.payment.paymentType = AppConstants.PAYMENT_CASH;payment_selection = false;
                    break;
                case "2":
                    success = handleCard(total);
                    if (Payment.payment != null) Payment.payment.paymentType = AppConstants.PAYMENT_CARD;payment_selection = false;
                    break;
                case "3":
                    success = handleUpi(total);
                    if (Payment.payment != null) Payment.payment.paymentType = AppConstants.PAYMENT_UPI;payment_selection = false;
                    break;
                default:
                    System.out.println("Invalid choice. Payment Please try again.");
                    break;
            }
        }

        // Simulate restaurant confirmation
        if (success) {
            success = simulateRestaurantConfirmation();
            if (Payment.payment != null) Payment.payment.paymentStatus = (success ? "success" : "failed");
        }
        else {
            System.out.println("Payment failed. Please try again.");
            paymentInterface();
        }

        // On success: print receipt, clear cart, save payment
        if (success) {
            printReceipt(cart, subtotal, tax, total);
            UserService.isEmpty = true;
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
        // Just mark as cash, but don’t ask yet
        System.out.println("💵 Cash selected. Please pay when order is finalized.");
        return true;
    }

    private static boolean handleCard(double total) {
        String card, expiry, cvv;

        // --- CARD NUMBER ---
        while (true) {
            System.out.print("Enter card number (16 digits): ");
            card = UserService.scanner.next().replaceAll("\\s+", ""); // remove spaces

            if (card.matches("\\d{16}") && isValidCard(card)) {
                break; // valid card number
            } else {
                System.out.println("❌ Invalid card number. Please try again.");
            }
        }

        // --- EXPIRY DATE ---
        while (true) {
            System.out.print("Enter expiry (MM/YY): ");
            expiry = UserService.scanner.next().trim();

            if (expiry.matches("^(0[1-9]|1[0-2])/\\d{2}$") && !isExpired(expiry)) {
                break; // valid expiry
            } else {
                System.out.println("❌ Invalid expiry date. Format must be MM/YY and not expired.");
            }
        }

        // --- CVV ---
        while (true) {
            System.out.print("Enter CVV: ");
            cvv = UserService.scanner.next().trim();

            if (cvv.matches("\\d{3,4}")) {
                break; // valid CVV
            } else {
                System.out.println("❌ Invalid CVV. Must be 3 or 4 digits.");
            }
        }

        // --- SUCCESS ---
        System.out.printf("✅ Card charged ₹%.2f successfully. **** **** **** %s%n",
                total, card.substring(card.length() - 4));
        return true;
    }

    // Luhn algorithm
    private static boolean isValidCard(String card) {
        int sum = 0;
        boolean alternate = false;
        for (int i = card.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(card.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    // Expiry check
    private static boolean isExpired(String expiry) {
        try {
            String[] parts = expiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]); // assumes 20YY

            java.time.YearMonth expDate = java.time.YearMonth.of(year, month);
            java.time.YearMonth now = java.time.YearMonth.now();

            return expDate.isBefore(now);
        } catch (Exception e) {
            return true;
        }
    }

    public static void handcash(double total) { handcash(total);}

    private static boolean handleUpi(double total) {
        System.out.print("Enter UPI ID (e.g., user@bank): ");
        String upi = UserService.scanner.next().trim();
        // Strong regex for UPI validation
        String upiRegex = "^[a-zA-Z0-9._-]{2,256}@[a-zA-Z]{2,64}$";
        if (!upi.matches(upiRegex)) {
            System.out.println("❌ Invalid UPI ID. Format must be like: username@bank");
            return false;
        }
        System.out.printf("✅ UPI collect request for ₹%.2f sent to %s...%n", total, upi);
        System.out.println("💰 Payment successful.");
        return true;
    }


    // ---------------- Helper Methods ----------------

    private static boolean simulateRestaurantConfirmation() {
        System.out.print("\nConfirming order with restaurant ");
        for (int i = 0; i < 6; i++) {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            System.out.print(".");
        }
        System.out.println();
        return true;
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
