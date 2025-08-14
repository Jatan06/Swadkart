//DishDAO.java
package Dao;

import java.sql.*;
import java.util.*;
import Constants.*;

public class DishDAO {

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

    public static void browseDishesByCuisine(String dish_category) throws Exception {
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
                "WHERE cuisine = ? " +
                "ORDER BY rating DESC, name ASC";

        try (PreparedStatement ps = AppConstants.connection.prepareStatement(sql)) {
            ps.setString(1, dish_category);
            try (ResultSet rs = ps.executeQuery()) {
                printResultSetAsTable("DISHES — " + dish_category, rs);
            }
        }
    }

    public static Models.Dish getDishByIdAndRestaurant(String dishId) {
        try {
            java.sql.PreparedStatement stmt = AppConstants.connection.prepareStatement(
                    "SELECT * FROM dishes WHERE dish_id = ?");
            stmt.setString(1, dishId);
            java.sql.ResultSet rs = stmt.executeQuery();
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
            // ignore
        }
        return null;
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
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.printf("Exception %s arise in getRestaurantNameById.");
        }
        return resName;
    }

    // ---------- Helpers for tabular display (mirrors OrderDAO style) ----------

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

        // Money-like formatting (price)
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
        return (s.isEmpty()) ? "-" : s;
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
        int total = 1; // starting '|'
        for (int w : widths) total += 1 + w + 1 + 1; // " " + content + " " + "|"
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