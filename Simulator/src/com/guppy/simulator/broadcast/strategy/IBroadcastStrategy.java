package com.guppy.simulator.broadcast.strategy;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;

public interface IBroadcastStrategy {

	void executeStrategy(IMessage message) throws Exception;

	void broadcastMessage(IMessage message);

	void leaderBroadcast(MessageContent content);

	NodeId getNodeId();

	void setNodeId(NodeId nodeId);

}
