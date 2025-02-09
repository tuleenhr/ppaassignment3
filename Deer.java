import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Deer extends Animal
{
     // Characteristics shared by all deer (class variables)
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 2;
    private static final int GRASS_FOOD_VALUE = 6;
    private static final int BERRY_FOOD_VALUE = 4;  // Berries provide less food than grass

    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param location The location within the field.
     */
     public Deer(boolean randomAge, Location location) {
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
        
        // First, check if there are any predators nearby
        boolean bearNearby = false;
        for(Location loc : adjacent) {
            Animal animal = field.getAnimalAt(loc);
            if(animal instanceof Bear) {
                bearNearby = true;
                break;
            }
        }
        
        // If a bear is nearby, skip eating unless very hungry
        if(bearNearby && getFoodLevel() > GRASS_FOOD_VALUE/2) {
            return null;
        }
        
        // First try to find grass (preferred food)
        while(it.hasNext()) {
            Location where = it.next();
            Plant plant = field.getPlantAt(where);
            
            if(plant instanceof Grass && plant.isAlive()) {
                plant.setDead();
                setFoodLevel(GRASS_FOOD_VALUE);
                return where;
            }
        }
        
        // If no grass found, look for berries
        it = adjacent.iterator();  // Reset iterator
        while(it.hasNext()) {
            Location where = it.next();
            Plant plant = field.getPlantAt(where);
            
            if(plant instanceof Berry && plant.isAlive()) {
                plant.setDead();
                setFoodLevel(BERRY_FOOD_VALUE);
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
        Deer young = new Deer(randomAge, location);
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
        return GRASS_FOOD_VALUE;  // Maximum food value from eating a deer
    }
    
    @Override
    protected int getInitialFoodLevel() {
        return GRASS_FOOD_VALUE;
    }
    
    @Override
    protected boolean isActiveTime() {
    int hour = getTimeOfDay();
    // Deers are most active at dawn (5-8) and dusk (17-20)
    return (hour >= 5 && hour <= 8) || (hour >= 17 && hour <= 20);    }
    
    @Override
    protected double getRestingProbability() {
        return 0.5;  // 50% chance to rest during non-active hours
    }
    
}
