package Session;
import java.io.*;
import Constants.*;
import Utils.*;
import Services.*;
import Dao.*;
import Db.*;
import Menus.*;
import Models.*;
import java.util.*;
public class SessionManager {
    static File logs = new File("LogsInfo.txt");

    static {
        try {
            BufferedWriter Writer = new BufferedWriter(new FileWriter(logs,true));
//            Writer.write(AppConstants.HEADER);
//            Writer.newLine();
//            Writer.newLine();
            Writer.newLine();
            Writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void NewRegistration(String id,Boolean isCreated) {
        try {
            BufferedWriter Writer = new BufferedWriter(new FileWriter(logs,true));
            String timestamp = java.time.LocalDateTime.now().toString();
            String s = (isCreated)?(AppConstants.NEW_USER_SUCCESS + " (" + timestamp + "): " +  id):AppConstants.NEW_USER_FAIL;
            Writer.write(s);
            Writer.newLine();
            Writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Login(String id) {
        try {
            BufferedWriter Writer = new BufferedWriter(new FileWriter(logs,true));
            String timestamp = java.time.LocalDateTime.now().toString();
            String s = (AppConstants.customerValid) ? AppConstants.LOG_SUCCESS + " (" + timestamp + "): " + id : AppConstants.LOG_FAIL + " (" + timestamp + "): " + id;
            Writer.write(s);
            Writer.newLine();
            Writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}