package Utils;

public class Validators {

    // Validate user ID format (u-0001 or a-0001)
    public static boolean validateId(String id) {
        if (id == null || id.length() != 6) {
            return false;
        }
        return (id.startsWith("u-") || id.startsWith("a-")) &&
                isNumeric(id.substring(6));
    }

    // Validate password (minimum 6 characters, alphanumeric)
    public static boolean validatePassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return isAlphanumeric(password);
    }

    // Validate mobile number (10 digits, Indian format)
    public static boolean validateMobileNumber(String mobile) {
        if (mobile == null || mobile.length() != 10) {
            return false;
        }
        char firstDigit = mobile.charAt(0);
        return (firstDigit >= '6' && firstDigit <= '9') && isNumeric(mobile);
    }

    // Validate email format
    public static boolean validateEmail(String email) {
        if (email == null || email.length() < 5) {
            return false;
        }
        return email.contains("@") && email.contains(".") &&
                email.indexOf("@") < email.lastIndexOf(".");
    }

    // Validate integer input
    public static boolean validateInteger(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validate double input
    public static boolean validateDouble(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validate string (not null, not empty, minimum length)
    public static boolean validateString(String input, int minLength) {
        return input != null && input.trim().length() >= minLength;
    }

    // Validate string (not null, not empty)
    public static boolean validateString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    // Validate name (letters and spaces only)
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        for (char c : trimmed.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    // Validate restaurant name (letters, numbers, spaces, hyphens)
    public static boolean validateRestaurantName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        for (char c : trimmed.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != ' ' && c != '-') {
                return false;
            }
        }
        return true;
    }

    // Validate dish name (letters, numbers, spaces, hyphens)
    public static boolean validateDishName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        for (char c : trimmed.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != ' ' && c != '-') {
                return false;
            }
        }
        return true;
    }

    // Validate price (positive number)
    public static boolean validatePrice(String price) {
        if (!validateDouble(price)) {
            return false;
        }
        double value = Double.parseDouble(price);
        return value > 0;
    }

    // Validate rating (1-5)
    public static boolean validateRating(String rating) {
        if (!validateInteger(rating)) {
            return false;
        }
        int value = Integer.parseInt(rating);
        return value >= 1 && value <= 5;
    }

    // Validate quantity (positive integer)
    public static boolean validateQuantity(String quantity) {
        if (!validateInteger(quantity)) {
            return false;
        }
        int value = Integer.parseInt(quantity);
        return value > 0;
    }

    // Helper method to check if string is numeric
    private static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Helper method to check if string is alphanumeric
    private static boolean isAlphanumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
