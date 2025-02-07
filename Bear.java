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
public class Bear extends Animal
{
    // Characteristics shared by all bears (class variables).
    // The age at which a bear can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a bear can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a bear breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single prey. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int DEER_FOOD_VALUE = 9;
    private static final int MOUSE_FOOD_VALUE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).

    // The bears's age.
    private int age;
    // The bears's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param location The location within the field.
     */
     public Bear(boolean randomAge, Location location) {
        super(location, randomAge);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            // Random food level between mouse and deer food values
            foodLevel = MOUSE_FOOD_VALUE + rand.nextInt(DEER_FOOD_VALUE - MOUSE_FOOD_VALUE + 1);
        }
        else {
            // New bears start with mouse food value (smaller amount)
            foodLevel = MOUSE_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState)
    {
        incrementAge();
        incrementHunger();
        
        if(isAlive()) {
            // Bears are most active at dawn and dusk
            int hour = getTimeOfDay();
            boolean isDawnOrDusk = (hour >= 5 && hour <= 8) || (hour >= 17 && hour <= 20);
            
            if(!isDawnOrDusk) {
                // 60% chance to rest during other times
                if(rand.nextDouble() < 0.6) {
                    return;
                }
            }
            
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
            
            Location nextLocation = findFood(currentField);
            
            // Check breeding only if well-fed
            if(foodLevel >= DEER_FOOD_VALUE/2 && !freeLocations.isEmpty()) {
                if(canBreed() && canFindMate(currentField)) {
                    giveBirth(nextFieldState, freeLocations);
                }
            }
            
            // Try to find food and move towards it or to a random location
            Location foodLocation = findFood(currentField);
            Location newLocation = foodLocation;
            if(newLocation == null && !freeLocations.isEmpty()) {
                newLocation = freeLocations.get(0);
            }
            
            if(newLocation != null) {
                setLocation(newLocation);
                nextFieldState.placeAnimal(this, newLocation);
            }
            else {
                setDead();
            }
        }
    }

    /**
     * Increase the age. This could result in the bears's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this fox more hungry. This could result in the bears's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for mice or deer adjacent to the current location.
     * Only the first live prey is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Animal animal = field.getAnimalAt(where);
            
            if(animal instanceof Deer && animal.isAlive()) {
                animal.setDead();
                foodLevel = DEER_FOOD_VALUE;
                return where;
            }
            else if(animal instanceof Mouse && animal.isAlive()) {
                animal.setDead();
                foodLevel = MOUSE_FOOD_VALUE;
                return where;
            }
        }
        return null;
    }
    
    /**
     * Check whether this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && ! freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Bear young = new Bear(false, loc);
                nextFieldState.placeAnimal(young, loc);
            }
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
