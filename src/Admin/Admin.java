package Admin;
public final class Admin {
    private static final String ADMIN_ID = "admin_xyz";
    private static final String ADMIN_PASSWORD = "12345";
    private Admin() {
        // Private constructor to prevent object creation
    }
    public static String getAdminId() {
        return ADMIN_ID;
    }
    public static String getAdminPassword() {
        return ADMIN_PASSWORD;
}
}