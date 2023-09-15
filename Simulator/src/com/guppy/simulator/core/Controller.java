package com.guppy.simulator.core;

import java.util.concurrent.CountDownLatch;

/**
 * Controller class to manage and control the reset mechanism for nodes.
 * It leverages the CountDownLatch to synchronize the reset operations
 * among multiple nodes.
 * 
 * @author HegdeSagar
 */
public class Controller {

    /** A flag to indicate if reset operation is in progress. */
    private volatile boolean resetFlag = false;

    /** Latch to synchronize reset operations among nodes. */
    private CountDownLatch latch;

    /**
     * Initiates the reset operation by setting the resetFlag and initializing the latch.
     *
     * @param numberOfNodes The total number of nodes involved in the reset operation.
     */
    public synchronized void initiateReset(int numberOfNodes) {
        this.resetFlag = true;
        this.latch = new CountDownLatch(numberOfNodes);
    }

    /**
     * Checks if the reset operation is currently in progress.
     *
     * @return true if reset is in progress, false otherwise.
     */
    public synchronized boolean isResetInProgress() {
        return resetFlag;
    }

    /**
     * Decrements the latch count, indicating a node has completed its reset operation.
     */
    public synchronized void decrementLatch() {
        if (latch != null) {
            latch.countDown();
        }
    }

    /**
     * Blocks the current thread until all nodes have completed their reset operation,
     * unless the thread is interrupted.
     */
    public void awaitUntillLatch() {
        try {
            if (latch != null) {
                latch.await();
            }
        } catch (InterruptedException e) {
            // Restore the interrupted status of the thread
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Blocks the current thread until all nodes have completed their reset operation.
     * After all nodes are reset, it will reset the Controller's state.
     */
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
            // Restore the interrupted status of the thread
            Thread.currentThread().interrupt();
        }
    }
}

