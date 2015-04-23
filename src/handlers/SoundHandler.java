package handlers;

import controllers.GameConstants;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Created by holmgr on 2015-03-20.
 * Loads and plays music and sound effects, implements the Singleton pattern.
 */
public class SoundHandler {

    private Clip shotClip;
    private Clip musicClip;
    private static SoundHandler INSTANCE = new SoundHandler();

    public SoundHandler() {

        try {
            AudioInputStream effectStream = AudioSystem.getAudioInputStream(
                    SoundHandler.class.getClassLoader().getResourceAsStream("sound/shot.wav"));
            shotClip = AudioSystem.getClip();
            shotClip.open(effectStream);

            AudioInputStream musicStream = AudioSystem.getAudioInputStream(
                    SoundHandler.class.getClassLoader().getResourceAsStream("sound/music.wav"));
            musicClip = AudioSystem.getClip();
            musicClip.open(musicStream);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

    }

    public void playProjectileFired() {
        if (shotClip.isOpen() && GameConstants.PLAY_SOUND_EFFECTS) {
            shotClip.stop();
            shotClip.setFramePosition(0);
            shotClip.start();
        }
    }

    public void playMusic() {
        if (musicClip.isOpen()) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        }
    }

    public void stopMusic() {
        if (musicClip.isRunning()) {
            musicClip.stop();
        }
    }

    public static SoundHandler getInstance(){
        return INSTANCE;
    }
}
