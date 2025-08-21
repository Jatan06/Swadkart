package Dao;

import java.sql.*;
import java.util.*;
import Constants.*;
import Models.Dish;
import Utils.Validators;

public class DishDAO {
    public static void browseDishes() {
        DishDAO dao = new DishDAO();
        while (true) {
            displayDishMenu();
            String choice = AppConstants.s.next().trim();
            try {
                switch (choice) {
                    case "1" -> dao.browseDishesByRating();
                    case "2" -> dao.browseDishesByPriceAsc();
                    case "3" -> dao.browseDishesByPriceDesc();
                    case "4" -> dao.browseDishesByName();
                    case "5" -> dao.browseDishesByCuisineFilter();
                    case "6" -> dao.browseDishesByRestaurant();
                    case "7" -> { return; }
                    default -> System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
                }
            } catch (Exception e) {
                System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
            }
        }
    }

    private static void displayDishMenu() {
        System.out.println("\n\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"---------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t*****************\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t+ Browse Dishes +\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t*****************\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t1. By Rating (High → Low)\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t2. By Price (Low → High)\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t3. By Price (High -> Low)\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t4. By Name (A → Z)\t\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t5. By Cuisine\t\t\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t6. By Restaurant (A → Z)\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t7. Back\t\t\t\t\t\t\t\t\t|"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"---------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }

    private void browseDishesByRating() throws Exception {
        String sql =
                "SELECT " +
                "  dish_id AS dish_id, " +
                "  name, " +
                "  cuisine, " +
                "  restaurant, " +
                "  rating, " +
                "  price, " +
                "  CASE " +
                "    WHEN rating >= 4.5 THEN '🌟 Top Rated' " +
                "    WHEN rating >= 4.0 THEN '👍 Good' " +
                "    WHEN rating >= 3.5 THEN '👌 Average' " +
                "    ELSE '👎 Low Rated' " +
                "  END AS status " +
                "FROM dishes " +
                "ORDER BY rating DESC, name ASC";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("DISHES (BY RATING)", rs);
        }
    }

    private void browseDishesByPriceAsc() throws Exception {
        String sql =
                "SELECT dish_id AS dish_id, name, cuisine, restaurant, rating, price, " +
                "  CASE " +
                "    WHEN rating >= 4.5 THEN '🌟 Top Rated' " +
                "    WHEN rating >= 4.0 THEN '👍 Good' " +
                "    WHEN rating >= 3.5 THEN '👌 Average' " +
                "    ELSE '👎 Low Rated' " +
                "  END AS status " +
                "FROM dishes " +
                "ORDER BY price ASC, rating DESC, name ASC";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("DISHES (PRICE: LOW → HIGH)", rs);
        }
    }

    private void browseDishesByPriceDesc() throws Exception {
        String sql =
                "SELECT dish_id AS dish_id, name, cuisine, restaurant, rating, price, " +
                "  CASE " +
                "    WHEN rating >= 4.5 THEN '🌟 Top Rated' " +
                "    WHEN rating >= 4.0 THEN '👍 Good' " +
                "    WHEN rating >= 3.5 THEN '👌 Average' " +
                "    ELSE '👎 Low Rated' " +
                "  END AS status " +
                "FROM dishes " +
                "ORDER BY price DESC, rating DESC, name ASC";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("DISHES (PRICE: HIGH → LOW)", rs);
        }
    }

    private void browseDishesByName() throws Exception {
        String sql =
                "SELECT dish_id AS dish_id, name, cuisine, restaurant, rating, price, " +
                "  CASE " +
                "    WHEN rating >= 4.5 THEN '🌟 Top Rated' " +
                "    WHEN rating >= 4.0 THEN '👍 Good' " +
                "    WHEN rating >= 3.5 THEN '👌 Average' " +
                "    ELSE '👎 Low Rated' " +
                "  END AS status " +
                "FROM dishes " +
                "ORDER BY name ASC, rating DESC";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("DISHES (BY NAME)", rs);
        }
    }

    private void browseDishesByCuisineFilter() throws Exception {
        AppConstants.s.nextLine();
        System.out.print("\nEnter Cuisine: ");
        String cuisine = AppConstants.s.nextLine().trim();
        if (cuisine.isEmpty()) {
            System.out.println("\nInvalid cuisine.");
            return;
        }
        String sql =
                "SELECT dish_id AS dish_id, name, cuisine, restaurant, rating, price, " +
                "  CASE " +
                "    WHEN rating >= 4.5 THEN '🌟 Top Rated' " +
                "    WHEN rating >= 4.0 THEN '👍 Good' " +
                "    WHEN rating >= 3.5 THEN '👌 Average' " +
                "    ELSE '👎 Low Rated' " +
                "  END AS status " +
                "FROM dishes " +
                "WHERE cuisine = ? " +
                "ORDER BY rating DESC, name ASC";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, cuisine);
            try (ResultSet rs = ps.executeQuery()) {
                printResultSetAsTable("DISHES — " + cuisine, rs);
            }
        }
    }

    private void browseDishesByRestaurant() throws Exception {
        String sql =
                "SELECT dish_id AS dish_id, name, cuisine, restaurant, rating, price, " +
                "  CASE " +
                "    WHEN rating >= 4.5 THEN '🌟 Top Rated' " +
                "    WHEN rating >= 4.0 THEN '👍 Good' " +
                "    WHEN rating >= 3.5 THEN '👌 Average' " +
                "    ELSE '👎 Low Rated' " +
                "  END AS status " +
                "FROM dishes " +
                "ORDER BY restaurant ASC, name ASC";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printResultSetAsTable("DISHES (BY RESTAURANT)", rs);
        }
    }

    private static String getNextDishId() {
        // The default ID if the table is empty
        String nextId = "VD001";
        String sql = "SELECT MAX(dish_id) AS max_id FROM dishes";
        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Read the max_id as a String, not a long
                String maxId = rs.getString("max_id");
                // Check if maxId is not null (which happens if the table is not empty)
                if (maxId != null && maxId.startsWith("VD")) {
                    int numericPart = Integer.parseInt(maxId.substring(2));
                    // Increment the numeric part
                    numericPart++;
                    // Format the new ID back to the "VD" followed by a 3-digit zero-padded number
                    // For example, 501 becomes "VD501"
                    nextId = String.format("VD%03d", numericPart);
                }
            }
        } catch (SQLException | NumberFormatException e) {
            // Catch both database errors and number parsing errors
            System.out.println("Error generating next dish_id: " + e.getMessage());
            // Return the default ID in case of any error
            return null;
        }
        return nextId;
    }

    public static void browseDishesByRestaurant(String res_name) throws Exception {
        String sql =
                "SELECT " +
                        "  dish_id AS dish_id, " +
                        "  name, " +
                        "  cuisine, " +
                        "  restaurant, " +
                        "  rating, " +
                        "  price, " +
                        "  CASE " +
                        "    WHEN rating >= 4.5 THEN '🌟 Top Rated' " +
                        "    WHEN rating >= 4.0 THEN '👍 Good' " +
                        "    WHEN rating >= 3.5 THEN '👌 Average' " +
                        "    ELSE '👎 Low Rated' " +
                        "  END AS status " +
                        "FROM dishes " +
                        "WHERE restaurant = ? " +
                        "ORDER BY rating DESC, name ASC";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, res_name);
            try (ResultSet rs = ps.executeQuery()) {
                printResultSetAsTable("DISHES — " + res_name, rs);
            }
        }
    }

    public static Models.Dish getDishByIdAndRestaurant(String dishId, String restaurantName) {
        try {
            PreparedStatement stmt = AppConstants.connection.prepareStatement(
                    "SELECT * FROM dishes WHERE dish_id = ? AND restaurant = ?"
            );
            stmt.setString(1, dishId);
            stmt.setString(2, restaurantName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Models.Dish(
                        rs.getString("dish_id"),
                        rs.getString("name"),
                        rs.getString("cuisine"),
                        rs.getString("restaurant"),
                        rs.getDouble("rating"),
                        rs.getDouble("price")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRestaurantNameByDishId(String d_Id) {
        PreparedStatement stmt = null;
        String resName = null;
        try {
            stmt = AppConstants.connection.prepareStatement(
                    "SELECT restaurant FROM dishes WHERE dish_id = ?");
            stmt.setString(1, d_Id);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                resName = rs.getString("restaurant");
            }
        } catch (SQLException e) {
            System.out.print("\nException %s arise in DishDAO/getRestaurantNameByDishId.");
        }
        return resName;
    }

    public static String getRestaurantNameById(String resId) {
        String resName = null;
        try {
            java.sql.PreparedStatement stmt = AppConstants.connection.prepareStatement(
                    "SELECT name FROM restaurants WHERE id = ?;");
            stmt.setString(1, resId);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                resName = rs.getString("name");
            }
        } catch (Exception e) {
            System.out.printf("Exception %s arise in DishDAO/getRestaurantNameById.");
        }
        return resName;
    }

    public static void addDishes(String resName) {
        String dish_id = getNextDishId();
        AppConstants.s.nextLine();
        System.out.print("\nEnter dish name: ");
        String name = AppConstants.s.nextLine().trim();
        while (!Validators.validateDishName(name)) {
            System.out.print("Invalid dish name. Please try again: ");
            name = AppConstants.s.nextLine().trim();
        }
        System.out.print("Enter cuisine: ");
        String cuisine = AppConstants.s.nextLine().trim();
        while (!Validators.validateString(cuisine)) {
            System.out.print("Invalid cuisine. Please try again: ");
            cuisine = AppConstants.s.nextLine().trim();
        }
        System.out.print("Enter rating :- ");
        double rating = 0;
        while (true) {
            try {
                rating = AppConstants.s.nextDouble();
                if (rating < 0) {
                    System.out.print("Invalid price. Please try again: ");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Enter numbers only.");
            }
        }
        System.out.print("Enter price :- ");
        double price = 0;
        while (true) {
            try {
                price = AppConstants.s.nextDouble();
                if (price < 0) {
                    System.out.print("Invalid price. Please try again: ");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Enter numbers only.");
            }
        }
        Models.Dish dish = new Dish(dish_id, name, cuisine, resName, rating, price);
        addDish(dish);
    }

    private static void addDish(Models.Dish dish) {
        String addDishSql = "INSERT INTO dishes VALUES (?,?,?,?,?,?)";
        try(PreparedStatement ps = AppConstants.connection.prepareCall(addDishSql)) {
            ps.setString(1,dish.getDish_id());
            ps.setString(2,dish.getName());
            ps.setString(3,dish.getCuisine());
            ps.setString(4,dish.getRestaurant());
            ps.setDouble(5,dish.getRating());
            ps.setDouble(6,dish.getPrice());
            if(ps.executeUpdate()>0) {
                System.out.println("Dish "+dish.getName()+" added successfully.");
            }
            else {
                System.out.println("Error adding dish: " + ps.getUpdateCount() + " row(s) affected.");
            }
        }
        catch (SQLException e) {
            System.out.println("Error adding dish: " + e.getMessage());
        }
    }

    // ---------- Helpers for tabular display (mirrors OrderDAO style) ----------

    private static void printResultSetAsTable(String title, ResultSet rs) throws SQLException {
        // stop if rs is null
        if (rs == null) {
            System.out.println("\nNo data found for " + title);
            return;
        }

        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();

        List<String[]> rows = new ArrayList<>();
        String[] headers = new String[colCount];
        int[] types = new int[colCount];

        // headers & types
        for (int i = 1; i <= colCount; i++) {
            headers[i - 1] = md.getColumnLabel(i);
            types[i - 1] = md.getColumnType(i);
        }

        // collect rows
        while (rs.next()) {
            String[] row = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                Object val = rs.getObject(i);
                row[i - 1] = formatValue(headers[i - 1], types[i - 1], val);
            }
            rows.add(row);
        }

        if (rows.isEmpty()) {
            System.out.println("\nNo data found for " + title);
            return;
        }

        final String GAP = "     ";
        final int rowsPerCol = 100; // <= chunk size (100–100–100 across)
        int numCols = (int) Math.ceil(rows.size() / (double) rowsPerCol);

        // split into column-chunks
        List<List<String[]>> columns = new ArrayList<>(numCols);
        for (int c = 0; c < numCols; c++) {
            int start = c * rowsPerCol;
            int end = Math.min(start + rowsPerCol, rows.size());
            columns.add(rows.subList(start, end));
        }

        // widths & separators per chunk
        List<int[]> colWidths = new ArrayList<>(numCols);
        List<String> seps = new ArrayList<>(numCols);
        for (List<String[]> colRows : columns) {
            int[] w = computeWidths(headers, colRows);
            colWidths.add(w);
            seps.add(buildSeparator(w));
        }

        // title & top border
        String fullSep = String.join(GAP, seps);
        System.out.println();
        System.out.println(centerTitleInSeparator(title, fullSep));
        System.out.println(fullSep);

        // headers row (for all chunks)
        for (int c = 0; c < numCols; c++) {
            System.out.print(buildRow(headers, colWidths.get(c)));
            if (c < numCols - 1) System.out.print(GAP);
        }
        System.out.println();
        System.out.println(fullSep);

        // ---- KEY CHANGE: print rows without wasting lines on trailing empty columns ----
        // max height among chunks
        int maxRows = columns.stream().mapToInt(List::size).max().orElse(0);

        for (int r = 0; r < maxRows; r++) {
            // find the rightmost column that still has data at row r
            int lastColToPrint = -1;
            for (int c = numCols - 1; c >= 0; c--) {
                if (r < columns.get(c).size()) { lastColToPrint = c; break; }
            }
            // if no columns have data at this row, stop completely
            if (lastColToPrint == -1) break;

            // print only up to that rightmost non-empty column
            for (int c = 0; c <= lastColToPrint; c++) {
                List<String[]> colRows = columns.get(c);
                if (r < colRows.size()) {
                    System.out.print(buildRow(colRows.get(r), colWidths.get(c)));
                } else {
                    // interior columns (left of lastColToPrint) still need alignment
                    System.out.print(buildEmptyRow(colWidths.get(c)));
                }
                if (c < lastColToPrint) System.out.print(GAP);
            }
            System.out.println();
        }

        // bottom border
        // (okay to show full width once; it’s just one line)
        System.out.println(fullSep);
    }

    // Helper: pad string to given width
    private static String pad(String text, int width) {
        if (text == null) text = "";
        return String.format("%-" + width + "s", text);
    }

    // Helper: center text in given width
    private static String center(String text, int width) {
        if (text.length() >= width) return text;
        int left = (width - text.length()) / 2;
        int right = width - text.length() - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }

    // compute widths per column
    private static int[] computeWidths(String[] headers, List<String[]> rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = Math.max(3, headers[i].length());
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] != null) {
                    widths[i] = Math.max(widths[i], row[i].length());
                }
            }
        }
        return widths;
    }

    // build a row (for headers or data)
    private static String buildRow(String[] row, int[] widths) {
        StringBuilder sb = new StringBuilder("| ");
        for (int i = 0; i < row.length; i++) {
            String fmt = "%-" + widths[i] + "s"; // left align always for simplicity
            sb.append(String.format(fmt, row[i] == null ? "-" : row[i])).append(" | ");
        }
        return sb.toString();
    }

    // empty row (used when one column has fewer entries than another)
    private static String buildEmptyRow(int[] widths) {
        StringBuilder sb = new StringBuilder("| ");
        for (int w : widths) {
            sb.append(" ".repeat(w)).append(" | ");
        }
        return sb.toString();
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
}