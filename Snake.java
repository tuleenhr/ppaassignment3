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
public class Snake extends Animal
{
    // Characteristics shared by all snakes (class variables)
    private static final int BREEDING_AGE = 2;
    private static final int MAX_AGE = 10 * 365 * 2;
    private static final double BREEDING_PROBABILITY = 0.40;
    private static final int MAX_LITTER_SIZE = 5;
    private static final int MOUSE_FOOD_VALUE = 20;
    private static final int LIZARD_FOOD_VALUE = 25;
    
    protected static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param location The location within the field.
     */
    public Snake(boolean randomAge, Field field, Location location) {
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
            
            while(it.hasNext()) {
                Location where = it.next();
                Animal animal = field.getAnimalAt(where);
                
                // Only eat prey 50% of the time (reduce hunting efficiency)
                if (rand.nextDouble() < 0.5) {
                    if(animal instanceof Lizard && animal.isAlive()) {
                        animal.setDead();
                        eat(LIZARD_FOOD_VALUE);
                        return where;
                    }
                }
                else if(animal instanceof Mouse && animal.isAlive()) {
                    // Check if the mouse is infected
                    if (animal.isInfected() && Randomizer.getRandom().nextDouble() < PREDATOR_INFECTION_PROBABILITY) {
                        setInfected();  // 80% chance to get infected
                    }
                    animal.setDead();
                    eat(MOUSE_FOOD_VALUE);
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
        Snake young = new Snake(randomAge, field, location);
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
        return 30;
    }
    
    @Override
    protected boolean isActiveTime() {
        return TimeKeeper.isDaytime(); // Active during the day
    }
    
    @Override
    protected double getRestingProbability() {
        return 0.7;  // 70% chance to rest during non-active hours
    }
    
}
