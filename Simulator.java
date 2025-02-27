import java.util.*;

/**
*
* A simple predator-prey simulator, based on a rectangular field containing
* preys and predators.
* @author Hamed Latif & Tuleen Rowaihy
* @version 20.02.25
*/
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 120;
   
    // Constants for creation probabilities
    private static final double BEAR_CREATION_PROBABILITY = 0.02;
    private static final double OWL_CREATION_PROBABILITY = 0.02;
    private static final double SNAKE_CREATION_PROBABILITY = 0.02;
    private static final double MOUSE_CREATION_PROBABILITY = 0.08;
    private static final double DEER_CREATION_PROBABILITY = 0.05;
    private static final double LIZARD_CREATION_PROBABILITY = 0.03;
    private static final double BERRY_CREATION_PROBABILITY = 0.09;
    private static final double GRASS_CREATION_PROBABILITY = 0;   // grass will only spread in random patches
    
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    private int timeOfDay = 0; // 0 = Day, 1 = Night
    private Weather weather = new Weather(); // weather (raining or sunny)
    // A graphical view of the simulation.
    private final SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        field = new Field(depth, width);
        view = new SimulatorView(depth, width);
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long 
     * period (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }
    
    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(50);  // adjust this to change simulation speed
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        String currentSeason = TimeKeeper.getCurrentSeason();
        weather.updateWeather(currentSeason); // Update weather
        TimeKeeper.advanceTime(); // Advance time (toggle day/night)

        // Provide space for newborn animals.
        Field nextField = new Field(field.getDepth(), field.getWidth());
        
        // Update all animals
        for(Animal animal : field.getAnimals()) {
            if(animal.isAlive()) {
                animal.act(field, nextField);
                nextField.placeAnimal(animal, animal.getLocation()); // ensure every animal moves
            }
        }
        
        // Update all plants
        for(Plant plant : field.getPlants()) {
            if(plant.isAlive()) {
                plant.act(field, nextField, weather);
            }
        }       
        
        // Replace the old state with the new one.
        field = nextField;
        
        view.showStatus(step, field, weather);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        populate();
        view.showStatus(step, field, weather);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        
        // place random clusters of grass and animals scattered around the map.
        field.placeRandomGrassPatches(30, 10, 20);
        field.placeRandomAnimalClusters(40, 30, 40, Mouse.class); // 10 clusters of mice, size 6-12
        field.placeRandomAnimalClusters(30, 20, 30, Deer.class); // 8 clusters of deer, size 10-15
        field.placeRandomAnimalClusters(20, 10, 20, Owl.class); // 4 clusters of owls, size 2-6
        field.placeRandomAnimalClusters(20, 10, 15, Snake.class); // 5 clusters of snakes, size 3-7
        field.placeRandomAnimalClusters(3, 3, 6, Bear.class);  // 6 clusters of bears, size 4-8
        
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                double chance = rand.nextDouble();  // Generate one random number for this location
                
                if(chance <= BEAR_CREATION_PROBABILITY) {
                    field.placeAnimal(new Bear(true, field, location), location);
                }
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY) {
                    field.placeAnimal(new Owl(true, field, location), location);
                }
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY) {
                    field.placeAnimal(new Snake(true, field, location), location);
                }
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + MOUSE_CREATION_PROBABILITY) {
                    field.placeAnimal(new Mouse(true, field, location), location);
                }
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + MOUSE_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY) {
                    field.placeAnimal(new Deer(true, field, location), location);
                }
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + MOUSE_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY + LIZARD_CREATION_PROBABILITY) {
                    field.placeAnimal(new Lizard(true, field, location), location);
                }
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + MOUSE_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY + LIZARD_CREATION_PROBABILITY +BERRY_CREATION_PROBABILITY) {
                    field.placePlant(new Berry(location, true), location);
                }
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + MOUSE_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY + LIZARD_CREATION_PROBABILITY + BERRY_CREATION_PROBABILITY + GRASS_CREATION_PROBABILITY) {
                    field.placePlant(new Grass(location, true), location);
                }
            }
        }
    }

    /**
     * Pause for a given time.
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds)
    {
        try {
             Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }
}
