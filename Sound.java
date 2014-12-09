
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// Most of this class is copied from: http://noobtuts.com/java/play-sounds
public class Sound {
    public static synchronized void play(final String fileName)
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    URL resource = getClass().getResource(fileName);
                    AudioInputStream inputStream
                        = AudioSystem.getAudioInputStream(resource);
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.out.println("Err:"+e.getMessage()+" on "+fileName);
                }
            }
        }).start();
    }
}
