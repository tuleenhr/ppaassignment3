import java.util.List;
import java.util.Random;

/**
 * Abstract class representing common elements of all animals in the ecosystem simulation.
 * This class provides the base functionality for all animal types including:
 * - Basic life cycle (birth, aging, death)
 * - Food and hunger management
 * - Movement and location tracking
 * - Breeding behaviors
 * - Disease mechanics
 * - Activity cycles (day/night)
 *
 * @author David J. Barnes, Michael Kölling, Tuleen Rowaihy, Hamed Latif
 * @version 8.0
 */
public abstract class Animal
{
    // Random number generator for probabilistic behaviors
    private static final Random rand = Randomizer.getRandom();
    
    // Core animal characteristics
    private boolean alive;         // Whether the animal is currently alive
    private Location location;     // Current position in the field
    private final boolean isMale;  // Gender of the animal (affects breeding)
    private int age;              // Current age in steps
    private int foodLevel;        // Current food level (0 = starving)
    private int breedingAge;      // Age at which the animal can start breeding
    
    // Disease tracking variables
    private boolean infected = false;      // Current infection status
    private boolean justInfected = false;  // Tracks new infections for immediate effects
    
    // Disease-related probabilities
    protected static final double PREDATOR_INFECTION_PROBABILITY = 0.8;  // Chance of infection when eating infected prey
    private static final double PREDATOR_DEATH_PROBABILITY = 0.4;        // Chance of death after infection
    
    /**
     * Creates a new animal with specified characteristics.
     * If randomized, the animal starts with random age and gender.
     * If not randomized, starts as a newborn female.
     * 
     * @param location The animal's starting location
     * @param randomize Whether to randomize initial characteristics
     * @param breedingAge Age at which the animal can start breeding
     * @param lifespan Maximum possible age of the animal
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
     * Main action method called each simulation step.
     * Handles:
     * - Aging and hunger
     * - Disease effects
     * - Movement and hunting
     * - Breeding
     * 
     * @param currentField Current state of the field
     * @param nextFieldState Next state being built
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
            if(foodLevel >= getMaxFoodValue()/4 && !freeLocations.isEmpty()) {
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
        int searchRadius = 1; // breeding distances
    
        for (int i = 0; i < searchRadius; i++) {
            List<Location> nearbyLocations = field.getAdjacentLocations(getLocation());
    
            for (Location loc : nearbyLocations) {
                Animal potentialMate = field.getAnimalAt(loc);
                if (potentialMate != null && potentialMate.getClass() == this.getClass() && potentialMate.isAlive()) {
                    return true; // Found a mate within range
                }
            }
        }
        return false; // No mate found
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
        if(canBreed()) {
            if(!reproducesSexually()) {
                // Asexual reproduction - just check probability
                if(rand.nextDouble() <= getBreedingProbability()) {
                    births = 1;  // Asexual animals have one offspring
                }
            }
            else {
                // Sexual reproduction - probability determines litter size
                if(rand.nextDouble() <= getBreedingProbability()) {
                    births = rand.nextInt(getMaxLitterSize()) + 1;
                }
            }
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
     * Handles disease progression and effects.
     * Newly infected predators have a chance to die immediately.
     * Otherwise, infection clears after one cycle.
     */
    private void handleDisease() {
        if (infected && justInfected) {
            justInfected = false;  // reset flag so the death chance applies only once
            if (rand.nextDouble() < PREDATOR_DEATH_PROBABILITY) {
                setDead();
            }
        }
    }
    
    protected boolean reproducesSexually() {
        return true;  // Default is true, override in Lizard class to return false
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