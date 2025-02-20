import java.util.*;

/**
*
* Represent a rectangular grid of field positions.
* Each position is able to store a single animal/object.
* @author Hamed Latif & Tuleen Rowaihy
* @version 20.02.25
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
    
    /**
     * check if a location is within the field bounds and not outside it.
     * @param x the x-coordinate of the field.
     * @param y the y-coordinate of the field.
     * @return true if it's within the bounds.
     */
    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < depth && y >= 0 && y < width;
    }
    
    /**
     * Place grass patches randomly in the field.
     * @param numPatches number of patches to generate e.g. 15 
     * @param minSize minimum patch size e.g., 7x7.
     * @param maxSize maximum patch size e.g., 25x25.
     */
    public void placeRandomGrassPatches(int numPatches, int minSize, int maxSize) {
        for (int i = 0; i < numPatches; i++) {
            int patchSize = rand.nextInt(maxSize - minSize + 1) + minSize; // generates a random patch size between the range
            int startX = rand.nextInt(depth - patchSize);  // gets a random starting position in the field
            int startY = rand.nextInt(width - patchSize);

            placeGrassPatch(startX, startY, patchSize);   // places the grass patch
        }
    }
    
    /**
     * places a grass patch of given size at a specific location.
     * @param startX the starting x-coordinate in the field where the patch starts.
     * @param startY the starting y-coordinate in the field where the patch ends.
     * @param patchSize the size of the patch.
     */
    private void placeGrassPatch(int startX, int startY, int patchSize) {
        for (int x = startX; x < startX + patchSize; x++) {
            for (int y = startY; y < startY + patchSize; y++) {
                Location loc = new Location(x, y);
                if (isWithinBounds(x, y)) {  // only place the patch if it's within the field bounds
                    placePlant(new Grass(loc, true), loc);
                }
            }
        }
    }
    
    /**
     * Place animal clusters randomly in the field.
     * @param numClusters Number of clusters to generate (e.g., 6 clusters of bears).
     * @param minSize Minimum cluster size (e.g., 3 animals).
     * @param maxSize Maximum cluster size (e.g., 8 animals).
     * @param animalClass The species to place.
     */
    public void placeRandomAnimalClusters(int numClusters, int minSize, int maxSize, Class<?> animalClass) {
        for (int i = 0; i < numClusters; i++) {
            int clusterSize = rand.nextInt(maxSize - minSize + 1) + minSize; // Random cluster size
            int startX = rand.nextInt(depth - 5);  // Ensures clusters stay inside the grid
            int startY = rand.nextInt(width - 5);
    
            placeAnimalCluster(startX, startY, clusterSize, animalClass);
        }
    }
    
    /**
     * Places a cluster of a given animal at a specific location.
     * @param startX The starting x-coordinate in the field where the cluster begins.
     * @param startY The starting y-coordinate in the field where the cluster ends.
     * @param clusterSize The number of animals in the cluster.
     * @param animalClass The species to place.
     */
    private void placeAnimalCluster(int startX, int startY, int clusterSize, Class<?> animalClass) {
        for (int x = startX; x < startX + Math.sqrt(clusterSize); x++) { // Spread animals in a small square
            for (int y = startY; y < startY + Math.sqrt(clusterSize); y++) {
                Location loc = new Location(x, y);
                if (isWithinBounds(x, y) && isFree(loc)) {  
                    placeAnimalByClass(animalClass, loc);
                }
            }
        }
    }
    
    /**
     * Helper method to create and place an animal based on its class.
     * @param animalClass The class of the animal.
     * @param location The location where it should be placed.
     */
    private void placeAnimalByClass(Class<?> animalClass, Location location) {
        if (animalClass == Bear.class) {
            placeAnimal(new Bear(true, this, location), location);
        } else if (animalClass == Deer.class) {
            placeAnimal(new Deer(true, this, location), location);
        } else if (animalClass == Mouse.class) {
            placeAnimal(new Mouse(true, this, location), location);
        } else if (animalClass == Snake.class) {
            placeAnimal(new Snake(true, this, location), location);
        } else if (animalClass == Owl.class) {
            placeAnimal(new Owl(true, this, location), location);
        }
    }

}
