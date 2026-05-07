public class SnakeScoreManager {

    private static final java.util.prefs.Preferences PREFS =
        java.util.prefs.Preferences.userNodeForPackage(SnakeScoreManager.class);
    private static final String HIGH_SCORE_KEY = "snake_high_score";
 
    // Level configuration
    private static final int[] LEVEL_THRESHOLDS = { 5, 10, 15, 22, 30, 40, 52, 65, 80 };
    private static final int BASE_SPEED_MS = 300;  // initial speed
    private static final int SPEED_STEP_MS = 25;  
    private static final int MIN_SPEED_MS = 80; 
 
    // status
    private int currentScore = 0;
    private int highScore;
    private int level = 1;
 
    private Runnable onScoreChanged;
    private Runnable onLevelUp;

    public SnakeScoreManager() {
        highScore = PREFS.getInt(HIGH_SCORE_KEY, 0);
    }
 

    //reset but highest score
    public void reset() {
        currentScore = 0;
        level = 1;
        fireScoreChanged();
    }
 
    //add score when get food
    public void addScore(int base) {
        currentScore += base * level;  
        if (currentScore > highScore) {
            highScore = currentScore;
        }
        fireScoreChanged();
    }
 

    public void incrementLevel(int snakeLength) {
        int newLevel = 1;
        for (int threshold : LEVEL_THRESHOLDS) {
            if (snakeLength > threshold) newLevel++;
            else break;
        }
        if (newLevel > level) {
            level = newLevel;
            if (onLevelUp != null) onLevelUp.run();
            fireScoreChanged();
        }
    }
 
    //record highest score
    public void updateHighScore() {
        PREFS.putInt(HIGH_SCORE_KEY, highScore);
    }
 
    //clear highest score manually
    public void clearHighScore() {
        highScore = 0;
        PREFS.remove(HIGH_SCORE_KEY);
        fireScoreChanged();
    }
 
    // Returns the tick interval (ms) for the current level
    public int getTickIntervalMs() {
        int speed = BASE_SPEED_MS - (level - 1) * SPEED_STEP_MS;
        return Math.max(speed, MIN_SPEED_MS);
    }
 
    public int getCurrentScore() { 
        return currentScore; 
    }

    public int getHighScore(){
         return highScore;
    }
    
    public int getLevel(){
        return level;
    }
 
    public void setOnScoreChanged(Runnable cb) { this.onScoreChanged = cb; }
    public void setOnLevelUp(Runnable cb){ this.onLevelUp = cb; }
 
    
    private void fireScoreChanged() {
        if (onScoreChanged != null) onScoreChanged.run();
    }
}
