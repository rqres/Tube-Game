/**
 * Represents a card. This class inherits from entity.
 * All cards have a balance.
 */

public class Card extends Entity {
    private float balance;

    /**
     * Create a card with a balance.
     * @param name
     * @param balance
     */
    public Card(String name, float balance) {
        super(name, 0.1F, true);
        this.balance = balance;
    }

    /**
     * Try to bill this card with the selected amount. If there is not enough money print an error message.
     * @param amount
     * @return
     */
    public boolean bill(float amount) {
        if(balance-amount < 0) {
            System.out.println("too broke");
            return false;
        }
        balance -= amount;
        return true;
    }

    /**
     *
     * @return the remaining balance on the card
     * */
    public float getRemainingBalance() {
        return this.balance;
    }
}
