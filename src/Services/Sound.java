package Services;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
public class Sound {
    public static void playWav(String resourcePath) {
        try (InputStream audioSrc = Sound.class.getResourceAsStream(resourcePath)) {
            if (audioSrc == null) {
                System.err.println("Sound file not found: " + resourcePath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioSrc);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
