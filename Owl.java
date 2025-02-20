import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Models an Owl in the ecosystem simulation.
 * Owls are nocturnal predators specializing in hunting mice and lizards.
 * They are most active at night and compete with snakes for prey.
 * Can become infected from eating diseased mice.
 * 
 * Key characteristics:
 * - Nocturnal (active during night)
 * - Hunts mice (food value: 25) and lizards (food value: 30)
 * - 70% chance to rest during day
 * - Can be infected through eating diseased mice
 * - Reduced hunting efficiency (50% success rate)
 * 
 * @author Tuleen Rowaihy & Hamed Latif
 * @version 7.1
 */
public class Owl extends Animal
{
     // Characteristics shared by all bears (class variables)
    private static final int BREEDING_AGE = 3;
    private static final int MAX_AGE = 7;
    private static final double BREEDING_PROBABILITY = 0.20;
    private static final int MAX_LITTER_SIZE = 5;
    private static final int MOUSE_FOOD_VALUE = 25;
    private static final int LIZARD_FOOD_VALUE = 30;
    
    protected static final Random rand = Randomizer.getRandom();

    /**
     * Create a new owl. Owls can be created as newborn (age zero and not hungry)
     * or with random age and hunger level.
     * 
     * @param randomAge If true, the owl starts with random age and hunger.
     * @param field The field currently being used.
     * @param location The location within the field.
     */
    public Owl(boolean randomAge, Field field, Location location) {
        super(location, randomAge, BREEDING_AGE, MAX_AGE);
    }
    
    /**
     * Look for prey adjacent to the current location.
     * Owls prefer lizards over mice but will eat either.
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
     * Make the owl eat prey.
     * Owls only eat when very hungry (below 25% food level).
     * 
     * @param foodValue The amount of food gained from eating prey.
     */
    protected void eat(int foodValue) {
        if (getFoodLevel() < getMaxFoodValue() / 4) { // Only eat when food level is below 25%
            setFoodLevel(getFoodLevel() + foodValue);
        }
    }
    
    /**
     * Create a new baby owl.
     * 
     * @param randomAge Whether to randomize the baby's age.
     * @param location Where to place the baby.
     * @param field The field to place the baby in.
     */
    @Override
    protected void createYoung(boolean randomAge, Location location, Field field) {
        Owl young = new Owl(randomAge, field, location);
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
        return 25;
    }
    
    @Override
    protected boolean isActiveTime() {
        return !TimeKeeper.isDaytime(); // Nocturnal (Active at night)
    }
    
    @Override
    protected double getRestingProbability() {
        return 0.7;  // 70% chance to rest during non-active hours
    }
    
}