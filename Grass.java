/**
 * Grass makes up a lot of the forest.
 * It grows in set patches and is a source of energy for preys.
 * It grows more under the rain.
 * @author Hamed Latif & Tuleen Rowaihy
 * @version 20.02.25
 */
public class Grass extends Plant {
    private static final double GROWTH_PROBABILITY = 0.70; // 70% chance to grow each step
    private static final double SPREADING_PROBABILITY = 0.50; // 50% chance to spread

    /**
     * Create a new grass plant
     * @param location Where the grass will be placed
     * @param randomize If true, starts mature
     */
    public Grass(Location location, boolean randomize) {
        super(location, randomize);
    }

    @Override
    protected double getGrowthProbability() {
        return GROWTH_PROBABILITY;
    }

    @Override
    protected double getSpreadingProbability() {
        return SPREADING_PROBABILITY;
    }

    @Override
    protected void createNewPlant(boolean randomize, Location location, Field field) {
        Grass grass = new Grass(location, randomize);
        field.placePlant(grass, location);
    }
}