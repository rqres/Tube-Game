import java.util.HashMap;
import java.util.HashSet;

/**
 * This class implements a bidirectional unweighted graph in the form of a tube map
 */
public class StationsGraph {
    private HashMap<Station, HashSet<Station>> tubeMap = new HashMap<>(); // map that contains the current node and its neighbours


    /**
     * Add vertex to the graph
     * @param v
     */
    public void addVertex(Station v) {
        tubeMap.put(v, new HashSet<>());
    }

    /**
     * Create an edge between two vertices, and adds them to the graph if they do not exist.
     * @param source
     * @param destination
     */
    public void addEdge(Station source, Station destination) {
        if (!tubeMap.containsKey(source))
            addVertex(source);
        if (!tubeMap.containsKey(destination))
            addVertex(destination);

        tubeMap.get(source).add(destination);
        tubeMap.get(destination).add(source);
    }

    /**
     * Creates a directed edge from one vertex to the other. Used for the magic transporter only.
     * @param source
     * @param destination
     */
    public void addDirectedEdge(Station source, Station destination) {
        if (!tubeMap.containsKey(source))
            addVertex(source);
        if (!tubeMap.containsKey(destination))
            addVertex(destination);

        tubeMap.get(source).add(destination);

    }

    /**
     * Returns the station object from an input string
     * @param stationName
     * @return
     */
    public Station getStation(String stationName) {
        for (Station station : tubeMap.keySet())
            if (station.getName().equals(stationName))
                return station;
        return null;
    }

    /**
     * Gets the adjacent nodes of the selected node
     * @param v
     * @return
     */
    public HashSet<Station> getAdjacentNodes(Station v) {
        HashSet<Station> results = new HashSet<>();

        if (!tubeMap.get(v).isEmpty())
            results.addAll(tubeMap.get(v));

        return results;
    }
}
