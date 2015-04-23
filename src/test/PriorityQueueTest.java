package test;

import org.junit.Test;
import pathfinding.Location;
import pathfinding.PriorityQueue;

import static org.junit.Assert.assertEquals;

/**
 * Created by Holmgr 2015-03-09
 * Test class using JUnit for PriorityQueue
 */
public class PriorityQueueTest {

    @Test
    public void queueReturnsSmallestElement(){

        // Class to be tested
        PriorityQueue<Location> tester = new PriorityQueue<>();

        tester.enqueue(new Location(1, 1), 100);
        tester.enqueue(new Location(2, 2), 50);

        // Test
        assertEquals("PriorityQueue should give us Location(2, 2)", new Location(2, 2), tester.dequeue());

        tester.enqueue(new Location(3, 3), 75);
        assertEquals("PriorityQueue should give us Location(3, 3)", new Location(3, 3), tester.dequeue());
        assertEquals("PriorityQueue should give us Location(1, 1)", new Location(1, 1), tester.dequeue());
    }
}
