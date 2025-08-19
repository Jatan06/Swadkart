package Admin;
public final class Admin {
    private static final String ADMIN_ID = "Admin_swadkart";
    private static final String ADMIN_PASSWORD = "admin@123";
    private Admin() { /* Private constructor to prevent object creation */ }
    public static String getAdminId() {return ADMIN_ID;}
    public static String getAdminPassword() {return ADMIN_PASSWORD; }
}