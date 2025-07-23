package Session;
import java.io.*;
import Constants.*;
public class SessionManager {
    static File logs = new File("B:\\Work\\Coding Projects\\Java Projects\\Swadkart - Food Ordering System\\Text_Files\\LogsInfo.txt");
    static {
        try {
            BufferedWriter Writer = new BufferedWriter(new FileWriter(logs));
            Writer.write(AppConstants.HEADER);
            Writer.newLine();
            Writer.newLine();
            Writer.newLine();
            Writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void NewRegistration(String id,Boolean isCreated) {
        try {
            BufferedWriter Writer = new BufferedWriter(new FileWriter(logs,true));
            String s = (isCreated)?(AppConstants.NEW_USER_SUCCESS + ": " +  id):AppConstants.NEW_USER_FAIL;
            Writer.write(s);
            Writer.newLine();
            Writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void Login(String id){
        try {
            BufferedWriter Writer = new BufferedWriter(new FileWriter(logs,true));
            String timestamp = java.time.LocalDateTime.now().toString();
            String s = (AppConstants.customerValid) ? AppConstants.LOG_SUCCESS + " (" + timestamp + "): " + id : AppConstants.LOG_FAIL + " (" + timestamp + "): " + id;
            Writer.write(s);
            Writer.newLine();
            Writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
