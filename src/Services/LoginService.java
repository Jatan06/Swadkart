package Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginService {
    public static void displayLogins() {
        File logsFile = new File("LogsInfo.txt");

        String headerFmt = "%-6s | %-20s | %-26s | %-20s%n";
        String rowFmt    = "%-6s | %-20s | %-26s | %-20s%n";
        String separator = "-".repeat(6) + "-+-" + "-".repeat(20) + "-+-" + "-".repeat(26) + "-+-" + "-".repeat(20);

        // Print table header
        System.out.println(separator);
        System.out.printf(headerFmt, "No.", "Condition", "Timestamp", "ID");
        System.out.println(separator);

        if (!logsFile.exists() || logsFile.isDirectory()) {
            System.out.printf(rowFmt, "-", "(no file)", "-", "-");
            System.out.println(separator);
            return;
        }

        Pattern linePattern = Pattern.compile("^(.*?)\\s*\\(([^)]+)\\)\\s*:\\s*(.+)$");
        boolean hasRows = false;
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(logsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                if (isSkippableLine(line)) continue;

                Matcher m = linePattern.matcher(line);
                if (m.matches()) {
                    String condition = m.group(1).trim();   // e.g., "Login Successful"
                    String timestamp = m.group(2).trim();   // e.g., "2025-08-13T11:30:54.347840200"
                    String id = m.group(3).trim();          // e.g., "u-0011"
                    String tsRounded = formatTimestampTo2Decimals(timestamp);

                    index++;
                    System.out.printf(rowFmt, String.valueOf(index), condition, tsRounded, id);
                    hasRows = true;
                } else {
                    // Fallback for lines not matching expected pattern
                    index++;
                    System.out.printf(rowFmt, String.valueOf(index), line.trim(), "-", "-");
                    hasRows = true;
                }
            }
        } catch (IOException e) {
            System.out.printf(rowFmt, "-", "Failed to read file", "-", e.getMessage());
        }

        if (!hasRows) {
            System.out.printf(rowFmt, "-", "(no entries)", "-", "-");
        }
        System.out.println(separator);
    }

    // Skip banners like "============   SWADKART   ============" and similar lines.
    private static boolean isSkippableLine(String line) {
        String trimmed = line.trim();
        // Pure equals or equals with spaces only
        if (trimmed.matches("^=+$")) return true;
        // Lines that are mostly equals around some text spacing
        if (trimmed.matches("^=+(\\s+)=*$")) return true;
        // Specific banner with brand in the middle (case-insensitive), equals and spaces around
        if (trimmed.matches("^=+\\s*SWADKART\\s*=+$") || trimmed.matches("^=+\\s*swadkart\\s*=+$")) return true;
        // Generic rule: lines that are made of '=', spaces, and one word in the middle
        if (trimmed.matches("^=+\\s*[A-Za-z0-9 _-]{1,30}\\s*=+$")) return true;
        return false;
    }

    // Rounds the fractional seconds to 2 decimal places (centiseconds) with proper carry handling.
    private static String formatTimestampTo2Decimals(String isoTs) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(isoTs);
            int nanos = ldt.getNano();
            long roundedCentis = Math.round(nanos / 10_000_000.0); // centiseconds (0..100)
            long targetNanos = roundedCentis * 10_000_000L;
            long delta = targetNanos - nanos;
            LocalDateTime adjusted = ldt.plusNanos(delta);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
            return adjusted.format(fmt);
        } catch (DateTimeParseException ex) {
            // Fallback: try to trim/round textual fraction if present; otherwise return as-is.
            // Matches "...:ss(.fraction)?"
            Pattern p = Pattern.compile("^(.*?\\d{2}:\\d{2}:\\d{2})(?:\\.(\\d+))?(.*)$");
            Matcher m = p.matcher(isoTs);
            if (!m.matches()) return isoTs;

            String base = m.group(1);
            String frac = m.group(2);
            String tail = m.group(3) == null ? "" : m.group(3);

            if (frac == null || frac.isEmpty()) {
                return base + ".00" + tail;
            }

            // Round the fraction to 2 digits
            // Pad to at least 3 digits for safe rounding, then numeric round
            String padded = (frac + "000000000").substring(0, 9);
            double asSecondsFrac = Double.parseDouble("0." + padded);
            double rounded = Math.round(asSecondsFrac * 100.0) / 100.0; // two decimals
            String two = String.format(java.util.Locale.ROOT, "%.2f", rounded); // "0.xx"
            String twoDigits = two.substring(2); // "xx"
            return base + "." + twoDigits + tail;
        }
    }
}
