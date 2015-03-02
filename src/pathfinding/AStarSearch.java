package pathfinding;

import java.util.HashMap;

/**
 * Created by Holmgr on 2015-03-02.
 * Implementation of the A* pathfinding algoritm including
 * herustics. Calculates the shortest path of nodes from a given
 * start node to a given end node.
 */
public class AStarSearch {
    public HashMap<Location, Location> cameFrom;
    public HashMap<Location, Integer> costSoFar;
    public Location start, goal;

    /**
     * Method used for determining the priority to a given node
     * based on the distance to the goal node
     * Lower value implies higher priority
     */
    public static int heuristic(Location a, Location b){
        return (int)(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2)); // Pythagorean theorem
    }

    /**
     * The A* algoritm. Uses a graph of nodes, a start Location and a end Location
     */
    public AStarSearch(WeightedGraph graph, Location start, Location goal) {

        cameFrom = new HashMap<Location, Location>();
        costSoFar = new HashMap<Location, Integer>();
        this.start = start;
        this.goal = goal;

        PriorityQueue<Location> frontier = new PriorityQueue<Location>();
        frontier.enqueue(start, 0);

        cameFrom.put(start, start);
        costSoFar.put(start, 0);

        while (frontier.count() > 0){

            Location current = frontier.dequeue(); // Continue with the Location with the lowest priority value
            if (current.equals(goal)){
                break; // Reached goal, stop search
            }

            for (Location next : graph.neighbors(current)){
                int newCost = costSoFar.get(current) + graph.cost(current, next); // Calculate cost to neighbor

                // Add next if not already visited or if the current path is shorter to next than before
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)){
                    costSoFar.put(next, newCost);
                    int priority = heuristic(next, goal);
                    frontier.enqueue(next, priority);
                    cameFrom.put(next, current);
                }
            }
        }
    }
}
