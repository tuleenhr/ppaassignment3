import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
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
    private static final double MOUSE_CREATION_PROBABILITY = 0.05;
    private static final double DEER_CREATION_PROBABILITY = 0.05;
    private static final double BERRY_CREATION_PROBABILITY = 0.09;
    private static final double GRASS_CREATION_PROBABILITY = 0;   // grass will only spread in random patches
    
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    private int timeOfDay = 0; // 0 = Day, 1 = Night
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
        TimeKeeper.advanceTime(); // Advance time (toggle day/night)

        // Provide space for newborn animals.
        Field nextField = new Field(field.getDepth(), field.getWidth());
        
        // Update all animals
        for(Animal animal : field.getAnimals()) {
            if(animal.isAlive()) {
                animal.act(field, nextField);
            }
        }
        
        // Update all plants
        for(Plant plant : field.getPlants()) {
            if(plant.isAlive()) {
                plant.act(field, nextField);
            }
        }       
        
        // Replace the old state with the new one.
        field = nextField;
        
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        populate();
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        
        field.placeRandomGrassPatches(15, 5, 10);
        
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
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + MOUSE_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY + BERRY_CREATION_PROBABILITY) {
                    field.placePlant(new Berry(location, true), location);
                }
                else if(chance <= BEAR_CREATION_PROBABILITY + OWL_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + MOUSE_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY + BERRY_CREATION_PROBABILITY + GRASS_CREATION_PROBABILITY) {
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
