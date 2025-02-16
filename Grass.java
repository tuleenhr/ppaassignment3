
/**
 * Write a description of class Grass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class Grass extends Plant {
    private static final double GROWTH_PROBABILITY = 0.70;   // 20% chance to grow each step
    private static final double SPREADING_PROBABILITY = 0.50; // 10% chance to spread
    
    /**
     * Create a new grass plant
     */
    public Grass(Location location, boolean randomize) {
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
        Grass grass = new Grass(location, randomize);
        field.placePlant(grass, location);
    }
}

