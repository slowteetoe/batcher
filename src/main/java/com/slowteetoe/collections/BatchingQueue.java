package com.slowteetoe.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchingQueue {

	private AtomicInteger receiverIndex = new AtomicInteger(0);
	private int queueSize;

	private BatchReceiver[] receivers;
	private InternalQueue[] queues;

	public BatchingQueue(List<BatchReceiver> recipients, int batchSize) {
		if (recipients == null || recipients.size() == 0) {
			throw new IllegalArgumentException("There must be at least one receiver provided for the queue");
		}
		receivers = new BatchReceiver[recipients.size()];
		for (int i = 0; i < recipients.size(); i++) {
			receivers[i] = recipients.get(i);
		}

		queueSize = this.receivers.length;

		queues = new InternalQueue[queueSize];

		for (int i = 0; i < queueSize; i++) {
			queues[i] = new InternalQueue(this.receivers[i], batchSize);
		}

	}

	public synchronized void enqueue(Object o) {
		this.distribute(o);
	}

	protected synchronized void distribute(Object o) {
		int i = receiverIndex.getAndAdd(1) % queueSize;
		queues[i].enqueue(o);
	}

	protected static class InternalQueue {

		private static AtomicInteger index = new AtomicInteger(0);

		private String id;

		private BatchReceiver receiver;

		private List contents = new ArrayList();

		private int maxSize;

		public InternalQueue(BatchReceiver receiver, int maxSize) {
			this.id = "InternalQueue[" + index.getAndAdd(1) + "]";
			this.receiver = receiver;
			this.maxSize = maxSize;
		}

		public void enqueue(Object o) {
			contents.add(o);
			if (contents.size() == maxSize) {
				receiver.receiveBatch(contents);
				contents.clear();
			}
		}

		public int currentSize() {
			return contents.size();
		}

		public String dumpState() {
			StringBuffer sb = new StringBuffer(id);
			for (Object o : contents) {
				sb.append(o);
			}
			sb.append("\n");
			return sb.toString();
		}
	}

	public String dumpInternalState() {
		StringBuffer sb = new StringBuffer("");
		for (InternalQueue q : queues) {
			sb.append(q.dumpState());
		}
		return sb.toString();
	}

}
