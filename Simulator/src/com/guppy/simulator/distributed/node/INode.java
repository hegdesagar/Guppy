package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import com.guppy.simulator.broadcast.message.IMessage;

public interface INode extends  Callable<Boolean> {

	BlockingQueue<IMessage> getMessageQueue();

	void setLeader(boolean isLeader);

	void injectFault();

}
