package com.willowtreeapps.hyperion.timber.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CircularBufferTest {

    @Test
    public void size() {
        CircularBuffer<Integer> queue = new CircularBuffer<>(2);
        assertEquals(0, queue.size());
        queue.enqueue(0);
        queue.enqueue(1);
        assertEquals(2, queue.size());
        queue.enqueue(3);
        assertEquals(2, queue.size());
    }

    @Test
    public void getItem() {
        CircularBuffer<Integer> queue = new CircularBuffer<>(3);
        queue.enqueue(0);
        assertEquals((Integer) 0, queue.getItem(0));
        queue.enqueue(1);
        assertEquals((Integer) 1, queue.getItem(0));
        assertEquals((Integer) 0, queue.getItem(1));
        queue.enqueue(2);
        assertEquals((Integer) 2, queue.getItem(0));
        assertEquals((Integer) 1, queue.getItem(1));
        assertEquals((Integer) 0, queue.getItem(2));
        queue.enqueue(3);
        assertEquals((Integer) 3, queue.getItem(0));
        assertEquals((Integer) 2, queue.getItem(1));
        assertEquals((Integer) 1, queue.getItem(2));
    }

    @Test
    public void overfill() {
        int maxSize = 10;
        CircularBuffer<Integer> queue = new CircularBuffer<>(maxSize);
        for (int i = 1; i <= maxSize * 4; i++) {
            queue.enqueue(i);
        }
        assertEquals((Integer) (maxSize * 4), queue.getItem(0));
    }

}