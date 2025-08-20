package Dao;
import Admin.Admin;
import Constants.*;
import Menus.CustomerMenu;
import Utils.Validators;

import java.sql.*;
import java.util.*;

public class RestaurantDAO {
    public static void updateRestaurant() {
        boolean up = true;
        while (up) {
            try {
                updateRestaurantMenu();
                switch (AppConstants.s.next().trim()) {
                    case "1" -> {
                        AppConstants.s.nextLine();
                        System.out.print("\nEnter Restaurant ID :- ");
                        String id = AppConstants.s.nextLine().trim();
                        while (true) {
                            if (id.length() == 1) {
                                id = "r-000".concat(id);
                            } else if (id.length() == 2) {
                                id = "r-00".concat(id);
                            } else if (id.length() == 3) {
                                id = "r-0".concat(id);
                            } else if (id.length() == 4) {
                                id = "r-".concat(id);
                            } else {
                                while (!validateId(id)) {
                                    System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                    id = AppConstants.s.nextLine().trim();
                                    if(id.equalsIgnoreCase("b")) return;
                                }
                                changeRestName(id);
                                return;
                            }
                            while (!validateId(id)) {
                                System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                id = AppConstants.s.nextLine().trim();
                                if(id.equalsIgnoreCase("b")) return;
                            }
                            if(validateId(id)) {
                                changeRestRating(id);
                                return;
                            }
                        }
                    }
                    case "2" -> {
                        AppConstants.s.nextLine();
                        System.out.print("\nEnter Restaurant ID :- ");
                        String id = AppConstants.s.nextLine().trim();
                        while (true) {
                            if (id.length() == 1) {
                                id = "r-000".concat(id);
                            } else if (id.length() == 2) {
                                id = "r-00".concat(id);
                            } else if (id.length() == 3) {
                                id = "r-0".concat(id);
                            } else if (id.length() == 4) {
                                id = "r-".concat(id);
                            } else {
                                while (!validateId(id)) {
                                    System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                    id = AppConstants.s.nextLine().trim();
                                    if(id.equalsIgnoreCase("b")) return;
                                }
                                changeRestCuisine(id);
                                return;
                            }
                            while (!validateId(id)) {
                                System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                id = AppConstants.s.nextLine().trim();
                                if(id.equalsIgnoreCase("b")) return;
                            }
                            if(validateId(id)) {
                                changeRestRating(id);
                                return;
                            }
                        }
                    }
                    case "3" -> {
                        AppConstants.s.nextLine();
                        System.out.print("\nEnter Restaurant ID :- ");
                        String id = AppConstants.s.nextLine().trim();
                        while (true) {
                            if (id.length() == 1) {
                                id = "r-000".concat(id);
                            } else if (id.length() == 2) {
                                id = "r-00".concat(id);
                            } else if (id.length() == 3) {
                                id = "r-0".concat(id);
                            } else if (id.length() == 4) {
                                id = "r-".concat(id);
                            } else {
                                while (!validateId(id)) {
                                    System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                    id = AppConstants.s.nextLine().trim();
                                    if(id.equalsIgnoreCase("b")) return;
                                }
                                changeRestPhone(id);
                                return;
                            }
                            while (!validateId(id)) {
                                System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                id = AppConstants.s.nextLine().trim();
                                if(id.equalsIgnoreCase("b")) return;
                            }
                            if(validateId(id)) {
                                changeRestRating(id);
                                return;
                            }
                        }
                    }
                    case "4" -> {
                        AppConstants.s.nextLine();
                        System.out.print("\nEnter Restaurant ID :- ");
                        String id = AppConstants.s.nextLine().trim();
                        while (true) {
                            if (id.length() == 1) {
                                id = "r-000".concat(id);
                            } else if (id.length() == 2) {
                                id = "r-00".concat(id);
                            } else if (id.length() == 3) {
                                id = "r-0".concat(id);
                            } else if (id.length() == 4) {
                                id = "r-".concat(id);
                            } else {
                                while (!validateId(id)) {
                                    System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                    id = AppConstants.s.nextLine().trim();
                                    if(id.equalsIgnoreCase("b")) return;
                                }
                                changeRestAddress(id);
                                return;
                            }
                            while (!validateId(id)) {
                                System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                id = AppConstants.s.nextLine().trim();
                                if(id.equalsIgnoreCase("b")) return;
                            }
                            if(validateId(id)) {
                                changeRestRating(id);
                                return;
                            }
                        }
                    }
                    case "5" -> {
                        AppConstants.s.nextLine();
                        System.out.print("\nEnter Restaurant ID :- ");
                        String id = AppConstants.s.nextLine().trim();
                        while (true) {
                            if (id.length() == 1) {
                                id = "r-000".concat(id);
                            } else if (id.length() == 2) {
                                id = "r-00".concat(id);
                            } else if (id.length() == 3) {
                                id = "r-0".concat(id);
                            } else if (id.length() == 4) {
                                id = "r-".concat(id);
                            } else {
                                while (!validateId(id)) {
                                    System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                    id = AppConstants.s.nextLine().trim();
                                    if(id.equalsIgnoreCase("b")) return;
                                }
                                changeRestRating(id);
                                return;
                            }
                            while (!validateId(id)) {
                                System.out.print(AppConstants.TEXT_ANSI_RED+"Invalid Restaurant ID. Please try again."+AppConstants.ANSI_RESET+" or enter 'b' :- ");
                                id = AppConstants.s.nextLine().trim();
                                if(id.equalsIgnoreCase("b")) return;
                            }
                            if(validateId(id)) {
                                changeRestRating(id);
                                return;
                            }
                        }
                    }
                    case "6" -> up = false;
                    default -> System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
                }
            }
            catch (Exception e) {
                System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
                AppConstants.s.nextLine();
            }
        }
    }

    private static boolean validateId(String id) {
        String sql = "SELECT id FROM restaurants WHERE id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Exception arise.");
            return false;
        }
    }

    private static void changeRestName(String id) {
        System.out.print("\nEnter new Restaurant Name :- ");
        String new_name = AppConstants.s.nextLine().trim();
        String upName = "UPDATE restaurants SET name = ? WHERE id = ?";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(upName)) {
            ps.setString(1, new_name);
            ps.setString(2, id);
            if (ps.executeUpdate() > 0) {
                System.out.println(AppConstants.TEXT_ANSI_GREEN + "Restaurant name updated successfully!" + AppConstants.ANSI_RESET);
            }
        } catch (SQLException e) {
            System.out.println(AppConstants.TEXT_ANSI_RED + "Exception occurred: " + e.getMessage() + AppConstants.ANSI_RESET);
        }
    }

    private static void changeRestCuisine(String id) {
        System.out.print("\nEnter new Cuisine :- ");
        String newCuisine = AppConstants.s.nextLine().trim();
        String sql = "UPDATE restaurants SET cuisine = ? WHERE id = ?";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, newCuisine);
            ps.setString(2, id);

            if (ps.executeUpdate() > 0) {
                System.out.println(AppConstants.TEXT_ANSI_GREEN +
                        "Cuisine updated successfully!" +
                        AppConstants.ANSI_RESET);
            }
        } catch (SQLException e) {
            System.out.println(AppConstants.TEXT_ANSI_RED +
                    "Exception occurred: " + e.getMessage() +
                    AppConstants.ANSI_RESET);
        }
    }

    private static void changeRestPhone(String id) {
        System.out.print("\nEnter new Phone Number :- ");
        String newPhone = AppConstants.s.nextLine().trim();
        while (!Validators.validateMobileNumber(newPhone)) {
            System.out.println("Enter a valid phone number! or enter 'b' to go back.");
            newPhone = AppConstants.s.nextLine().trim();
            if (newPhone.equalsIgnoreCase("b")) {
                return;
            }
        }
        String sql = "UPDATE restaurants SET phone_no = ? WHERE id = ?";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, newPhone);
            ps.setString(2, id);

            if (ps.executeUpdate() > 0) {
                System.out.println(AppConstants.TEXT_ANSI_GREEN +
                        "Phone number updated successfully!" +
                        AppConstants.ANSI_RESET);
            }
        } catch (SQLException e) {
            System.out.println(AppConstants.TEXT_ANSI_RED +
                    "Exception occurred: " + e.getMessage() +
                    AppConstants.ANSI_RESET);
        }
    }

    private static void changeRestAddress(String id) {
        System.out.print("\nEnter new Address :- ");
        String newAddress = AppConstants.s.nextLine().trim();
        String sql = "UPDATE restaurants SET address = ? WHERE id = ?";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, newAddress);
            ps.setString(2, id);

            if (ps.executeUpdate() > 0) {
                System.out.println(AppConstants.TEXT_ANSI_GREEN +
                        "Address updated successfully!" +
                        AppConstants.ANSI_RESET);
            }
        } catch (SQLException e) {
            System.out.println(AppConstants.TEXT_ANSI_RED +
                    "Exception occurred: " + e.getMessage() +
                    AppConstants.ANSI_RESET);
        }
    }

    private static void changeRestRating(String id) {
        System.out.print("\nEnter new Rating (1.0 - 5.0) :- ");
        String newRating = AppConstants.s.nextLine().trim();
        String sql = "UPDATE restaurants SET rating = ? WHERE id = ?";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, new java.math.BigDecimal(newRating));
            ps.setString(2, id);

            if (ps.executeUpdate() > 0) {
                System.out.println(AppConstants.TEXT_ANSI_GREEN +
                        "Rating updated successfully!" +
                        AppConstants.ANSI_RESET);
            }
        } catch (SQLException e) {
            System.out.println(AppConstants.TEXT_ANSI_RED +
                    "Exception occurred: " + e.getMessage() +
                    AppConstants.ANSI_RESET);
        }
    }

    public static void restaurantStats() {
        // Pending
    }

    private static void updateRestaurantMenu() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-------------------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t**********************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Update Restaurants +\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t**********************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t1. Name\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t2. Cuisine\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t3. Phone No\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t4. Address\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t5. Rating\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t6. Back\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-------------------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }

    public static void getRestaurantIdAndName() {
        String sql = "SELECT id AS restaurant_id, name FROM restaurants ORDER BY CAST(id AS UNSIGNED) ASC, name ASC";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("RESTAURANT IDs AND NAMES", rs);
        } catch (SQLException e) {
            System.out.println("\nError fetching restaurant IDs and names: " + e.getMessage());
        }
    }

    public static void browseRestaurants() {
        RestaurantDAO dao = new RestaurantDAO();
        while (true) {
            displayRestMenu();
            String choice = AppConstants.s.next().trim();
            try {
                switch (choice) {
                    case "1" -> dao.browseRestaurantsByRating();
                    case "2" -> dao.browseRestaurantsById();
                    case "3" -> dao.browseRestaurantsByName();
                    case "4" -> dao.browseRestaurantsByCuisine();
                    case "5" -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Please enter numbers only.");
            }
        }
    }

    private static void displayRestMenu() {
        System.out.println("\n\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-------------------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t********************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Browse Restaurants +\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t********************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t1. By Rating (High → Low)\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t2. By ID (Ascending)\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t3. By Name (A → Z)\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t4. By Cuisine (A → Z)\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t5. Back\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-------------------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }

    private void browseRestaurantsByRating() throws Exception {
        String sql =
                "SELECT " +
                "  id AS restaurant_id, " +
                "  name, " +
                "  cuisine, " +
                "  phone_no AS phone, " +
                "  address, " +
                "  rating, " +
                "  CASE " +
                "    WHEN rating >= 4.5 THEN '⭐ Excellent' " +
                "    WHEN rating >= 4.0 THEN '👍 Good' " +
                "    WHEN rating >= 3.5 THEN '👌 Average' " +
                "    ELSE '👎 Below Avg' " +
                "  END AS status " +
                "FROM restaurants " +
                "ORDER BY rating DESC, name ASC";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("RESTAURANTS", rs);
        }
    }

    private void browseRestaurantsById() {
        String sql =
                "SELECT " +
                        "  id AS restaurant_id, " +
                        "  name, " +
                        "  cuisine, " +
                        "  phone_no AS phone, " +
                        "  address, " +
                        "  rating, " +
                        "  CASE " +
                        "    WHEN rating >= 4.5 THEN '⭐ Excellent' " +
                        "    WHEN rating >= 4.0 THEN '👍 Good' " +
                        "    WHEN rating >= 3.5 THEN '👌 Average' " +
                        "    ELSE '👎 Below Avg' " +
                        "  END AS status " +
                        "FROM restaurants " +
                        "ORDER BY id ASC, name ASC";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("RESTAURANTS (BY ID)", rs);
        } catch (SQLException e) {
            System.out.println("\nError fetching restaurants: " + e.getMessage());
        }
    }

    private void browseRestaurantsByName() {
        String sql =
                "SELECT " +
                        "  id AS restaurant_id, " +
                        "  name, " +
                        "  cuisine, " +
                        "  phone_no AS phone, " +
                        "  address, " +
                        "  rating, " +
                        "  CASE " +
                        "    WHEN rating >= 4.5 THEN '⭐ Excellent' " +
                        "    WHEN rating >= 4.0 THEN '👍 Good' " +
                        "    WHEN rating >= 3.5 THEN '👌 Average' " +
                        "    ELSE '👎 Below Avg' " +
                        "  END AS status " +
                        "FROM restaurants " +
                        "ORDER BY name ASC, id ASC";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("RESTAURANTS (BY NAME)", rs);
        } catch (SQLException e) {
            System.out.println("\nError fetching restaurants: " + e.getMessage());
        }
    }

    private void browseRestaurantsByCuisine() {
        String sql =
                "SELECT " +
                        "  id AS restaurant_id, " +
                        "  name, " +
                        "  cuisine, " +
                        "  phone_no AS phone, " +
                        "  address, " +
                        "  rating, " +
                        "  CASE " +
                        "    WHEN rating >= 4.5 THEN '⭐ Excellent' " +
                        "    WHEN rating >= 4.0 THEN '👍 Good' " +
                        "    WHEN rating >= 3.5 THEN '👌 Average' " +
                        "    ELSE '👎 Below Avg' " +
                        "  END AS status " +
                        "FROM restaurants " +
                        "ORDER BY cuisine ASC, name ASC";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("RESTAURANTS (BY CUISINE)", rs);
        } catch (SQLException e) {
            System.out.println("\nError fetching restaurants: " + e.getMessage());
        }

    }

    private static void printResultSetAsTable(String title, ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();

        List<String[]> rows = new ArrayList<>();
        int[] widths = new int[colCount];
        String[] headers = new String[colCount];
        int[] types = new int[colCount];

        for (int i = 1; i <= colCount; i++) {
            headers[i - 1] = md.getColumnLabel(i);
            types[i - 1] = md.getColumnType(i);
            widths[i - 1] = Math.max(3, headers[i - 1].length());
        }

        while (rs.next()) {
            String[] row = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                String colName = headers[i - 1];
                int sqlType = types[i - 1];

                Object val = rs.getObject(i);
                String txt = formatValue(colName, sqlType, val);
                row[i - 1] = txt;
                widths[i - 1] = Math.max(widths[i - 1], txt.length());
            }
            rows.add(row);
        }

        String[] colFmts = new String[colCount];
        for (int i = 0; i < colCount; i++) {
            boolean rightAlign = isNumeric(types[i]) || looksLikeMoney(headers[i]);
            colFmts[i] = rightAlign ? "%" + widths[i] + "s" : "%-" + widths[i] + "s";
        }

        String sep = buildSeparator(widths);

        StringBuilder headerFmt = new StringBuilder("| ");
        for (int i = 0; i < colCount; i++) {
            headerFmt.append(String.format("%%-%ds", widths[i])).append(" | ");
        }
        headerFmt.append("%n");

        StringBuilder rowFmt = new StringBuilder("| ");
        for (int i = 0; i < colCount; i++) {
            rowFmt.append(colFmts[i]).append(" | ");
        }
        rowFmt.append("%n");

        System.out.println();
        System.out.println(centerTitleInSeparator(title, sep));
        System.out.println(sep);
        System.out.printf(headerFmt.toString(), (Object[]) headers);
        System.out.println(sep);

        if (rows.isEmpty()) {
            System.out.printf("| %s |%n", padRight("No records found.", sep.length() - 4));
            System.out.println(sep);
            return;
        }

        for (String[] row : rows) {
            System.out.printf(rowFmt.toString(), (Object[]) row);
        }
        System.out.println(sep);
    }

    private static String formatValue(String colName, int sqlType, Object val) {
        if (val == null) return "-";

        if (looksLikeMoney(colName) && val instanceof Number) {
            return String.format(java.util.Locale.US, "%,.2f", ((Number) val).doubleValue());
        }

        if (isNumeric(sqlType)) {
            if (val instanceof Double || val instanceof Float) {
                return String.format(java.util.Locale.US, "%.1f", ((Number) val).doubleValue());
            }
            return val.toString();
        }

        if (val instanceof java.sql.Timestamp || val instanceof java.sql.Date || val instanceof java.sql.Time) {
            return val.toString();
        }

        String s = val.toString();
        return (s == null || s.isEmpty()) ? "-" : s;
    }

    private static boolean looksLikeMoney(String colName) {
        String n = colName.toLowerCase(Locale.ROOT);
        return n.contains("amount") || n.contains("price") || n.contains("total");
    }

    private static boolean isNumeric(int sqlType) {
        return switch (sqlType) {
            case Types.BIT, Types.BOOLEAN,
                    Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT,
                    Types.FLOAT, Types.REAL, Types.DOUBLE,
                    Types.NUMERIC, Types.DECIMAL -> true;
            default -> false;
        };
    }

    private static String buildSeparator(int[] widths) {
        int total = 1;
        for (int w : widths) total += 1 + w + 1 + 1;
        return repeat('-', total);
    }

    private static String centerTitleInSeparator(String title, String sep) {
        int len = sep.length();
        String t = " " + title + " ";
        if (t.length() >= len) return t.substring(0, len);
        int pad = (len - t.length()) / 2;
        return repeat('-', pad) + t + repeat('-', len - pad - t.length());
    }

    private static String repeat(char c, int count) {
        if (count <= 0) return "";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }

    private static String padRight(String s, int w) {
        if (s == null) s = "-";
        if (s.length() >= w) return s;
        StringBuilder sb = new StringBuilder(w);
        sb.append(s);
        for (int i = s.length(); i < w; i++) sb.append(' ');
        return sb.toString();
    }

    public static void addRestaurant() throws Exception {
        AppConstants.s.nextLine();
        String id = getRestaurantId();
        System.out.print("\nEnter Restaurant Name: ");
        String name = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Cuisine Type: ");
        String cuisine = AppConstants.s.nextLine().trim();

        System.out.print("\nEnter Phone Number: ");
        String phone = AppConstants.s.nextLine().trim();
        while (!Validators.validateMobileNumber(phone)) {
            System.out.print("\nInvalid Phone Number. Please enter a valid phone number. :- ");
            phone = AppConstants.s.nextLine().trim();
        }
        System.out.print("\nEnter Address: ");
        String address = AppConstants.s.nextLine().trim();
        while (!Validators.validateAddress(address)) {
            System.out.print("\nInvalid Address. Please enter a valid address. :- ");
            address = AppConstants.s.nextLine().trim();
        }
        System.out.print("\nEnter Rating: ");
        double rating = AppConstants.s.nextDouble();
        while (!Validators.validateRating(rating)) {
            System.out.println("\nInvalid Rating. Please enter a valid rating. (1.0-5.0):- ");
            rating = AppConstants.s.nextDouble();
        }
        CallableStatement br = AppConstants.connection.prepareCall("{call addRestaurant (?,?,?,?,?,?)}");
        br.setString(1, id);
        br.setString(2, name);
        br.setString(3, cuisine);
        br.setString(4, phone);
        br.setString(5, address);
        br.setDouble(6, rating);
        System.out.print("\nEnter number of dishes that you want to add for restaurant if not then enter '0' :- ");
        int dish_input = 0;
        while (true) {
            try {
                dish_input = AppConstants.s.nextInt();
                if(dish_input<=-1) {
                    System.out.println("Invalid input. Please enter a valid positive number :- ");
                } else if (dish_input>=0) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Enter numbers only.");
                AppConstants.s.nextLine();
            }
        }
        if(dish_input==0) {
            if (br.executeUpdate() > 0) {
                System.out.println("\nRestaurant added successfully.");
            }
            else {
                System.out.println("\nError adding restaurant.");
            }
            return;
        }
        else {
            if (br.executeUpdate() > 0) {
                System.out.println("\nRestaurant added successfully.");
            }
            else {
                System.out.println("\nError adding restaurant.");
                return;
            }
            for (int i = 0; i < dish_input; i++) {
                DishDAO.addDishes(name);
            }
            System.out.println("Would you like to add more dishes :- ");
            while (true) {
                if (AppConstants.s.next().equalsIgnoreCase("y")) {
                    System.out.println("Enter number of dishes that you want to add for restaurant :- ");
                    while (true) {
                        try {
                            dish_input = AppConstants.s.nextInt();
                            if(dish_input<=-1) {
                                System.out.println("Invalid input. Please enter a valid positive number :- ");
                            } else if (dish_input>=0) {
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("Enter numbers only.");
                            AppConstants.s.nextLine();
                        }
                    }
                    for (int i = 0; i < dish_input; i++) {
                        DishDAO.addDishes(name);
                    }
                }
                else {
                    return;
                }
            }
        }
    }

    private static String getRestaurantId() {
        String sql = "SELECT id FROM restaurants ORDER BY CAST(id AS UNSIGNED) DESC LIMIT 1";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            String lastId = null;
            if (rs.next()) {
                lastId = rs.getString("id");
            }

            if (lastId == null || lastId.isBlank()) {
                return "1";
            }

            // Try to increment the trailing numeric part while preserving prefix and zero padding
            String pattern = "(.*?)(\\d+)$";
            java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(lastId);
            if (m.find()) {
                String prefix = m.group(1);
                String numPart = m.group(2);
                int width = numPart.length();
                long num = Long.parseLong(numPart);
                String nextNum = String.format(java.util.Locale.ROOT, "%0" + width + "d", num + 1);
                return prefix + nextNum;
            }

            // Fallback: if the whole ID is numeric, increment it
            try {
                long n = Long.parseLong(lastId);
                return String.valueOf(n + 1);
            } catch (NumberFormatException ignore) {
                // If not numeric at all, append 1
                return lastId + "1";
            }

        } catch (SQLException e) {
            System.out.println("Error generating next restaurant id: " + e.getMessage());
            return "1";
        }

    }

    public static void deleteRestaurant() throws Exception {
        AppConstants.s.nextLine();
        AppConstants.connection.setAutoCommit(false);
        System.out.print("\nEnter Restaurant ID: ");
        String id = AppConstants.s.nextLine().trim();
        if(id.length()==1) {
            id = "r-000"+id;
        }
        else if(id.length()==2) {
            id = "r-00"+id;
        } else if (id.length()==3) {
            id = "r-0"+id;
        }
        else if (id.length()==4) {
            id = "r-"+id;
        }
        while (!Validators.validateRestaurantId(id)) {
            System.out.print("\nEnter valid id :- ");
            id = AppConstants.s.nextLine().trim();
        }
        if(!RestaurantDAO.checkRestaurantId(id)) {
            System.out.print("\nInvalid Restaurant Id. Try again. or enter 'b' to go back :- ");
            if(AppConstants.s.next().equalsIgnoreCase("b")) {
                return;
            }
            else {
                deleteRestaurant();
            }
        }
        System.out.print("\nAre you sure you want to delete the restaurant with ID '" + id + "'? (y/n): ");
        String confirmation = AppConstants.s.next().trim();
        if (confirmation.equalsIgnoreCase("y")) {
            System.out.print("\nEnter Password: ");
            if (AppConstants.s.next().equals(Admin.getAdminPassword())) {
                CallableStatement br = AppConstants.connection.prepareCall("delete from restaurants where id = ?");
                br.setString(1, id);
                if (br.executeUpdate() > 0) {
                    AppConstants.connection.commit();
                    System.out.println("\nRestaurant deleted successfully.");
                } else {
                    AppConstants.connection.rollback();
                    System.out.println("\nError: Restaurant with that ID was not found during deletion.");
                }
            } else {
                System.out.println("\nIncorrect password. Deletion cancelled.");
                AppConstants.connection.rollback();
            }
        } else {
            System.out.println("\nDeletion cancelled by user.");
            AppConstants.connection.rollback();
        }
    }

    static public String getRestaurantIdByName(String name) {
        String id = null;
        try {
            PreparedStatement ps = AppConstants.connection.prepareCall("select id from restaurants where name = ?;");
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                id = rs.getString("id");
            }
        } catch (SQLException e) {
            System.out.printf("Exception :- "+e);
        }
        return id;
    }

    static public String getRestaurantNameById(String id) {
        String name = null;
        try {
            PreparedStatement ps = AppConstants.connection.prepareCall("select name from restaurants where id = ?;");
            ps.setString(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.printf("Exception :- "+e);
        }
        return name;
    }

    public static boolean checkRestaurantId(String id) {
        boolean resfound = false;
        String sql = "{ call checkRestId(?, ?) }"; // procedure with IN + OUT
        try (CallableStatement cs = AppConstants.connection.prepareCall(sql)) {
            // Set input param
            cs.setString(1, id);

            // Register OUT param (MySQL BOOLEAN = TINYINT(1))
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            // Execute procedure
            cs.execute();

            // Get OUT param
            resfound = cs.getBoolean(2);

        } catch (SQLException e) {
            System.out.println("Exception at RestaurantDAO/checkRestaurantID at line 381");
        }
        return resfound;
    }

    private static String nz(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    private static String truncate(String s) {
        return truncate(s, 20);
    }

    private static String truncate(String s, int width, String fill) {
        if (s == null) return "-";
        if (s.length() <= width) return s;
        return (width >= 3) ? s.substring(0, width - 3) + fill : s.substring(0, width);
    }

    private static String truncate(String s, int width) {
        if (s == null) return "-";
        if (s.length() <= width) return s;
        return (width >= 3) ? s.substring(0, width - 3) + "..." : s.substring(0, width);
    }
}