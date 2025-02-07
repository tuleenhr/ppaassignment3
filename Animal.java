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
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;
    
    private final boolean isMale;
    
    private static int timeOfDay = 0;

    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     * @param randomize A boolean to determine random gender assignment
     */
    public Animal(Location location, boolean randomize) {
        this.alive = true;
        this.location = location;
        if (randomize) {
            this.isMale = Randomizer.getRandom().nextBoolean();
        } else {
            this.isMale = false; // Default to female if not randomized
        }
    }
    
    public boolean isMale() {
        return isMale;
    }
    
    public static int getTimeOfDay() {
        return timeOfDay;
    }
    
    public static void advanceTime() {
        timeOfDay = (timeOfDay + 1) % 24;
    }
    
    public static boolean isDaytime() {
        return timeOfDay >= 6 && timeOfDay < 18;
    }
    
    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void act(Field currentField, Field nextFieldState);
    
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
    
    protected boolean canFindMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal animal = field.getAnimalAt(loc);
            if (animal != null && animal.isAlive() && 
                animal.getClass() == this.getClass() && 
                animal.isMale() != this.isMale()) {
                return true;
            }
        }
        return false;
    }
}
