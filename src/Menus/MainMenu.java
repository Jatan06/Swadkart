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
                        System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+"Please choose a number only: "+AppConstants.ANSI_RESET);
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
                            System.out.println(AppConstants.TEXT_ANSI_RED+"Exception at MainMenu/show"+AppConstants.ANSI_RESET);
                        }
                        System.out.print("\n\t\t\t"+AppConstants.BG_ANSI_BLACK+"============== Login =============="+AppConstants.ANSI_RESET+"\n");
                        login();
                        break;
                    case 3:
                        SpeakTextService.speak("SwadKart signing off...Thank You !");
                        Thread.sleep(2000);
                        System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSwadKart signing off...Thank You! \uD83D\uDE0E\uD83C\uDF55");
                        AppConstants.show = false;
                        return;
                    default:
                        System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET+"\n\n");
                        break;
                }
            }
            catch(Exception e){
                System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_INVALID_INPUT+AppConstants.ANSI_RESET);
                AppConstants.s.nextLine(); // attempt to clear buffer for next loop
            }
        }
    }

    private static void login() {
        boolean id_verification = true;
        String id = "";
        while (id_verification) {
            System.out.print("\nEnter id (u-xxxx/xxxx) or enter 'b' for back: ");
            id = AppConstants.s.next().trim();
            if(id.equalsIgnoreCase("b")) return;
            else if (id.charAt(0) == 'u' || id.charAt(0) == 'U') {
                if (id.charAt(1) == '-') {
                    if(CustomerMenu.validateId(id)) {
                        id_verification = false;
                    }
                    else {
                        System.out.print("\n"+AppConstants.TEXT_ANSI_RED+"\nUser not found. enter any key or enter 'b' to go back :- "+AppConstants.ANSI_RESET+"\n");
                        if(AppConstants.s.next().trim().equalsIgnoreCase("b")) {return;}
                        else login();
                    }
                }
                else {
                    System.out.print("\n"+AppConstants.TEXT_ANSI_RED+"\nInvalid id. Please try again! enter any key or enter 'b' to go back :- "+AppConstants.ANSI_RESET+"\n");
                    if(AppConstants.s.next().trim().equalsIgnoreCase("b")) {return;}
                    else login();
                }
            }
            else if (id.charAt(0) == 'a' || id.charAt(0) == 'A') {
                while (!AdminMenu.adminValidatorId(id)) {
                    System.out.println(AppConstants.TEXT_ANSI_RED+"Invalid Admin password."+AppConstants.ANSI_RESET);
                    id = AppConstants.s.next().trim();
                }
                System.out.print("Enter password or enter 'b' for back:- ");
                String password = AppConstants.s.next().trim();
                if(password.equalsIgnoreCase("b")) return;
                while (!AdminMenu.adminValidatorPassword(password)) {
                    System.out.println(AppConstants.TEXT_ANSI_RED+"Invalid Admin password."+AppConstants.ANSI_RESET);
                    password = AppConstants.s.next().trim();
                }
                AdminMenu.adminMenu();
                return;
            }
            else {
                for (int i = 0;i<id.length();i++) {
                    if(!Character.isDigit(id.charAt(i))) {
                        System.out.print("\n"+AppConstants.TEXT_ANSI_RED+"\nInvalid id. Please try again! enter any key or enter 'b' to go back :- "+AppConstants.ANSI_RESET+"\n");
                        if(AppConstants.s.next().trim().equalsIgnoreCase("b")) {return;}
                        else login();
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
                    System.out.print("\n"+AppConstants.TEXT_ANSI_RED+"\nInvalid id. Please try again! enter any key or enter 'b' to go back :- "+AppConstants.ANSI_RESET+"\n");
                    if(AppConstants.s.next().trim().equalsIgnoreCase("b")) {return;}
                    else login();
                }
                if(CustomerMenu.validateId(id)) {
                    id_verification = false;
                }
                else {
                    System.out.print("\n"+AppConstants.TEXT_ANSI_RED+AppConstants.ERR_USER_NOT_FOUND+"try again !, enter any key or 'b' to go back :- "+AppConstants.ANSI_RESET+"\n");
                    if(AppConstants.s.next().trim().equalsIgnoreCase("b")) {return;}
                    else login();
                }
            }
        }
        boolean password_verification = true;
        while (password_verification) {
            try {
                System.out.print("Enter Password or Enter 'f' for forgot password :- ");
                String password = AppConstants.s.next().trim();
                if(password.equalsIgnoreCase("f")) {
                    if(CustomerMenu.forgotPassword(id)) {
                        System.out.println(AppConstants.BG_ANSI_BLACK+"Enter password again  or enter 'b' to go back :- "+AppConstants.ANSI_RESET);
                        password = AppConstants.s.next().trim();
                        if(password.equalsIgnoreCase("b")) return;
                        CustomerMenu.customerMenu(id);
                    }
                    else {
                        System.out.println(AppConstants.TEXT_ANSI_RED+"Sorry, Please try again!"+AppConstants.ANSI_RESET);
                        return;
                    }
                }
                while (!Validators.validatePassword(password)) {
                    System.out.print(AppConstants.TEXT_ANSI_RED+"\nEnter valid password or enter 'b' to go back :- "+AppConstants.ANSI_RESET);
                    password = AppConstants.s.next().trim();
                    if(password.equalsIgnoreCase("b")) return;
                }
                while(!CustomerMenu.customerValidator(id,password)) {
                    System.out.print(AppConstants.TEXT_ANSI_RED+"\nIncorrect password, enter again or enter 'f' or enter 'b' :- "+AppConstants.ANSI_RESET);
                    password = AppConstants.s.next().trim();
                    if(password.length()==1 && (password.charAt(0)=='f' || password.charAt(0)=='F')) {
                        AppConstants.s.nextLine();
                        if(CustomerMenu.forgotPassword(id)) {
                            System.out.println(AppConstants.BG_ANSI_BLACK+"Enter password again :- "+AppConstants.ANSI_RESET);
                            password = AppConstants.s.next().trim();
                            while(!CustomerMenu.customerValidator(id,password)) {
                                System.out.println("Invalid password, Enter password that you had set or 'b' for back :- ");
                                password = AppConstants.s.next().trim();
                                if(password.equalsIgnoreCase("b")) return;
                            }
                            break;
                        }
                    } else if (password.length()==1 && (password.charAt(0)=='b' || password.charAt(0)=='B')) {
                        return;
                    }
                }
                Thread.sleep(1000);
                System.out.println(AppConstants.TEXT_ANSI_GREEN+AppConstants.SUCCESS_LOGIN+AppConstants.ANSI_RESET);
                password_verification = false;
            } catch (Exception e) {
                System.out.println(AppConstants.TEXT_ANSI_RED+"Exception at MainMenu"+AppConstants.ANSI_RESET);
            }
        }
        CustomerMenu.customerMenu(id);
    }

    private static void showMenu() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t*************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t+ Main Menu +\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t*************\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t\t\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t1. New To Swadkart\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t2. Login\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"|\t\t\t\t\t3. Exit\t\t\t\t\t\t\t\t  |"+AppConstants.ANSI_RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+AppConstants.BG_ANSI_BLACK+"-----------------------------------------------------------"+AppConstants.ANSI_RESET);
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an option: ");
    }
}