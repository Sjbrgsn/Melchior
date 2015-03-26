package score;

import java.io.Serializable;

/**
 * Created by acrux on 2015-03-26.
 */
public class HighscoreEntry implements Serializable{
    private String name;
    private int score;

    public HighscoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
