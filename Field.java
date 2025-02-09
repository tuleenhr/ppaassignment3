import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal/object.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The dimensions of the field.
    private final int depth, width;
    // Storage for animals and plants
    private final Map<Location, Animal> animals = new HashMap<>();
    private final Map<Location, Plant> plants = new HashMap<>();

    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Place an animal at the given location.
     */
    public void placeAnimal(Animal animal, Location location) {
        if(animal != null && location != null) {
            animals.put(location, animal);
        }
    }
    
    /**
     * Place a plant at the given location.
     */
    public void placePlant(Plant plant, Location location) {
        if(plant != null && location != null) {
            plants.put(location, plant);
        }
    }
    
    /**
     * Return the animal at the given location, if any.
     */
    public Animal getAnimalAt(Location location) {
        return animals.get(location);
    }
    
    /**
     * Return the plant at the given location, if any.
     */
    public Plant getPlantAt(Location location) {
        return plants.get(location);
    }
    
    /**
     * Check if a location is free (no animal or plant).
     */
    public boolean isFree(Location location) {
        return !animals.containsKey(location) && !plants.containsKey(location);
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getAdjacentLocations(location);
        for(Location next : adjacent) {
            if(isFree(next)) {
                free.add(next);
            }
        }
        return free;
    }

    /**
    * Get adjacent locations.
    */
    public List<Location> getAdjacentLocations(Location location) {
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Clear the field.
     */
    public void clear() {
        animals.clear();
        plants.clear();
    }
    
    /**
     * Get the depth of the field.
     */
    public int getDepth() {
        return depth;
    }
    
    /**
     * Get the width of the field.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Get all plants in the field.
     */
    public List<Plant> getPlants() {
        return new ArrayList<>(plants.values());
    }
    
    /**
     * Get all animals in the field.
     */
    public List<Animal> getAnimals() {
        return new ArrayList<>(animals.values());
    }
}
