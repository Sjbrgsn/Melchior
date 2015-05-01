package score;

import controllers.GameConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by acrux on 2015-03-26.
 */
public class HighscoreList {
    private List<HighscoreEntry> scoreList = null;

    public HighscoreList() {
        File file = new File(GameConstants.HIGHSCORE_PATH);
        if (file.exists() && !file.isDirectory()){
            System.out.println("Loading file");
            loadFromFile();
        }
        else {
            System.out.println("No file, creating new list");
            this.scoreList = new ArrayList<>();
        }

    }

    public void add(String name, int score)  {
        HighscoreEntry entry = new HighscoreEntry(name, score);
        scoreList.add(entry);
        sort();
        while (scoreList.size() > 10){
            scoreList.remove(scoreList.size()-1);
        }

    }

    private void sort() {
        Collections.sort(scoreList, new ScoreComparator());
    }

    public void saveToFile() {
        //serialize the List
        try (ObjectOutput output = new ObjectOutputStream(new FileOutputStream(GameConstants.HIGHSCORE_PATH))){
            output.writeObject(scoreList);
        }
        catch(IOException e) {
            //Logger.log(Level.SEVERE, "Cannot perform output.", ex);
            // TODO add logging
            e.printStackTrace();
        }
    }

    private void loadFromFile(){
        //deserialize file
        try (ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(GameConstants.HIGHSCORE_PATH)))){
            //deserialize the List
            Iterable<HighscoreEntry> highscoreEntries = (Iterable<HighscoreEntry>) input.readObject();
            //display its data
            for (HighscoreEntry entry : highscoreEntries) {
                System.out.println("Recovered " + entry.getName());
            }
            scoreList = (List<HighscoreEntry>) highscoreEntries;
        }
        catch(ClassNotFoundException | IOException ex){
            ex.printStackTrace();
        }
    }

    public List<HighscoreEntry> getScoreList(){
        return scoreList;
    }
}
