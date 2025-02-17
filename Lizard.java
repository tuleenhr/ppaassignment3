import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Lizard extends Animal
{
     // Characteristics shared by all deer (class variables)
<<<<<<< HEAD
    private static final int BREEDING_AGE = 3;
    private static final int MAX_AGE = 6;
    private static final double BREEDING_PROBABILITY = 0.30;
=======
    private static final int BREEDING_AGE = 2;
    private static final int MAX_AGE = 4 * 365 * 2;
    private static final double BREEDING_PROBABILITY = 0.1;
>>>>>>> e4177dceb871ae3659bfb108ef9bb7774622d6b1
    private static final int MAX_LITTER_SIZE = 1;
    private static final int BERRY_FOOD_VALUE = 30;  
    private static final boolean NOCTURNAL = false; 
    
    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param location The location within the field.
     */
    public Lizard(boolean randomAge, Field field, Location location) {
        super(location, randomAge, BREEDING_AGE, MAX_AGE);
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
                if(animal instanceof Owl || animal instanceof Snake) {
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
     * Create a new bear.
     * @param randomAge Whether to create a bear with random age or as a baby.
     * @param location Where to create the bear.
     * @param field The field to place the bear in.
     */
    @Override
    protected void createYoung(boolean randomAge, Location location, Field field) {
        Lizard young = new Lizard(randomAge, field, location);
        field.placeAnimal(young, location);
    }
    
    // Implementation of abstract methods
    @Override
    protected boolean canFindMate(Field field) {
        return true;  // No mate needed
    }
    
    @Override
    protected boolean reproducesSexually() {
        return false;
    }
    
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
        return BERRY_FOOD_VALUE;  //
    }
    
    @Override
    protected int getInitialFoodLevel() {
        return 40;
    }
    
    @Override
    protected boolean isActiveTime() {
        return TimeKeeper.isDaytime(); // Active during the day
    }
    
    @Override
    protected double getRestingProbability() {
<<<<<<< HEAD
        return 0.3;  // 50% chance to rest during non-active hours
    }
    
}
=======
        return 0.2;  // 20% chance to rest during non-active hours
    }
    
}
>>>>>>> e4177dceb871ae3659bfb108ef9bb7774622d6b1
