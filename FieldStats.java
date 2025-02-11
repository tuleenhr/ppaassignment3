import java.util.HashMap;
import java.util.Map;

/**
 * This class collects and provides some statistical data on the state 
 * of a field. It is flexible: it will create and maintain a counter 
 * for any class of object that is found within the field.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public class FieldStats
{
    // Counters for each type of entity (fox, rabbit, etc.) in the simulation.
    private final Map<Class<?>, Counter> counters;
    // Whether the counters are currently up to date.
    private boolean countsValid;
    // track all infected animals (both mice and predators)
    private int infectedAnimalsCount = 0;

    /**
     * Construct a FieldStats object.
     */
    public FieldStats()
    {
        // Set up a collection for counters for each type of animal that
        // we might find
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
     * Get details of what is in the field.
     * @return A string describing what is in the field.
     */
    public String getPopulationDetails(Field field)
    {
        StringBuilder details = new StringBuilder();
        if(!countsValid) {
            generateCounts(field);
        }
        
        // Sort animals and plants separately for better readability
        for(Class<?> key : counters.keySet()) {
            if(Animal.class.isAssignableFrom(key)) {
                Counter info = counters.get(key);
                details.append(info.getName());
                details.append(": ");
                details.append(info.getCount());
                details.append(" ");
            }
        }
        
        details.append("| ");  // Separator between animals and plants
        
        for(Class<?> key : counters.keySet()) {
            if(Plant.class.isAssignableFrom(key)) {
                Counter info = counters.get(key);
                details.append(info.getName());
                details.append(": ");
                details.append(info.getCount());
                details.append(" ");
            }
        }
        
        details.append("| " + "Infected: " + getInfectedAnimalsCount(field));
            
        return details.toString();
    }
    
    /**
     * Invalidate the current set of statistics; reset all 
     * counts to zero.
     */
    public void reset() 
    {
        countsValid = false;
        for(Counter count : counters.values()) {
            count.reset();
        }
    }

    /**
     * Increment the count for one class of animal.
     * @param animalClass The class of animal to increment.
     */
    public void incrementCount(Class<?> animalClass)
    {
        Counter count = counters.get(animalClass);
        if(count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            count = new Counter(animalClass.getName());
            counters.put(animalClass, count);
        }
        count.increment();
    }

    /**
     * Indicate that an animal count has been completed.
     */
    public void countFinished()
    {
        countsValid = true;
    }

    /**
     * Determine whether the simulation is still viable.
     * I.e., should it continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        if(!countsValid) {
            generateCounts(field);
        }
        
        // Count species with non-zero populations
        int nonZero = 0;
        for(Class<?> key : counters.keySet()) {
            Counter info = counters.get(key);
            if(info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;  // Need at least two species for viable ecosystem
    }
    
    /**
     * Generate counts of the number of foxes and rabbits.
     * These are not kept up to date as foxes and rabbits
     * are placed in the field, but only when a request
     * is made for the information.
     * @param field The field to generate the stats for.
     */
    private void generateCounts(Field field) {
        reset();
        infectedAnimalsCount = 0;
        
        // Count animals
        for (Animal animal : field.getAnimals()) {
            if (animal.isAlive()) {
                incrementCount(animal.getClass());
                if (animal.isInfected()) {
                    infectedAnimalsCount++;  // if the animal is infected and alive, increase the infected count
                }
            }
        }
        
        // Count plants
        for(Plant plant : field.getPlants()) {
            if(plant.isAlive()) {
                incrementCount(plant.getClass());
            }
        }
        
        countsValid = true;
        
    }
    
    public int getInfectedAnimalsCount(Field field) {
        generateCounts(field);
        return infectedAnimalsCount;
    }

}
