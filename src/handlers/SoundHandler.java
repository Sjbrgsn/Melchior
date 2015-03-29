package handlers;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Created by holmgr on 2015-03-20.
 * Loads and plays music and sound effects, implements the Singleton pattern.
 */
public class SoundHandler {

    private AudioInputStream effectStream;
    private AudioInputStream musicStream;
    private Clip shopClip;
    private Clip musicClip;
    private static SoundHandler INSTANCE = new SoundHandler();

    public SoundHandler() {

        try {
            if (effectStream == null) {
                effectStream = AudioSystem.getAudioInputStream(
                        SoundHandler.class.getClassLoader().getResourceAsStream("sound/shot.wav"));
                shopClip = AudioSystem.getClip();
                shopClip.open(effectStream);
            }

            musicStream = AudioSystem.getAudioInputStream(
                    SoundHandler.class.getClassLoader().getResourceAsStream("sound/music.wav"));
            musicClip = AudioSystem.getClip();
            musicClip.open(musicStream);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

    }

    public void playShotFired() {
        shopClip.stop();
        shopClip.setFramePosition(0);
        shopClip.start();
    }

    public void playMusic() {
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        musicClip.start();
    }

    public void stopMusic() {
        musicClip.stop();
    }

    public static SoundHandler getInstance(){
        return INSTANCE;
    }
}
