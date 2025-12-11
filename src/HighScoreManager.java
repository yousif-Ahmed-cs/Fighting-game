// src/HighScoreManager.java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreManager {
    private static final String HIGHSCORE_FILE = "highscores.dat";

    // Load scores from file
    @SuppressWarnings("unchecked")
    public static List<HighScoreEntry> loadScores() {
        List<HighScoreEntry> scores = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE))) {
            scores = (List<HighScoreEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("High score file not found. A new one will be created.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }
        return scores;
    }

    // Save scores to file
    public static void saveScores(List<HighScoreEntry> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }
}
