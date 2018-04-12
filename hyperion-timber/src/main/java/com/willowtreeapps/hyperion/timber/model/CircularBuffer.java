package com.willowtreeapps.hyperion.timber.model;

/**
 * FIFO Queue/Ring buffer implementation that overwrites previous queued entries once full.
 *
 * @param <T>
 */
public class CircularBuffer<T> {

    private final Object[] queue;
    private int head;
    private int size;

    /**
     * Create the queue with a maximum size.
     *
     * @param size the maximum number of elements that can be held by the queue.
     */
    public CircularBuffer(int size) {
        queue = new Object[size];
    }

    /**
     * Add an item to the queue.
     *
     * @param item to be added
     */
    public void enqueue(T item) {
        queue[head] = item;
        head++;
        size++;
        if (head == queue.length) head = 0;
    }

    /**
     * Current number of elements stored in the queue.
     *
     * @return number of stored elements
     */
    public int size() {
        return Math.min(this.size, queue.length);
    }

    /**
     * Get the item at a position `n` positions from the head.
     *
     * @param index of the item to retrieve
     * @return the stored item
     */
    @SuppressWarnings("unchecked")
    public T getItem(int index) {
        int target = head - 1 - index;
        if (target < 0) target = size + target - head;
        return (T) queue[target];
    }

}
