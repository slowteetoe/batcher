package com.slowteetoe.collections;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class BatchingQueueTest {

	@Test
	public void shouldDistributeEvenly() {

		List<BatchReceiver> receivers = buildTestReceivers(3);

		BatchingQueue queue = new BatchingQueue(receivers, 2);

		List<String> items = Arrays.asList("a", "b", "c", "d");

		for (String s : items) {
			queue.distribute(s);
		}

		System.out.println(queue.dumpInternalState());
	}

	@Test
	public void shouldExecMultipleTimes() {
		List<BatchReceiver> receivers = buildTestReceivers(3);

		BatchingQueue queue = new BatchingQueue(receivers, 2);

		for (int i = 0; i < 1000; i++) {
			queue.distribute(i);
		}
		CountingBatchReceiver one = ((CountingBatchReceiver) receivers.get(0));
		CountingBatchReceiver two = ((CountingBatchReceiver) receivers.get(1));
		CountingBatchReceiver three = ((CountingBatchReceiver) receivers.get(2));

		assertEquals(167, one.triggerCount);
		assertEquals(166, two.triggerCount);
		assertEquals(166, three.triggerCount);

		queue.dumpInternalState();
		System.out.println(queue.dumpInternalState());

	}

	private List<BatchReceiver> buildTestReceivers(int num) {
		List<BatchReceiver> receivers = new ArrayList<BatchReceiver>();
		for (int i = 0; i < num; i++) {
			receivers.add(new CountingBatchReceiver());
		}
		assertEquals("There should be the correct number of receivers", num, receivers.size());

		return receivers;
	}

}
