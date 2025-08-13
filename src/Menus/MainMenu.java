package Menus;
import Constants.AppConstants;
import Db.DBConnection;
import Services.SpeakTextService;

public class MainMenu {
    public static void run() {
        if (DBConnection.DbConnection()) {
            System.out.println(AppConstants.welcome);
            show();
        } else {
            return;
        }
    }

    public static void show() {
        AppConstants.show = true;
        while (AppConstants.show) {
            try {
                Thread.sleep(1000);
                System.out.println("===================================");
                System.out.println("|           Main Menu             |");
                System.out.println("===================================");
                System.out.println("|  1. New Customer Registration   |");
                System.out.println("|  2. Existing Customer Login     |");
                System.out.println("|  3. Exit                        |");
                System.out.println("===================================");
                System.out.print("\nPlease select an option: ");

                // Make nextInt safe with buffer cleanup
                int n;
                if (AppConstants.s.hasNextInt()) {
                    n = AppConstants.s.nextInt();
                    AppConstants.s.nextLine();
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    AppConstants.s.next(); // clear invalid token
                    continue;
                }

                switch (n) {
                    case 1:
                        CustomerMenu.newCustomer();
                        break;
                    case 2:
                        Thread.sleep(500);
                        System.out.print("\n============== Login ==============\n");
                        System.out.print("\nEnter id : ");
                        String id = AppConstants.s.nextLine();
                        System.out.print("Enter password : ");
                        String password = AppConstants.s.nextLine();

                        if (!id.isEmpty() && (id.charAt(0) == 'U' || id.charAt(0) == 'u')) {
                            if (CustomerMenu.customerValidator(id, password)) {
                                CustomerMenu.customerMenu(id);
                            } else {
                                System.out.println("\nInvalid User id or User password please try again !\n");
                            }
                        } else if (!id.isEmpty() && (id.charAt(0) == 'A' || id.charAt(0) == 'a')) {
                            if (AdminMenu.adminValidator(id, password)) {
                                AdminMenu.adminMenu();
                            } else {
                                System.out.println("\nInvalid Admin id or Admin password please try again !");
                            }
                        } else {
                            System.out.println("\nInvalid id !");
                        }
                        break;
                    case 3:
                        //Thank you for using SwadKart. Goodbye!
                        SpeakTextService.speak("SwadKart signing off... pet full, mood chill!");
                        Thread.sleep(4000);
                        System.out.println("\nSwadKart signing off... pet full, mood chill! \uD83D\uDE0E\uD83C\uDF55");
                        AppConstants.show = false;
                        break;
                    default:
                        System.out.println("Invalid Choice Please try again !");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                AppConstants.s.nextLine(); // attempt to clear buffer for next loop
            }
        }
    }
}