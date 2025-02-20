import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * The forest isn't always sunny.
 * This class represents the weather, specifically if it is raining.
 * The chance of it raining depends on the current season.
 * @author Hamed Latif & Tuleen Rowaihy
 * @version 20.02.25
 */
public class Weather {
    private static final double WINTER_RAIN_CHANCE = 0.7; // 70% in winter
    private static final double SUMMER_RAIN_CHANCE = 0.3; // 30% in summer
    private static final double DEFAULT_RAIN_CHANCE = 0.5; // 50% in spring and autumn
    private boolean raining;
    private Random rand;

    public Weather() {
        this.rand = new Random();
        this.raining = false; // random object for raining probability
    }

    /**
     * Update the weather based on the current season.
     * The chance of it raining is set depending on the season.
     * @param currentSeason The current season as a string.
     */
    public void updateWeather(String currentSeason) {
        double rainProbability;
        if (currentSeason.equals(Season.WINTER)) {
            rainProbability = WINTER_RAIN_CHANCE;
        } else if (currentSeason.equals(Season.SUMMER)) {
            rainProbability = SUMMER_RAIN_CHANCE;
        } else {
            rainProbability = DEFAULT_RAIN_CHANCE;
        }
        raining = rand.nextDouble() < rainProbability;
    }

    /**
     * Check if it is currently raining.
     * @return true if it is raining.
     */
    public boolean isRaining() {
        return raining;
    }
}
