package Services;

import Constants.AppConstants;
import Dao.OrderDAO;
import Ds.LL;
import Models.Payment;

public class PaymentService {

    public static String choice; // "1" cash, "2" card, "3" UPI

    public static boolean paymentInterface() {
        LL cart = UserService.Cart;
        if (cart == null || cart.head == null) {
            System.out.println("Cart is empty. Nothing to pay.");
            return false;
        }

        // ---- totals ----
        double subtotal = 0.0;
        LL.Node current = cart.head;
        while (current != null) {
            subtotal += current.quantity * current.data.getPrice();
            current = current.next;
        }
        double taxRate = 0.05;
        double tax   = round2(subtotal * taxRate);
        double total = round2(subtotal + tax);
        OrderDAO.total = total;              // used later by placeOrder cash settlement
        if (Payment.payment != null) {
            Payment.payment.amount = total;
        }

        printReceipt(cart, subtotal, tax, total);

        // ---- choose method ----
        System.out.print("\n\t\t\t" + AppConstants.BG_ANSI_BLACK +
                "============== Payment Methods ==============" + AppConstants.ANSI_RESET + "\n");
        System.out.println("1) Cash");
        System.out.println("2) Card");
        System.out.println("3) UPI");

        while (true) {
            System.out.print("Enter choice (1-3) or 'exit' to cancel: ");
            choice = UserService.scanner.next().trim();
            switch (choice) {
                case "1" -> {
                    if (Payment.payment != null) Payment.payment.paymentType = AppConstants.PAYMENT_CASH;
                    System.out.println("\n💵 Cash selected. Pay on delivery.");
                    // We collect the cash AFTER order is placed (in placeOrder)
                    return true;
                }
                case "2" -> {
                    if (handleCard(total)) {
                        if (Payment.payment != null) Payment.payment.paymentType = AppConstants.PAYMENT_CARD;
                        return true;
                    }
                    return false;
                }
                case "3" -> {
                    if (handleUpi(total)) {
                        if (Payment.payment != null) Payment.payment.paymentType = AppConstants.PAYMENT_UPI;
                        return true;
                    }
                    return false;
                }
                case "exit" -> { return false; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---------------- Payment Handlers ----------------

    private static boolean handleCard(double total) {
        String card, expiry, cvv;

        // CARD NUMBER
        while (true) {
            System.out.print("\nEnter card number (16 digits): ");
            card = UserService.scanner.next().replaceAll("\\s+", "");
            if (card.matches("\\d{16}") && isValidCard(card)) {
                break;
            } else {
                System.out.println("\n❌ Invalid card number. Try again or enter 'b' to go back.");
                String tok = UserService.scanner.next().trim();
                if (tok.equalsIgnoreCase("b")) return false;
            }
        }

        // EXPIRY
        while (true) {
            System.out.print("Enter expiry (MM/YY): ");
            expiry = UserService.scanner.next().trim();
            if (expiry.matches("^(0[1-9]|1[0-2])/\\d{2}$") && !isExpired(expiry)) break;
            System.out.println("❌ Invalid expiry date. Format must be MM/YY and not expired.");
        }

        // CVV
        while (true) {
            System.out.print("Enter CVV: ");
            cvv = UserService.scanner.next().trim();
            if (cvv.matches("\\d{3,4}")) break;
            System.out.println("❌ Invalid CVV. Must be 3 or 4 digits.");
        }

        System.out.printf("\n✅ Card charged ₹%.2f successfully. **** **** **** %s%n",
                total, card.substring(card.length() - 4));
        if (Payment.payment != null) Payment.payment.paymentStatus = "SUCCESS";
        return true;
    }

    private static boolean isValidCard(String card) {
        int sum = 0; boolean alt = false;
        for (int i = card.length() - 1; i >= 0; i--) {
            int n = card.charAt(i) - '0';
            if (alt) { n *= 2; if (n > 9) n -= 9; }
            sum += n; alt = !alt;
        }
        return sum % 10 == 0;
    }

    private static boolean isExpired(String expiry) {
        try {
            String[] p = expiry.split("/");
            int m = Integer.parseInt(p[0]);
            int y = 2000 + Integer.parseInt(p[1]);
            java.time.YearMonth exp = java.time.YearMonth.of(y, m);
            return exp.isBefore(java.time.YearMonth.now());
        } catch (Exception e) {
            return true;
        }
    }

    private static boolean handleUpi(double total) {
        System.out.print("\nEnter UPI ID (e.g., user@bank): ");
        String upi = UserService.scanner.next().trim();
        String upiRegex = "^[a-zA-Z0-9._-]{2,256}@[a-zA-Z]{2,64}$";
        if (!upi.matches(upiRegex)) {
            System.out.println("\n❌ Invalid UPI ID. Format must be like: username@bank");
            return false;
        }
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            return false;
        }
        System.out.printf("\n✅ UPI collect request for ₹%.2f sent to %s...%n", total, upi);
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            return false;
        }
        System.out.println("\n💰 Payment successful.");
        if (Payment.payment != null) Payment.payment.paymentStatus = "SUCCESS";
        return true;
    }

    // ---------------- Helpers ----------------

    private static void printReceipt(LL cart, double subtotal, double tax, double total) {
        System.out.println("\n=================== ORDER SUMMARY ===================");
        String fmt = "%-24s %5s %10s %12s%n";
        System.out.printf(fmt, "Item", "Qty", "Price", "Line Total");
        System.out.println("-----------------------------------------------");
        LL.Node n = cart.head;
        while (n != null) {
            double price = n.data.getPrice();
            int qty = n.quantity;
            System.out.printf(fmt, n.data.getName(), qty,
                    String.format(java.util.Locale.US,"%.2f",price),
                    String.format(java.util.Locale.US,"%.2f",qty*price));
            n = n.next;
        }
        System.out.println("-----------------------------------------------");
        System.out.printf("Subtotal: %.2f%n", subtotal);
        System.out.printf("Tax (5%%): %.2f%n", tax);
        System.out.printf("Total Due: %.2f%n", total);
        System.out.println("============================\n");
    }

    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
}