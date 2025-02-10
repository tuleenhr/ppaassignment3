import java.util.List;
import java.util.Random;

/**
 * Common elements of foxes and rabbits.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Plant
{
    private static final Random rand = Randomizer.getRandom();
    
    private boolean alive;
    private Location location;
    private int growthStage;    // The growth stage (0 = seed, 1 = growing, 2 = mature)
    
    /**
     * Create a new plant
     * @param location The plant's location
     * @param randomize If true, starts mature
     */
    public Plant(Location location, boolean randomize) {
        this.alive = true;
        this.location = location;
        this.growthStage = randomize ? 2 : 0;  // Start mature if random, otherwise seed
    }
    
    /**
     * Make this plant act: grow and possibly spread seeds.
     */
    public void act(Field currentField, Field nextFieldState) {
        if(isAlive()) {
            // Only spread seeds if mature
            if(growthStage == 2 && TimeKeeper.isDaytime()) {
                spreadSeeds(nextFieldState);
            }
            // Try to grow if not mature
            else if(growthStage < 2 && TimeKeeper.isDaytime()) {
                // Apply seasonal growth modifier
                double modifier = Season.getGrowthModifier(TimeKeeper.getCurrentSeason());
                if(rand.nextDouble() < getGrowthProbability() * modifier) {
                    grow();
                }
            }
            
            // Stay in the same location if still alive
            if(isAlive()) {
                nextFieldState.placePlant(this, getLocation());
            }
        }
    }
    
    /**
     * Check whether the plant is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive() {
        return alive && growthStage == 2;  // Only considered "alive" (for eating) when mature
    }

    /**
     * Indicate that the animal is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Try to grow to next stage
     */
    protected void grow() {
            growthStage++;
    }
    
    /**
     * Try to spread seeds to adjacent locations
     */
    protected void spreadSeeds(Field field) {
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        
        // Modify spreading based on season
        double modifier = Season.getGrowthModifier(TimeKeeper.getCurrentSeason());
        
        if(!free.isEmpty() && rand.nextDouble() < getSpreadingProbability() * modifier) {
            Location loc = free.get(0);
            createNewPlant(false, loc, field);
        }
    }
    
    // Abstract methods for specific plant types
    protected abstract double getGrowthProbability();
    protected abstract double getSpreadingProbability();
    protected abstract void createNewPlant(boolean randomize, Location location, Field field);
    
}
