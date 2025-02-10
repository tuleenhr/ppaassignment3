
/**
 * Manages time progression in the simulation.
 * Handles day/night cycles and seasons.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class TimeKeeper
{
    // Time tracking (each step is 12 hours), 2 steps = 1 day
    private static boolean isDay = true;  // true = AM, false = PM
    private static int currentDay = 1;    // 1-30
    private static int currentMonth = 0;  // 0-11 (Jan-Dec)

    /**
     * Check if it's currently daytime (AM)
     */
    public static boolean isDaytime() {
        return isDay;
    }
    
    /**
     * Get current month (0-11)
     */
    public static int getCurrentMonth() {
        return currentMonth;
    }
    
    /**
     * Get current season
     */
    public static String getCurrentSeason() {
        return Season.getSeason(currentMonth);
    }
    
     /**
     * Advance time by 12 hours
     */
    public static void advanceTime() {
        // Toggle between AM and PM
        isDay = !isDay;
        
        // If we just switched to AM, advance the day
        if(isDay) {
            currentDay++;
            // If we've completed a month
            if(currentDay > 30) {
                currentDay = 1;
                currentMonth = (currentMonth + 1) % 12;
            }
        }
    }
    
    /**
     * Get time of day as string
     */
    public static String getTimeOfDay() {
        return isDay ? "AM" : "PM";
    }
    
    /**
     * Get current day of month
     */
    public static int getCurrentDay() {
        return currentDay;
    }
    
    /**
     * Get a formatted string of current time
     */
    public static String getTimeString() {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return String.format("Day %d %s (%s) - %s", 
            currentDay, 
            months[currentMonth], 
            getCurrentSeason(),
            getTimeOfDay());
    }
}
