package score;

import java.util.Comparator;

/**
 * Created by acrux on 2015-03-26.
 */
public class ScoreComparator implements Comparator<HighscoreEntry>{

    @Override
    public int compare(HighscoreEntry o1, HighscoreEntry o2) {
        if (o1.getScore() < o2.getScore()) return 1;
        else if (o1.getScore() == o2.getScore()) return 0;
        else return -1;
    }
}
