// src/HighScoreEntry.java
import java.io.Serializable;

public class HighScoreEntry implements Serializable, Comparable<HighScoreEntry> {
    private static final long serialVersionUID = 1L; // For serialization
    private String playerName;
    private int timeInSeconds; // Time taken to win

    public HighScoreEntry(String playerName, int timeInSeconds) {
        this.playerName = playerName;
        this.timeInSeconds = timeInSeconds;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    // Used for sorting the list. Lower time is better.
    @Override
    public int compareTo(HighScoreEntry other) {
        return Integer.compare(this.timeInSeconds, other.timeInSeconds);
    }

    @Override
    public String toString() {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%-20s %02d:%02d", playerName, minutes, seconds);
    }
}
