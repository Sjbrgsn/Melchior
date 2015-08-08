package handlers;

import controllers.GameConstants;

import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.FloatControl.Type;
import javax.swing.*;
import java.io.IOException;

/**
 * Created by holmgr on 2015-03-20.
 * Loads and plays music and sound effects, implements the Singleton pattern.
 */
public class SoundHandler {

    private Clip[] shotClips = new Clip[0];
    private Clip musicClip = null;
    private final static SoundHandler INSTANCE = new SoundHandler();

    public SoundHandler() {

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                SoundHandler.class.getClassLoader().getResourceAsStream("sound/shot.wav"))) {
            int shotClipSize = 10;
            shotClips = new Clip[shotClipSize];

            AudioFormat af = audioInputStream.getFormat();
            int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
            byte[] audio = new byte[size];
            Info info = new Info(Clip.class, af, size);


            // Failed loading sound effect resource
            if (audioInputStream.read(audio, 0, size) != size) {
                throw IOException("Failed loading sound effect resource");
            }

            final double shotVolume = 0.3; // Volume reduction
            float dB = (float) (Math.log(shotVolume) / Math.log(10.0) * 20.0); // Convert into dB

            for (int i = 0; i < shotClipSize; i++) {
                shotClips[i] = (Clip) AudioSystem.getLine(info);
                shotClips[i].open(af, audio, 0, size);

                //Volume reduction
                FloatControl gainControl = (FloatControl) shotClips[i].getControl(Type.MASTER_GAIN);
                gainControl.setValue(dB);
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
            e1.printStackTrace();
        }

        try {
            AudioInputStream musicStream = AudioSystem.getAudioInputStream(
                    SoundHandler.class.getClassLoader().getResourceAsStream("sound/music.wav"));
            musicClip = AudioSystem.getClip();
            musicClip.open(musicStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e1) {
            e1.printStackTrace();
        }

    }

    public void playProjectileFired() {
        if (!GameConstants.PLAY_SOUND_EFFECTS)
            return;

        // Find first available clip and start sound effect
        for (Clip shotClip : shotClips) {
            if (!shotClip.isRunning()) {
                shotClip.stop();
                shotClip.setMicrosecondPosition(0);
                shotClip.start();
                System.out.println("Play sound" + shotClip);
                break;
            }
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
