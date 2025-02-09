import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Owl extends Animal
{
     // Characteristics shared by all bears (class variables)
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 100;
    private static final double BREEDING_PROBABILITY = 0.10;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int MOUSE_FOOD_VALUE = 6;

    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param location The location within the field.
     */
     public Owl(boolean randomAge, Location location) {
        super(location, randomAge);
    }
    
    /**
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    @Override
    protected Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        
        while(it.hasNext()) {
            Location where = it.next();
            Animal animal = field.getAnimalAt(where);
            
            if(animal instanceof Mouse && animal.isAlive()) {
                animal.setDead();
                setFoodLevel(MOUSE_FOOD_VALUE);
                return where;
            }
        }
        return null;
    }
    
    /**
     * Create a new bear.
     * @param randomAge Whether to create a bear with random age or as a baby.
     * @param location Where to create the bear.
     * @param field The field to place the bear in.
     */
    @Override
    protected void createYoung(boolean randomAge, Location location, Field field) {
        Owl young = new Owl(randomAge, location);
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
        return MOUSE_FOOD_VALUE;  // Maximum food value from eating a deer
    }
    
      @Override
    protected int getInitialFoodLevel() {
        // Random value between mouse food values
        return MOUSE_FOOD_VALUE;
    }
    
    @Override
    protected boolean isActiveTime() {
        return !isDaytime(); //Owls are nocturnal
    }
    
    @Override
    protected double getRestingProbability() {
        return 0.8;  // 80% chance to rest during non-active hours
    }
    
}
