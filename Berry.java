/**
 * A berry is a plant that grows randomly around the habitat.
 * It is a source of energy for deers and mice.
 * @author Hamed Latif & Tuleen Rowaihy
 * @version 19.02.25
 */
public class Berry extends Plant {
    private static final double GROWTH_PROBABILITY = 0.6; // 60% chance to grow each step
    private static final double SPREADING_PROBABILITY = 0.3; // 30% chance to spread

    /**
     * Create a new berry plant
     * @param location Where the berry will be placed
     * @param randomize If true, starts mature
     */
    public Berry(Location location, boolean randomize) {
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
        Berry berry = new Berry(location, randomize);
        field.placePlant(berry, location);
    }
}