package pathfinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Created by Holmgr 2015-03-02
 * Generic queue. Each element is stored with an priority (int).
 */
public class PriorityQueue<T>{

    private List<Tuple<T, Integer>> elements = new ArrayList<Tuple<T, Integer>>();

    public int count(){
	return elements.size();
    }

    /**
     * Adds an element to the queue with a given priority.
     */
    public void enqueue(T item, int priority){
	elements.add(new Tuple<T, Integer>(item, priority));
    }

    /**
     * Pops and returns the element with the lowest priority
     * in the queue.
     */
    public T dequeue(){
	int bestIndex = 0; // Assume the first element is the smallest

	for (int i = 0; i < count(); i++) {
	    if (elements.get(i).b < elements.get(bestIndex).b) // Compare priorities
		bestIndex = i;
	}

	T bestItem = elements.get(bestIndex).a;
	elements.remove(bestIndex);
	return bestItem;
    }
}
