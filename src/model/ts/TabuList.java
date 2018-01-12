package model.ts;

import helper.ConfigurationHelper;

import java.util.LinkedList;
import java.util.Queue;

public class TabuList {

    private Queue<Integer> queue;
    private int queueSize = ConfigurationHelper.getInstance().getPropertyInteger("ts.TabuListSize", 10);

    public TabuList() {
        queue = new LinkedList<>();
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
