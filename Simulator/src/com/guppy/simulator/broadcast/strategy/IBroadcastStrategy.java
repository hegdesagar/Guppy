package com.guppy.simulator.broadcast.strategy;

import java.util.List;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;

public interface IBroadcastStrategy {

	boolean executeStrategy(IMessage message) throws Exception;

	boolean leaderBroadcast(MessageContent content);

	void setNodeId(NodeId nodeId);

	boolean isDelivered();

	void close();

	void reset();

	//void reset();


}
