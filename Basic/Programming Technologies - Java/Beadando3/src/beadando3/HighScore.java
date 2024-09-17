package beadando3;

public class HighScore {
    
    private final String name;
    private final int score;
    private final String timestamp;

    public HighScore(String name, int score, String timestamp) {
        this.name = name;
        this.score = score;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return name + ": " + score + " - " + timestamp;
    }
    

}