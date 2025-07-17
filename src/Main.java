import Menus.MainMenu;
class Main {
    static String welcome = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSwadkart - Food Ordering System\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEk command, anek swaad!";
    static {
        System.out.println(welcome);
    }
    public static void main(String[] args) {
        Main.run();
    }
    static void run() {
        MainMenu.show();
    }
}