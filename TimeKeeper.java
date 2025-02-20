/**
 * Manages time progression in the simulation.
 * Handles day/night cycles and seasons, important for simulating realistic environmental changes.
 * @author Hamed Latif & Tuleen Rowaihy
 * @version 20.02.25
 */
public class TimeKeeper
{
    // Time tracking (each step is 12 hours), 2 steps = 1 day
    private static boolean isDay = true;  // true = day, false = night
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
     * Advance time by 12 hours.
     */
    public static void advanceTime() {
        isDay = !isDay; // Toggle between day and night
        if (isDay) {
            currentDay++;
            if (currentDay > 30) {
                currentDay = 1;
                currentMonth = (currentMonth + 1) % 12;
            }
        }
    }
    
    /**
     * Get time of day as string
     */
    public static String getTimeOfDay() {
        return isDay ? "Day" : "Night";
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
        return String.format("%d %s (%s) - %s", 
            currentDay, 
            months[currentMonth], 
            getCurrentSeason(),
            getTimeOfDay());
    }
}
