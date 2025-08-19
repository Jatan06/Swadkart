package Services;
import java.io.IOException;
public class SpeakTextService {
    public static void speak(String message) {
        try {
            String command = "PowerShell -Command \"Add-Type –AssemblyName System.Speech; " +
                    "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                    "$speak.Speak('" + message + "');\"";
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            System.out.println("Exception at Services/SpeakTextService/speak at line 11");
        }
    }
}
