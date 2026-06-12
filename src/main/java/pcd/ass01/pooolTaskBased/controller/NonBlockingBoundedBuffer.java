package pcd.ass01.pooolTaskBased.controller;

import pcd.ass01.poool.util.BoundedBuffer;

import java.util.LinkedList;

public class NonBlockingBoundedBuffer<Item> implements BoundedBuffer<Item> {

	private final LinkedList<Item> buffer;
	private final int maxSize;

	public NonBlockingBoundedBuffer(int size) {
		buffer = new LinkedList<Item>();
		maxSize = size;
	}

	@Override
	public synchronized void put(Item item) {
		if (isFull()) {
			throw new IllegalStateException("Buffer is full");
		}
		buffer.addLast(item);
	}

	@Override
	public synchronized Item get() {
		if (buffer.isEmpty()) {
			return null;
		}
		return buffer.removeFirst();
	}

	private boolean isFull() {
		return buffer.size() == maxSize;
	}
}
