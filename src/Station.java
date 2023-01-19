import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a location on the tube map.
 * All locations hold a description, a set of all of their neighbours, a set of all the items available at
 * the location and whether it is a station or an exit.
 */

public class Station {
    private String name, description, kind;
    private HashSet<Station> neighbours;
    private HashSet<Entity> itemsAvailable;

    /**
     * Create a station
     * @param name
     * @param description
     */
    public Station(String name, String description) {
        this.name = name;
        this.description = description;
        this.kind = "station";
        this.neighbours = new HashSet<>();
        itemsAvailable = new HashSet<>();
    }

    /**
     * Create an exit
     * @param name
     * @param description
     * @param kind
     */
    public Station(String name, String description, String kind) {
        this.name = name;
        this.description = description;
        this.kind = kind;
        this.neighbours = new HashSet<>();
        itemsAvailable = new HashSet<>();
    }

    /**
     * Adds an item to this location on the map.
     * @param item
     */
    public void addItem(Entity item) {
        itemsAvailable.add(item);
    }

    /**
     * Removes an item from this location.
     * @param item
     */
    public void removeItem(Entity item) {
        itemsAvailable.remove(item);
    }

    /**
     *
     * @return a set of all items available at the station
     */
    public HashSet<Entity> getItemsAvailable() {
        return itemsAvailable;
    }

    /**
     * Sets the neighbours of this station
     * @param tubeMap map to use to extract neighbour data
     */
    public void setNeighbours(StationsGraph tubeMap) {
        HashSet<Station> neighboursSet = tubeMap.getAdjacentNodes(this);
        if (!neighboursSet.isEmpty())
            this.neighbours.addAll(neighboursSet);
    }

    /**
     *
     * @return a set of this stations's neighbours (other stations)
     */
    public HashSet<Station> getNeighbours() {
        HashSet<Station> results = new HashSet<>();
        for (Station entry : neighbours)
            if (!entry.isExit()) {
                results.add(entry);
            }
        return results;
    }

    /**
     *
     * @return a pretty string of the station's neighbours
     */
    public String getNeighboursString() {
        String returnString = "";
        for (Station neighbour : getNeighbours())
            returnString += " " + neighbour.getName();
        return returnString;
    }

    /**
     *
     * @return a set of this station's exits
     */
    public HashSet<Station> getExits() {
        HashSet<Station> results = new HashSet<>();
        for (Station neighbour : neighbours)
            if (neighbour.isExit()) {
                results.add(neighbour);
            }
        return results;
    }

    /**
     *
     * @return a pretty string of this station's exits
     */
    public String getExitsString() {
        String returnString = "";
        for (Station exit : getExits())
                returnString += " " + exit.getName();
        return returnString;
    }

    /**
     *
     * @return the name of this location
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the description of this location
     */
    public String getLongDescription() {
        if (isExit())
            return "You are at The " + name + "\n" + description;

        return "You are at " + name + " station." + "\n" + description;
    }

    /**
     *
     * @return whether this location is an exit or not
     */
    public boolean isExit() {
        return kind.equals("exit");
    }


}
