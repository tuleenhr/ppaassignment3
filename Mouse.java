import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Mouse extends Animal
{
     // Characteristics shared by all mice (class variables)
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 2 * 365 * 2;
    private static final double BREEDING_PROBABILITY = 0.70;
    private static final int MAX_LITTER_SIZE = 7;
    private static final int BERRY_FOOD_VALUE = 15;
    private static final boolean NOCTURNAL = true;

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
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
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
     * Eat food but only if hungry.
     * @param foodValue The amount of food gained.
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
        return !TimeKeeper.isDaytime(); // Active at night
    }
    
    @Override
    protected double getRestingProbability() {
        return 0.6;  // 60% chance to rest during non-active hours
    }
    
}
