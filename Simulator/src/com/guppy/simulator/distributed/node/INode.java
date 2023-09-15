package com.guppy.simulator.distributed.node;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.NodeId;

public interface INode extends  Runnable {

	void publishMessage(IMessage msg) throws InterruptedException;

	public void stop();
	
	public NodeId getNodeId();
	
	public boolean isLeader();

	void setInterrupt(boolean b);

	void injectFlooding(boolean b);

	void injectDropMessage(boolean b);

	void injectMessageTampering(boolean b);
	

}



