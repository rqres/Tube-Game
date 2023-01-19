import java.util.ArrayList;

/**
 * This class represents the player. An instance of this class has several attributes.
 * We should always know the player's location, their previous location, whether they have paid the entrace fare,
 * the weight they are currently carrying, the list of items they are carrying and whether they are drunk or not.
 */

public class Player {
    private Station location;
    private Station previousLocation;
    private boolean hasPaidTubeEntrance;
    private static final float maxWeight = 5;
    private float currentWeight;
    private ArrayList<Entity> itemsCarried;
    private boolean isDrunk;

    /**
     * Create a player. Initially they have not paid the tube fare and they're not carrying anything.
     */
    public Player() {
        hasPaidTubeEntrance = false;
        currentWeight = 0;
        itemsCarried = new ArrayList<>();
        isDrunk = false;
    }

    /**
     * Checks whether the player is at the Strand Campus or not
     * @return
     */
    public boolean isAtCampus() {
        return (location.getName().equals("StrandCampus"));
    }

    /**
     * Checks whether the player is currently on the train or not.
     * @return
     */
    public boolean isOnTube() {
        return (!location.isExit() && !previousLocation.isExit());
    }

    /**
     * Gets the location of the player.
     * @return Station
     */
    public Station getLocation() {
        return location;
    }

    /**
     * Gets the previous location of the player.
     * @return Station
     */
    public Station getPreviousLocation() {
        return previousLocation;
    }

    /**
     * Sets the location of the player
     * @param location
     */
    public void setLocation(Station location) {
        this.location = location;
    }

    /**
     * Sets the previous location of the player
     * @param location
     */
    public void setPreviousLocation(Station location) {
        previousLocation = location;
    }

    /**
     * Checks whether the player has paid the tube fare or not.
     * @return
     */
    public boolean getHasPaidTubeEntrance() {
        return hasPaidTubeEntrance;
    }

    /**
     * Sets the entrance paid status.
     * @param val
     */
    public void setHasPaidTubeEntrance(boolean val) {
        hasPaidTubeEntrance = val;
    }

    /**
     * Tries to put an item in backpack. If too heavy prints an error message.
     * @param item
     * @return
     */
    public boolean putItemInBackpack(Entity item) {
        if (currentWeight + item.getWeight() > maxWeight) {
            System.out.println("Item too heavy");
            return false;
        }

        if (!item.ableToBePickedUp) {
            System.out.println("This item can't be picked up");
            return false;
        }

        itemsCarried.add(item);
        currentWeight += item.getWeight();
        item.setQuantity(item.getQuantity() - 1);
        if (item.getQuantity() <= 0)
            getLocation().removeItem(item);
        return true;
    }

    /**
     * Tries to drop an item. If not currently carrying it, prints error message.
     * @param item
     */
    public void dropItem(Entity item) {
        itemsCarried.remove(item);
        currentWeight -= item.getWeight();
        item.setQuantity(item.getQuantity() + 1);
        getLocation().addItem(item);
    }

    /**
     *
     * @return How much space is left in the backpack.
     */
    public float getRemainingWeight() {
        return maxWeight - currentWeight;
    }

    /**
     * Returns a list of the items carried
     * @return
     */
    public ArrayList<Entity> getItemsCarried() {
        return itemsCarried;
    }

    /**
     * Sets drunk status
     * @param val
     */
    public void setDrunk(boolean val) {
        isDrunk = val;
    }

    /**
     * Checks whether the player is drunk or not
     * @return
     */
    public boolean isDrunk() {
        return isDrunk;
    }

    public boolean isCompletelyBroke() {
        if (itemsCarried.isEmpty())
            return false;

        for (Entity item : itemsCarried)
            if (item instanceof Card)
                if (((Card) item).getRemainingBalance() > 0)
                    return false;

        return true;
    }
}
