package com.guppy.simulator.distributed.node;

import java.util.concurrent.Callable;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.NodeId;

public interface INode extends  Callable<Boolean> {

	//BlockingQueue<IMessage> getMessageQueue();
	
	void publishMessage(IMessage msg) throws InterruptedException;

	void setLeader(boolean isLeader);
	
	public void stop();
	
	public NodeId getNodeId();

	void reset();

	Boolean isDelivered();


}
