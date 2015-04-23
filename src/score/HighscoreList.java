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
    private ArrayList<HighscoreEntry> scoreList = null;

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
        try {
            OutputStream file = new FileOutputStream(GameConstants.HIGHSCORE_PATH);
            //OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(file);
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
        try {
            InputStream file = new FileInputStream(GameConstants.HIGHSCORE_PATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the List
            Iterable<HighscoreEntry> highscoreEntries = (Iterable<HighscoreEntry>) input.readObject();
            //display its data
            for (HighscoreEntry entry : highscoreEntries) {
                System.out.println("Recovered " + entry.getName());
            }
            scoreList = (ArrayList<HighscoreEntry>) highscoreEntries;
        }
        catch(ClassNotFoundException | IOException ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<HighscoreEntry> getScoreList(){
        return scoreList;
    }
}
