
/**
 * Represents the seasons in the simulation.
 * Each season affects animal behavior and plant growth.
 * @author (your name)
 * @version (a version number or a date)
 */
public class Season
{
    public static final String SPRING = "Spring";
    public static final String SUMMER = "Summer";
    public static final String AUTUMN = "Fall";
    public static final String WINTER = "Winter";
    
    // Plant growth modifiers for each season
    private static final double SPRING_GROWTH = 1.3;  // 30% boost
    private static final double SUMMER_GROWTH = 1.0;  // Normal
    private static final double AUTUMN_GROWTH = 0.8;  // 20% reduction
    private static final double WINTER_GROWTH = 0.5;  // 50% reduction
    
    /**
     * Get the current season name from a month number (0-11)
     */
    public static String getSeason(int month) {
        if(month >= 2 && month <= 4) {        // March, April, May
            return SPRING;
        }
        else if(month >= 5 && month <= 7) {   // June, July, August
            return SUMMER;
        }
        else if(month >= 8 && month <= 10) {  // September, October, November
            return AUTUMN;
        }
        else {                                // December, January, February
            return WINTER;
        }
    }
    
    /**
     * Get the growth modifier for a given season
     */
    public static double getGrowthModifier(String season) {
        if(season.equals(SPRING)) {
            return SPRING_GROWTH;
        }
        else if(season.equals(SUMMER)) {
            return SUMMER_GROWTH;
        }
        else if(season.equals(AUTUMN)) {
            return AUTUMN_GROWTH;
        }
        else if(season.equals(WINTER)) {
            return WINTER_GROWTH;
        }
        else {
            return SUMMER_GROWTH;  // Default to normal growth
        }
    }
    
    /**
     * Check if it's breeding season (Spring or Summer)
     */
    public static boolean isBreedingSeason(String season) {
        return season.equals(SPRING) || season.equals(SUMMER);
    }
    
    /**
     * Check if it's hibernation season (Winter)
     */
    public static boolean isHibernationSeason(String season) {
        return season.equals(WINTER);
    }
}
