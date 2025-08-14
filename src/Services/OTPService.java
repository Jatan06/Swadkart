package Services;
import com.twilio.*;
import com.twilio.rest.api.v2010.account.*;
import java.util.Random;
public class OTPService {
    private static final String ACCOUNT_SID = "ACaab97c0aac083c2cc628ebb1c2c11733";
    private static final String AUTH_TOKEN = "364bb342ac82a2766b82b1b5a0e55b18";
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    public static boolean sendOTP(String phoneNumber, String otp) {
        try {
            if (!phoneNumber.matches("^\\+[1-9]\\d{9,14}$")) {
                throw new IllegalArgumentException("\nPhone number must be in E.164 format (e.g., +1234567890).");
            }
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(phoneNumber),
                    new com.twilio.type.PhoneNumber("+15075026399"), // Your verified Twilio number
                    "Swadkart OTP: \"" + otp + "\". Use this code to complete your registration. It is valid for 10 minutes. Do not share it with anyone."
            ).create();
            System.out.println("\nOTP sent successfully to " + phoneNumber);
            return true;
        } catch (com.twilio.exception.ApiException e) {
            System.err.println("Twilio API Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Failed to send OTP: " + e.getMessage());
            return false;
        }
    }
}