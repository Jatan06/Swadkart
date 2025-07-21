package Menus;

public class AdminMenu {
    static boolean adminValidator(String id,String password) {
        if(id.equals("") && password.equals("")) {
            return true;
        }
        else {
            return false;
        }
    }
    static void adminMenu(){

    }
}
