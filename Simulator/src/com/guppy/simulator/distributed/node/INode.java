package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;

import com.guppy.simulator.broadcast.message.IMessage;

public interface INode extends Runnable {

	BlockingQueue<IMessage> getMessageQueue();

	void setLeader(boolean isLeader);

	void injectFault();

}
