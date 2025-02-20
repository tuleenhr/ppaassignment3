import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Models a Bear in the ecosystem simulation.
 * Bears are apex predators that hunt both deer and mice, with deer being their preferred prey.
 * They are diurnal (active during day) and require significant food to survive.
 * Bears can be infected by eating diseased prey and have high breeding requirements.
 * 
 * Key characteristics:
 * - Diurnal (active during daytime)
 * - Hunts deer (food value: 60) and mice (food value: 25)
 * - 50% chance to rest during night
 * - Requires male and female for breeding
 * - Can be infected through eating diseased prey
 * 
 * @author Tuleen Rowaihy & Hamed Latif
 * @version 7.1
 */
public class Bear extends Animal
{
     // Characteristics shared by all bears (class variables)
    private static final int BREEDING_AGE = 4;
    private static final int MAX_AGE = 8;
    private static final double BREEDING_PROBABILITY = 0.20;
    private static final int MAX_LITTER_SIZE = 5;
    private static final int DEER_FOOD_VALUE = 60;
    private static final int MOUSE_FOOD_VALUE = 25;
    
    protected static final Random rand = Randomizer.getRandom();

    /**
     * Create a new bear. Bears can be created as newborn (age zero and not hungry)
     * or with random age and hunger level.
     * 
     * @param randomAge If true, the bear starts with random age and hunger.
     * @param field The field currently being used.
     * @param location The location within the field.
     */
    public Bear(boolean randomAge, Field field, Location location) {
        super(location, randomAge, BREEDING_AGE, MAX_AGE);
    }
    
     /**
     * Look for prey adjacent to the current location.
     * Bears prioritize deer over mice and only hunt when hungry.
     * Implements 50% hunting efficiency reduction and disease transmission.
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
                    // Try to find a deer first (more food value)
                    if(animal instanceof Deer && animal.isAlive()) {
                        animal.setDead();
                        eat(DEER_FOOD_VALUE);
                        return where;
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
        }
        return null;
    }
    
    /**
     * Make the bear eat prey.
     * Bears only eat when very hungry (below 25% food level).
     * 
     * @param foodValue The amount of food gained from eating prey.
     */
    protected void eat(int foodValue) {
        if (getFoodLevel() < getMaxFoodValue() / 4) { // Only eat when food level is below 25%
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
        Bear young = new Bear(randomAge, field, location);
        field.placeAnimal(young, location);
    }
    
    // Implementation of abstract methods with behavioral characteristics
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
        return DEER_FOOD_VALUE;  // Maximum food value from eating a deer
    }
    
      @Override
    protected int getInitialFoodLevel() {
        return 40;
    }
    
    @Override
    protected boolean isActiveTime() {
        return TimeKeeper.isDaytime(); // Dinural (Active during the day)
    }   
    
    @Override
    protected double getRestingProbability() {
        return 0.5;  // 50% chance to rest during non-active hours
    }
    
}
