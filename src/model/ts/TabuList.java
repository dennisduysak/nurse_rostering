package model.ts;

import java.util.LinkedList;
import java.util.Queue;

public class TabuList {

    private Queue<Integer> queue;
    private int queueSize;

    public TabuList(int tabuListSize) {
        queue = new LinkedList<>();
        queueSize = tabuListSize;
    }

    public void add(int i) {
        boolean hasAdded = false;
        while (!hasAdded) {
            if (queue.size() < queueSize) {
                queue.add(i);
                hasAdded = true;
            } else {
                queue.poll();
            }
        }
    }

    public boolean contains(int i) {
        return queue.contains(i);
    }
}
