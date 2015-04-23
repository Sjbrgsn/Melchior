package test;

import org.testng.Assert;
import org.testng.annotations.Test;
import pathfinding.Location;
import pathfinding.PriorityQueue;

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
        Assert.assertEquals(tester.dequeue(), new Location(2, 2), "PriorityQueue should give us Location(2, 2)");

        tester.enqueue(new Location(3, 3), 75);
        Assert.assertEquals(tester.dequeue(), new Location(3, 3), "PriorityQueue should give us Location(3, 3)");
        Assert.assertEquals(tester.dequeue(), new Location(1, 1), "PriorityQueue should give us Location(1, 1)");
    }
}
