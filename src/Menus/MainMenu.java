package Menus;
import Admin.AdminMenu;
import Constants.AppConstants;
import Db.DBConnection;
import Services.SpeakTextService;
import Utils.*;

public class MainMenu {
    public static void run() {
        try {
            if (DBConnection.DbConnection()) {
                System.out.println(AppConstants.welcome);
                show();
            }
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected static void show() {
        AppConstants.show = true;
        while (AppConstants.show) {
            try {
                Thread.sleep(1000);
                showMenu();
                int n;
                while (true) {
                    try {
                        n = AppConstants.s.nextInt();
                        break;
                    } catch (Exception e) {
                        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease choose a number only: ");
                        AppConstants.s.nextLine();
                    }
                }
                switch (n) {
                    case 1:
                        CustomerMenu.newCustomer();
                        break;
                    case 2:
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            System.out.println("Exception at MainMenu/show at line 51");
                        }
                        System.out.print("\n\n============== Login ==============\n");
                        login();
                        break;
                    case 3:
                        SpeakTextService.speak("SwadKart signing off... pet full, mood chill!");
                        Thread.sleep(2000);
                        System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSwadKart signing off... pet full, mood chill! \uD83D\uDE0E\uD83C\uDF55");
                        AppConstants.show = false;
                        break;
                    default:
                        System.out.println("Invalid Choice Please try again !");
                        break;
                }
            }
            catch(Exception e){
                System.out.println("Exception at MainMenu/show() at line 67.");
                AppConstants.s.nextLine(); // attempt to clear buffer for next loop
            }
        }
    }

    public static void login() {
        login_main();
    }

    private static void login_main() {
        boolean id_verification = true;
        String id = "";
        while (id_verification) {
            System.out.print("\nEnter id (u-xxxx/xxxx) : ");
            id = AppConstants.s.next().trim();
            if (id.charAt(0) == 'u' || id.charAt(0) == 'U') {
                if (id.charAt(1) == '-') {
                    int j = 0;
                    for (int i = 2; i < id.length(); i++,j++) {
                        char c = id.charAt(i);
                        if (Character.isDigit(c)) {
                            if(id.length()==6) {
                                continue;
                            }
                            else {
                                System.out.println("Enter valid id like u-xxxx.");
                                break;
                            }
                        } else {
                            System.out.println("Invalid Id! Please enter again.");
                            break;
                        }
                    }
                } else {
                    System.out.println("Invalid id, please try again!");
                }
                if (CustomerMenu.validateId(id)) {
                    id_verification = false;
                }
                else {
                    System.out.print("\nNo id " + id + " found. Have you Register ? (y/n) :- ");
                    while (true) {
                        try {
                            if (AppConstants.s.next().charAt(0) == 'y') {
                                System.out.println("Forgot id ? (y/n) :- ");
                                if (AppConstants.s.next().charAt(0) == 'y' || AppConstants.s.next().charAt(0) == 'Y') {
                                    System.out.println("Enter mobile number :- ");
                                    String mo_no = AppConstants.s.next().trim();
                                    while (!Validators.validateMobileNumber(mo_no)) {
                                        System.out.println("Enter valid mobile number :- ");
                                        mo_no = AppConstants.s.next().trim();
                                    }
                                    if (CustomerMenu.getId(mo_no) == null) {
                                        System.out.println("Please try again.");
                                        return;
                                    } else {
                                        System.out.println("\nYour id : " + id);
                                    }
                                } else if (AppConstants.s.next().charAt(0) == 'n' || AppConstants.s.next().charAt(0) == 'N') {
                                    continue;
                                }
                                break;
                            } else if (AppConstants.s.next().charAt(0) == 'n') {
                                return;
                            } else {
                                System.out.println("\nEnter y/n only.");
                            }
                        } catch (Exception e) {
                            System.out.println("\nEnter only a single character.");
                        }
                    }
                }
            }
            else if (id.charAt(0) == 'a' || id.charAt(0) == 'A') {
                while (!AdminMenu.adminValidatorId(id)) {
                    System.out.println("Invalid Admin password.");
                    id = AppConstants.s.next().trim();
                }
                System.out.print("Enter password :- ");
                String password = AppConstants.s.next().trim();
                while (!AdminMenu.adminValidatorPassword(password)) {
                    System.out.println("Invalid Admin password.");
                    password = AppConstants.s.next().trim();
                }
                AdminMenu.adminMenu();
                MainMenu.show();
            }
            else {
                for (int i = 0;i<id.length();i++) {
                    if(Character.isDigit(id.charAt(i))) {
                        continue;
                    }
                    else {
                        System.out.print("\nInvalid id, Have you registered ? (y/n) :- ");
                        if(AppConstants.s.next().charAt(0)=='n') {
                            return;
                        }
                        else {
                            System.out.println("\nPlease try again.");
                            login_main();
                        }
                    }
                }
                if(id.length()==1) {
                    id = "u-000"+id;
                } else if (id.length()==2) {
                    id = "u-00"+id;
                } else if (id.length()==3) {
                    id = "u-0"+id;
                } else if (id.length()==4) {
                    id = "u-"+id;
                }
                else {
                    System.out.println("Invalid id. Please try again!");
                    return;
                }
                if(CustomerMenu.validateId(id)) {
                    id_verification = false;
                }
                else {
                    System.out.print("\nNo id " + id + " found. Have you Register ? (y/n) :- ");
                    while (true) {
                        try {
                            if (AppConstants.s.next().charAt(0) == 'y') {
                                System.out.println("Forgot id ? (y/n) :- ");
                                if (AppConstants.s.next().charAt(0) == 'y' || AppConstants.s.next().charAt(0) == 'Y') {
                                    System.out.println("Enter mobile number :- ");
                                    String mo_no = AppConstants.s.next().trim();
                                    while (!Validators.validateMobileNumber(mo_no)) {
                                        System.out.println("Enter valid mobile number :- ");
                                        mo_no = AppConstants.s.next().trim();
                                    }
                                    if (CustomerMenu.getId(mo_no) == null) {
                                        System.out.println("Please try again.");
                                        return;
                                    } else {
                                        System.out.println("\nYour id : " + id);
                                    }
                                } else if (AppConstants.s.next().charAt(0) == 'n' || AppConstants.s.next().charAt(0) == 'N') {
                                    continue;
                                }
                                break;
                            } else if (AppConstants.s.next().charAt(0) == 'n') {
                                return;
                            } else {
                                System.out.println("\nEnter y/n only.");
                            }
                        } catch (Exception e) {
                            System.out.println("\nEnter only a single character.");
                        }
                    }
                }
            }
        }
        boolean password_verification = true;
        while (password_verification) {
            try {
                System.out.print("Enter Password or Enter 'f' for forgot password :- ");
                String password = AppConstants.s.next().trim();
                if(password.length()==1 && (password.charAt(0)=='f' || password.charAt(0)=='F')) {
                    if(CustomerMenu.forgotPassword(id)) {
                        CustomerMenu.customerMenu(id);
                        System.out.println("Enter password again :- ");
                        password = AppConstants.s.next().trim();
                    }
                    else {
                        System.out.println("Sorry, Please try again!");
                        return;
                    }
                }
                while (!Validators.validatePassword(password)) {
                    System.out.print("\nEnter valid password :- ");
                    password = AppConstants.s.next().trim();
                }
                while(!CustomerMenu.customerValidator(id,password)) {
                    System.out.print("\nIncorrect password, enter again :- ");
                    password = AppConstants.s.next().trim();
                }
                CustomerMenu.customerMenu(id);
                password_verification = false;
                MainMenu.show();
            } catch (Exception e) {
                System.out.println("Exception at MainMenu/show at line 195.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t-------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|\t\t\t\t\t\t\t\t\t*************\t\t\t\t\t\t\t\t\t|");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|\t\t\t\t\t\t\t\t\t+ Main Menu +\t\t\t\t\t\t\t\t\t|");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|\t\t\t\t\t\t\t\t\t*************\t\t\t\t\t\t\t\t\t|");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|\t\t\t\t\t\t\t\t1. New To Swadkart\t\t\t\t\t\t\t\t\t|");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|\t\t\t\t\t\t\t\t2. Login\t\t\t\t\t\t\t\t\t\t\t|");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|\t\t\t\t\t\t\t\t3. Exit\t\t\t\t\t\t\t\t\t\t\t\t|");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t-------------------------------------------------------------------------------------");
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }
}