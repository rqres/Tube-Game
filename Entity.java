/**
 * This class is responsible for all entities in the game, such as books, laptops, drinks and cards.
 * All entities will have a name and a weight.
 */

public class Entity {
    protected String name;
    protected float weight, price;
    protected int quantity;
    protected boolean ableToBePickedUp;

    /**
     * Create an entity without price.
     * @param name
     * @param weight
     * @param ableToBePickedUp
     */
    public Entity(String name, float weight, boolean ableToBePickedUp) {
        this.name = name;
        this.weight = weight;
        this.ableToBePickedUp = ableToBePickedUp;
        quantity = 9999;
        price = -1;
    }

    /**
     * Create an entity with price
     * @param name
     * @param weight
     * @param ableToBePickedUp
     * @param price
     */
    public Entity(String name, float weight, boolean ableToBePickedUp, float price) {
        this.name = name;
        this.weight = weight;
        this.ableToBePickedUp = ableToBePickedUp;
        this.price = price;
        quantity = 9999;
    }

    /**
     *
     * @return the name of the entity
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the price of the items
     */
    public float getPrice() {
        return price;
    }

    /**
     *
     * @return the weight of the entity
     */
    public float getWeight() {
        return weight;
    }

    /**
     *
     * @return whether this item is able to be picked up and put in the backpack
     */
    public boolean isAbleToBePickedUp() {
        return ableToBePickedUp;
    }

    /**
     *
     * @return how many items like this there are
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @return whether this item is purchasable or not
     */
    public boolean isPurchasable() {
        return price > 0;
    }

    /**
     * Set how many items like this there are
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Make this item non purchasable
     */
    public void setNonPurchasable() {
        price = -1;
    }
}
