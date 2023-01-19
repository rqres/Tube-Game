/**
 * This class represents a drink and whether it is alcoholic or not.
 * Inherits from entity.
 */

public class Drink extends Entity {
    private String kind; //alcoholic or soft

    /**
     * Create a new drink.
     * @param name
     * @param price
     * @param kind
     */
    public Drink(String name, float price, String kind) {
        super(name, 0, false, price);
        this.price = price;
        this.kind = kind;
    }

    /**
     *
     * @return whether it is alcoholic or not
     */
    public boolean isAlcoholic() {
        return kind.equals("alcoholic");
    }

}
