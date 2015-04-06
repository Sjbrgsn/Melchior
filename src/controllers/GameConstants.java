package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by acrux on 2015-03-18.
 * Holds all global constants mainly used in the game mechanics
 */
public final class GameConstants {
    public static final int ENEMY_SPAWN_DELAY;
    public static final int BUILD_STATE_TIME;
    public static final double DIFFICULTY_INCREASE_FACTOR;
    public static final int GRID_SIZE;
    public static final int GAME_TICK_DELAY;

    public static final int STARTING_DIFFICULTY;
    public static final int STARTING_CASH;
    public static final int STARTING_HEALTH;

    public static final String HIGHSCORE_PATH = "highscore.ser";

    public static boolean PLAY_MUSIC;
    public static boolean PLAY_SOUND_EFFECTS;

    public static boolean RESIZABLE;

    static {
        // Default values
        int delay = 30;
        int buildTime = 500;
        double difficultyIncrease = 2.5;
        int gridSize = 20;
        int tickDelay = 100/3;

        int startingDifficulty = 100;
        int startingCash = 300;
        int startingHealth = 20;

        boolean playMusic = true;
        boolean playSound = true;

        boolean resizable = true;

        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("constants.properties");
            prop.load(input);

            delay = Integer.parseInt(prop.getProperty("ENEMY_SPAWN_DELAY"));
            buildTime = Integer.parseInt(prop.getProperty("BUILD_STATE_TIME"));
            difficultyIncrease = Double.parseDouble(prop.getProperty("DIFFICULTY_INCREASE_FACTOR"));
            gridSize = Integer.parseInt(prop.getProperty("GRID_SIZE"));
            tickDelay = Integer.parseInt(prop.getProperty("GAME_TICK_DELAY"));

            startingDifficulty = Integer.parseInt(prop.getProperty("STARTING_DIFFICULTY"));
            startingCash = Integer.parseInt(prop.getProperty("STARTING_CASH"));
            startingHealth = Integer.parseInt(prop.getProperty("STARTING_HEALTH"));

            playMusic = Boolean.parseBoolean(prop.getProperty("PLAY_MUSIC"));
            playSound = Boolean.parseBoolean(prop.getProperty("PLAY_SOUND_EFFECTS"));

            resizable = Boolean.parseBoolean(prop.getProperty("RESIZABLE"));

        }catch (IOException e){
            e.printStackTrace();


        }finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ENEMY_SPAWN_DELAY = delay;
        BUILD_STATE_TIME = buildTime;
        DIFFICULTY_INCREASE_FACTOR = difficultyIncrease;
        GRID_SIZE = gridSize;
        GAME_TICK_DELAY = tickDelay;
        STARTING_DIFFICULTY = startingDifficulty;
        STARTING_CASH = startingCash;
        STARTING_HEALTH = startingHealth;
        PLAY_MUSIC = playMusic;
        PLAY_SOUND_EFFECTS = playSound;
        RESIZABLE = resizable;
    }
}
