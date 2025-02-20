import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Models a Mouse in the ecosystem simulation.
 * Mice are small prey animals that eat berries and are hunted by multiple predators.
 * They are nocturnal and serve as disease vectors in the ecosystem.
 * They have high breeding rates but face significant predation pressure.
 * 
 * Key characteristics:
 * - Nocturnal (active during night)
 * - Feeds on berries (food value: 15)
 * - Can carry and transmit disease (10% chance at birth)
 * - High breeding rate with large litter size
 * - Avoids predators (bears, owls, snakes)
 * 
 * @author Tuleen Rowaihy & Hamed Latif
 * @version 7.1
 */
public class Mouse extends Animal
{
     // Characteristics shared by all mice (class variables)
    private static final int BREEDING_AGE = 9;
    private static final int MAX_AGE = 12;
    private static final double BREEDING_PROBABILITY = 0.18;
    private static final int MAX_LITTER_SIZE = 8;
    private static final int BERRY_FOOD_VALUE = 15;

    /**
     * Create a mouse. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param location The location within the field.
     */
    public Mouse(boolean randomAge, Field field, Location location) {
        super(location, randomAge, BREEDING_AGE, MAX_AGE);
        if (Randomizer.getRandom().nextDouble() < 0.1) { // 10% chance to be infected at birth
            setInfected();
        }
    }
    
    /**
     * Look for food around the mouse.
     * Checks for nearby predators first and may skip eating if predators are present.
     * Only eats berries and must manage predator avoidance.
     * 
     * @param field The field currently occupied.
     * @return Where food was found, or null if none found.
     */
    @Override
    protected Location findFood(Field field) {
        if (getFoodLevel() < getMaxFoodValue() / 2) { // Only search for food if hungry
            List<Location> adjacent = field.getAdjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            
            // First, check if there are any predators nearby
            boolean dangerNearby = false;
            for(Location loc : adjacent) {
                Animal animal = field.getAnimalAt(loc);
                if(animal instanceof Bear || animal instanceof Owl || animal instanceof Snake) {
                    dangerNearby = true;
                    break;
                }
            }
            
            // If danger is nearby, mice might skip eating to move to safety
            if(dangerNearby && getFoodLevel() > BERRY_FOOD_VALUE/2) {
                return null;
            }
            
            // Look for food if hungry enough or no danger
            while(it.hasNext()) {
                Location where = it.next();
                Plant plant = field.getPlantAt(where);
                
                if(plant instanceof Berry && plant.isAlive()) {
                    plant.setDead();
                    eat(BERRY_FOOD_VALUE);
                    return where;
                }
            }
        }
        return null;
    }
    
     /**
     * Make the mouse eat food.
     * Only eats when below 50% food level to maintain realistic behavior.
     * 
     * @param foodValue Amount of food (nutrition) to add.
     */
    protected void eat(int foodValue) {
        if (getFoodLevel() < getMaxFoodValue() / 2) { // Only eat when food level is below 50%
            setFoodLevel(getFoodLevel() + foodValue);
        }
    }
    
    /**
     * Create a new mouse.
     * @param randomAge Whether to create a bear with random age or as a baby.
     * @param location Where to create the bear.
     * @param field The field to place the bear in.
     */
    @Override
    protected void createYoung(boolean randomAge, Location location, Field field) {
        Mouse young = new Mouse(randomAge, field, location);
        field.placeAnimal(young, location);
    }
    
    // Implementation of abstract methods
    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }
    
    @Override
    protected int getBreedingAge() {
        return BREEDING_AGE;
    }
    
    @Override
    protected double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }
    
    @Override
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
    
    @Override
    protected int getMaxFoodValue() {
        return BERRY_FOOD_VALUE;  // Maximum food value from eating a deer
    }
    
    @Override
    protected int getInitialFoodLevel() {
        return 15;
    }
    
    @Override
    protected boolean isActiveTime() {
        return !TimeKeeper.isDaytime(); // Nocturnal (Active at night)
    }
    
    @Override
    protected double getRestingProbability() {
        return 0.7;  // 70% chance to rest during non-active hours
    }
    
}