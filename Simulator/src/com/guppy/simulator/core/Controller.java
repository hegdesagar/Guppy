package com.guppy.simulator.core;

import java.util.concurrent.CountDownLatch;

public class Controller {
	private volatile boolean resetFlag = false;
	private CountDownLatch latch;

	public synchronized void initiateReset(int numberOfNodes) {
		this.resetFlag = true;
		this.latch = new CountDownLatch(numberOfNodes);
		//System.out.println("Latch initialized with count: " + numberOfNodes);
	}

	public synchronized boolean isResetInProgress() {
		return resetFlag;
	}

	public synchronized void decrementLatch() {
		if (latch != null) {
			latch.countDown();
			//System.out.println("Latch decremented, current count: " + latch.getCount());
		}
		
	}
	
	public void awaitUntillLatch() {
		try {
			if (latch != null) {
				latch.await();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void awaitLatchAndReset() {
		try {
			if (latch != null) {
				latch.await();
			}
			synchronized (this) {
				resetFlag = false;
				latch = null; // Optionally set latch to null after reset
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
