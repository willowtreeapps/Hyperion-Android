package com.willowtreeapps.hyperion.timber.model;

/**
 * FIFO Queue/Ring buffer implementation that overwrites previous queued entries once full.
 *
 * @param <T>
 */
public class CircularBuffer<T> {

    private final Object[] queue;
    private int head = 0;
    private int nextHead = 0;
    private int currentSize = 0;
    private int maxSize;

    /**
     * Create the queue with a maximum size.
     *
     * @param size the maximum number of elements that can be held by the queue.
     */
    public CircularBuffer(int size) {
        queue = new Object[size];
        maxSize = size;
    }

    /**
     * Add an item to the queue.
     *
     * @param item to be added
     */
    public void enqueue(T item) {
        head = nextHead;
        queue[head] = item;
        if (currentSize != maxSize) {
            currentSize++;
        }
        nextHead++;
        if (nextHead == maxSize) {
            nextHead = 0;
        }

        printQueue();
        //printQueueNormal();
    }

    /**
     * Current number of elements stored in the queue.
     *
     * @return number of stored elements
     */
    public int size() {
        return currentSize;
    }

    /**
     * Get the item at a position `n` positions from the head.
     *
     * @param index of the item to retrieve
     * @return the stored item
     */
    @SuppressWarnings("unchecked")
    public T getItem(int index) {
        int target = index;
        if (index >= maxSize) {
            target = maxSize - index;
        }
        return (T) queue[head - target];
    }

    private void printQueue() {
        System.out.print("Curren: ");
        for (int i = 0; i < this.size(); i++) {
            try {
                System.out.print(" " + this.getItem(i) + " ");
            } catch (Exception e) {
                System.out.print(" e ");
            }
        }
        System.out.println();
    }

    private void printQueueNormal() {
        System.out.print("Actual: ");
        for (int i = 0; i < this.size(); i++) {
            System.out.print(" " + queue[i] + " ");
        }
        System.out.println();
    }
}
