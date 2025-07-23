package Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import java.util.Random;

public class OTPService {
    private static final String ACCOUNT_SID = "AC8aea7e4ed61acce266b6e3ff58bdbeb0";
    private static final String AUTH_TOKEN = "e626321462d6a7dcfcd9e17c5cd1ec9a";
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    public static void sendOTP(String phoneNumber, String otp) {
        try {
            if (!phoneNumber.matches("^\\+[1-9]\\d{9,14}$")) {
                throw new IllegalArgumentException("Phone number must be in E.164 format (e.g., +1234567890).");
            }
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(phoneNumber),
                    new com.twilio.type.PhoneNumber("+14027519222"), // Your verified Twilio number
                    "Your OTP is: " + otp
            ).create();
            System.out.println("OTP sent successfully to " + phoneNumber);
        } catch (com.twilio.exception.ApiException e) {
            System.err.println("Twilio API Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to send OTP: " + e.getMessage());
        }
    }
}
