import java.util.Random;

/**
 * Write a description of class Weather here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class Weather 
{
    private static final double WINTER_RAIN_CHANCE = 0.7; // 70% in winter
    private static final double SUMMER_RAIN_CHANCE = 0.3; // 30% in summer
    private static final double DEFAULT_RAIN_CHANCE = 0.5; // 50% in spring and autumn

    private boolean raining;
    private Random rand;

    public Weather() {
        this.rand = new Random();
        this.raining = false;
    }

    /**
     * Update the weather based on the current season (String-based).
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
     */
    public boolean isRaining() {
        return raining;
    }
}


