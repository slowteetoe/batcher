package com.slowteetoe.collections;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CountingBatchReceiver implements BatchReceiver {

	private static AtomicInteger index = new AtomicInteger(0);

	private String name;

	public int triggerCount = 0;

	public CountingBatchReceiver() {
		name = "CountingBatchReceiver[" + index.getAndAdd(1) + "]";
	}

	public void receiveBatch(List<?> objects) {

		System.out.println(name + ": there were " + objects.size()
				+ " objects received. " + objects);

		triggerCount++;
	}

}
