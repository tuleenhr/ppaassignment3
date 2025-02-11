import java.util.List;
import java.util.Random;

/**
 * Common elements of foxes and rabbits.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Animal
{
    private static final Random rand = Randomizer.getRandom();
    
    // Instance variables
    private boolean alive;
    private Location location;
    private final boolean isMale;
    private int age;
    private int foodLevel;
    private int breedingAge;
    private boolean infected = false;  // tracks if the animal is infected
    private boolean justInfected = false;  // track new infections
    protected static final double PREDATOR_INFECTION_PROBABILITY = 0.8;  // 80% chance of infection when eating an infected mouse
    private static final double PREDATOR_DEATH_PROBABILITY = 0.4;  // 70% chance to die next cycle if infected

    
    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     * @param randomize A boolean to determine random gender assignment
     */
    public Animal(Location location, boolean randomize, int breedingAge, int lifespan) {
        this.alive = true;
        this.location = location;
        this.breedingAge = breedingAge;

        if (randomize) {
            this.isMale = Randomizer.getRandom().nextBoolean();
            this.age = Randomizer.getRandom().nextInt(getMaxAge()); // Random starting age
            this.foodLevel = getInitialFoodLevel(); // Ensure animal starts with food
        } else {
            this.isMale = false; // Default to female if not randomized
            this.age = 0; // Start at birth
            this.foodLevel = getMaxFoodValue(); // Full food if not randomized
        }   
    }
    
    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    public void act(Field currentField, Field nextFieldState) {
        incrementAge();
        incrementHunger();
        handleDisease();    // kill newly infected predators 70% of the time
        
        if(isAlive()) {
            // Check if active based on time of day
            if(!isActiveTime()) {
                if(rand.nextDouble() < getRestingProbability()) {
                    return;  // Rest during inactive period
                }
            }
            
            // Get possible moves and look for food
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
            Location foodLocation = findFood(currentField);
            
            // Try to breed if well-fed
            if(foodLevel >= getMaxFoodValue()/2 && !freeLocations.isEmpty()) {
                if(canBreed() && canFindMate(currentField)) {
                    giveBirth(nextFieldState, freeLocations);
                }
            }
            
            // Move to food or random location
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
    
    protected boolean isBreedingSeason() {
        return Season.isBreedingSeason(TimeKeeper.getCurrentSeason());
    }
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Set the animal's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }
    
    public boolean isMale() {
        return isMale;
    }
    
    /**
     * Make the animal get hungrier.
     */
    protected void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Make the animal older.
     */
    protected void incrementAge() {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Set the animal's food level.
     */
    protected void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }
    
    /**
     * Get the animal's current food level.
     */
    protected int getFoodLevel() {
        return foodLevel;
    }
    
    /**
     * Get the animal's current age.
     */
    protected int getAge() {
        return age;
    }
    
    protected boolean canFindMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal animal = field.getAnimalAt(loc);
            if (animal != null 
                && animal.isAlive() 
                && animal.getClass() == this.getClass()
                && animal.isMale() != this.isMale()
                && animal.canBreed()) {
                return true;
            }
        }
        return false;
    }
    
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations) {
        int births = breed();
        for(int b = 0; b < births && !freeLocations.isEmpty(); b++) {
            Location loc = freeLocations.remove(0);
            createYoung(false, loc, nextFieldState);
        }
    }
    
    /**
     * Generate a birth number.
     */
    private int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    /**
     * Check if animal can breed.
     */
    protected boolean canBreed() {
        return age >= getBreedingAge();
    }
    
    /**
     * Infect this animal.
     */
    public void setInfected() {
        this.infected = true;
        this.justInfected = true;  // mark animal as just infected
    }
    
    /**
     * Check if the animal is infected.
     */
    public boolean isInfected() {
        return infected;
    }
    
    /**
     * Handle diseases: If a predator that just got infected, 
     * there is a 70% chance it dies immediately. 
     * If not, heals after one cycle
     */
    private void handleDisease() {
        if (infected && justInfected) {
            justInfected = false;  // reset flag so the death chance applies only once
            if (rand.nextDouble() < PREDATOR_DEATH_PROBABILITY) {
                setDead();
            }
        }
    }


    // Abstract methods to be implemented by specific animals
    protected abstract Location findFood(Field field);
    protected abstract int getMaxAge();
    protected abstract int getBreedingAge();
    protected abstract double getBreedingProbability();
    protected abstract int getMaxLitterSize();
    protected abstract int getMaxFoodValue();
    protected abstract int getInitialFoodLevel();
    protected abstract boolean isActiveTime();
    protected abstract double getRestingProbability();
    protected abstract void createYoung(boolean randomAge, Location location, Field field);
    
}
