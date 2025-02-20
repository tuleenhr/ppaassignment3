import java.util.List;
import java.util.Iterator;

/**
 * Models a Lizard in the ecosystem simulation.
 * Lizards are unique as they reproduce asexually and specialize in eating berries.
 * They are prey for both owls and snakes, active during day.
 * Implements simplified breeding due to asexual reproduction.
 * 
 * Key characteristics:
 * - Diurnal (active during day)
 * - Asexual reproduction (no mate needed)
 * - Feeds exclusively on berries (food value: 30)
 * - Prey for owls and snakes
 * - 30% chance to rest at night
 * 
 * @author Tuleen Rowaihy & Hamed Latif
 * @version 7.1
 */
public class Lizard extends Animal
{
     // Characteristics shared by all deer (class variables)
    private static final int BREEDING_AGE = 3;
    private static final int MAX_AGE = 7;
    private static final double BREEDING_PROBABILITY = 0.30;
    private static final int MAX_LITTER_SIZE = 1;
    private static final int BERRY_FOOD_VALUE = 30;  
    private static final boolean NOCTURNAL = false; 
    
     /**
     * Create a new lizard.
     * 
     * @param randomAge If true, starts with random age and hunger.
     * @param field The field currently being used.
     * @param location The location within the field.
     */
    public Lizard(boolean randomAge, Field field, Location location) {
        super(location, randomAge, BREEDING_AGE, MAX_AGE);
    }
    
    /**
     * Look for food around the lizard.
     * Checks for predators before feeding and only eats berries.
     * Will skip eating to avoid predators unless very hungry.
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
     * Create a new baby lizard through asexual reproduction.
     * 
     * @param randomAge Whether to create with random age.
     * @param location Where to place the baby.
     * @param field The field to place the baby in.
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
    
    // Special methods for asexual reproduction
    @Override
    protected boolean canFindMate(Field field) {
        return true;  // No mate needed
    }
    
    @Override
    protected boolean reproducesSexually() {
        return false;
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
        return 0.3;  // 50% chance to rest during non-active hours
    }
    
}
