import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Models a Snake in the ecosystem simulation.
 * Snakes are nocturnal predators that compete with owls for mice and lizards.
 * They have a high resting probability during the day and can become infected from prey.
 * 
 * Key characteristics:
 * - Nocturnal (active during night)
 * - Hunts mice (food value: 20) and lizards (food value: 25)
 * - 80% chance to rest during day
 * - Can be infected through eating diseased mice
 * - Reduced hunting efficiency (50% success rate)
 * 
 * @author Tuleen Rowaihy & Hamed Latif
 * @version 7.1
 */
public class Snake extends Animal
{
    // Characteristics shared by all snakes (class variables)
    private static final int BREEDING_AGE = 4;
    private static final int MAX_AGE = 9;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 5;
    private static final int MOUSE_FOOD_VALUE = 20;
    private static final int LIZARD_FOOD_VALUE = 25;
    
    protected static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a new snake.
     * 
     * @param randomAge If true, starts with random age and hunger.
     * @param field The field currently being used.
     * @param location The location within the field.
     */
    public Snake(boolean randomAge, Field field, Location location) {
        super(location, randomAge, BREEDING_AGE, MAX_AGE);
    }
    
    /**
     * Look for prey adjacent to the current location.
     * Snakes prefer lizards over mice and implement hunting efficiency reduction.
     * Can become infected from eating infected mice.
     * 
     * @param field The field currently occupied.
     * @return Where food was found, or null if none found.
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
     * Make the snake eat prey.
     * Only eats when very hungry (below 25% food level).
     * 
     * @param foodValue Amount of food (nutrition) to add.
     */
    protected void eat(int foodValue) {
        if (getFoodLevel() < getMaxFoodValue() / 4) { // Only eat when food level is below 25%
            setFoodLevel(getFoodLevel() + foodValue);
        }
    }
    
    /**
     * Create a new snake.
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
        return !TimeKeeper.isDaytime(); // Nocturnal (Active during the day)
    }
    
    @Override
    protected double getRestingProbability() {
        return 0.8;  // 80% chance to rest during non-active hours
    }
    
}