// Assignment 5
// lindsay aaron
// aaronlindsay
// lei bowen
// bowenleis

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Sound {

  private Clip clip;

  public Sound(String fileName) {
    // specify the sound to play
    // (assuming the sound can be played by the audio system)
    // from a wave File
    try {
      File file = new File(fileName);
      if (file.exists()) {
        AudioInputStream sound = AudioSystem.getAudioInputStream(file);
        // load the sound into memory (a Clip)
        clip = AudioSystem.getClip();
        clip.open(sound);
      }
      else {
        throw new RuntimeException("Sound: file not found: " + fileName);
      }
    }
    catch (LineUnavailableException e) {
      System.out.println("LineUnavailableException: " + e);
    }
    catch (UnsupportedAudioFileException e) {
      System.out.println("UnsupportedAudioFileException: " + e);
    }
    catch (IOException e) {
      System.out.println("IOException: " + e);
    }
    catch (NoClassDefFoundError e) {
      System.out.println("NoClassDefFoundError: " + e);
    }
  }

  // play, stop, loop the sound clip
  public void play() {
    clip.setFramePosition(0);  // Must always rewind!
    clip.start();
  }
  
  public void loop() {
    clip.loop(Clip.LOOP_CONTINUOUSLY);
  }
  
  public void stop() {
    clip.stop();
  }

  // sounds
  public static final Sound UNDERWATER = new Sound("underwaterSounds.wav");
  public static final Sound CHOMP = new Sound("chomp.wav");
}
