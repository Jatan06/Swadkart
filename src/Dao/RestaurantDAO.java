package Dao;
import Admin.Admin;
import Constants.*;
import java.sql.*;
import java.util.*;

public class RestaurantDAO {

    public static void browseRestaurants() {
        RestaurantDAO dao = new RestaurantDAO();
        while (true) {
            System.out.println("\n==== Browse Restaurants ====");
            System.out.println("1. By Rating (High → Low)");
            System.out.println("2. By ID (Ascending)");
            System.out.println("3. By Name (A → Z)");
            System.out.println("4. By Cuisine (A → Z)");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

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
                System.out.println("\nError: " + e.getMessage());
            }
        }

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
        System.out.print("\nEnter Restaurant ID: ");
        String id = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Restaurant Name: ");
        String name = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Cuisine Type: ");
        String cuisine = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Phone Number: ");
        String phone = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Address: ");
        String address = AppConstants.s.nextLine().trim();
        System.out.print("\nEnter Rating: ");
        double rating = AppConstants.s.nextDouble();
        CallableStatement br = AppConstants.connection.prepareCall("{call addRestaurant (?,?,?,?,?,?)}");
        br.setString(1, id);
        br.setString(2, name);
        br.setString(3, cuisine);
        br.setString(4, phone);
        br.setString(5, address);
        br.setDouble(6, rating);
        if (br.executeUpdate() > 0) {
            System.out.println("\nRestaurant added successfully.");
        }
        else {
            System.out.println("\nError adding restaurant.");
        }
    }

    public static void deleteRestaurant() throws Exception {
        AppConstants.connection.setAutoCommit(false);
        AppConstants.s.nextLine();
        System.out.print("\nEnter Restaurant ID: ");
        String id = AppConstants.s.nextLine().trim();
        if(!RestaurantDAO.checkRestaurantId(id)) {
            System.out.print("\nInvalid Restaurant Id. Try again. or enter 'b' to go back :- ");
            if(AppConstants.s.next().equalsIgnoreCase("b")) {
                return;
            }
            else {
                deleteRestaurant();
            }
        }
        CallableStatement br = AppConstants.connection.prepareCall("delete from restaurants where id = ?");
        br.setString(1, id);
        if (br.executeUpdate() > 0) {
            System.out.print("\nAre you sure you want to delete this restaurant? (y/n): ");
            if(AppConstants.s.next().trim().equalsIgnoreCase("y")) {
                System.out.print("\nEnter Password");
                if(AppConstants.s.next().equals(Admin.getAdminPassword())) {
                    AppConstants.connection.commit();
                    System.out.println("\nRestaurant deleted successfully.");
                }
                else {
                    AppConstants.connection.rollback();
                    System.out.println("\nIncorrect password. Restaurant not deleted!");
                }
            }
            else {
                System.out.println("\nRestaurant not deleted.");
            }
        }
        else {
            System.out.println("Error deleting restaurant.");
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
            throw new RuntimeException("Error while checking restaurant ID", e);
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