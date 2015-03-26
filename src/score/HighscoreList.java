package score;

import controllers.GameConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by acrux on 2015-03-26.
 */
public class HighscoreList {
    ArrayList<HighscoreEntry> scoreList;

    public HighscoreList() {
        this.scoreList = new ArrayList<>();
    }

    public void add(String name, int score)  {
        HighscoreEntry entry = new HighscoreEntry(name, score);
        scoreList.add(entry);
        sort();

    }

    private void sort() {
        Collections.sort(scoreList, new ScoreComparator());
    }

    public void saveToFile() {
        //serialize the List
        try {
            OutputStream file = new FileOutputStream(GameConstants.HIGHSCORE_PATH);
            System.out.println(file.toString());
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(scoreList);
        }
        catch(IOException ex) {
            //Logger.log(Level.SEVERE, "Cannot perform output.", ex);
            // TODO add logging
            System.out.println("error");
        }
    }

    public ArrayList<HighscoreEntry> getScoreList(){
        return scoreList;
    }
}
