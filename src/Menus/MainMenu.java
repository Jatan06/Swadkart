package Menus;
import java.util.*;
public class MainMenu {
    static boolean b;
    static Scanner s = new Scanner(System.in);
    public static void show() {
        try {
            b = true;
            while (b) {
                System.out.print("\nEnter \"" + 1 + "\" for User\nEnter \"" + 2 + "\" for Admin\nEnter \"" + 3 + "\" for Exit\n\nEnter :- ");
                int n = s.nextInt();
                switch (n) {
                    case 1:
                        System.out.println("\nWelcome User.");
                        break;
                    case 2:
                        System.out.println("\nWelcome Admin.");
                        break;
                    case 3:
                        System.out.println("\nSwadKart signing off... pet full, mood chill! \uD83D\uDE0E\uD83C\uDF55");
                        b = false;
                        break;
                    default:
                        System.out.println("Invalid Choice Please try again !");
                        break;
                }
            }
        }
        catch (Exception e) {

        }
    }
}