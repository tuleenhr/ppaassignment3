
/**
 * Write a description of class Berry here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Berry extends Plant {
    private static final double GROWTH_PROBABILITY = 0.15;  // 15% chance to grow each step
    private static final double SPREADING_PROBABILITY = 0.08;  // 8% chance to spread

    /**
     * Create a new berry plant
     */
    public Berry(Location location, boolean randomize) {
        super(location, randomize);
    }
    
    @Override
    protected double getGrowthProbability() {
        return GROWTH_PROBABILITY;
    }
    
    @Override
    protected double getSpreadingProbability() {
        return SPREADING_PROBABILITY;
    }
    
    @Override
    protected void createNewPlant(boolean randomize, Location location, Field field) {
        Berry berry = new Berry(location, randomize);
        field.placePlant(berry, location);
    }
}
