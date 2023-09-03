package com.guppy.simulator.broadcast.strategy;

import java.util.List;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.producermq.KafkaMessageProducer;

public interface IBroadcastStrategy {

	boolean executeStrategy(IMessage message, long latency) throws Exception;

	boolean leaderBroadcast(MessageContent content);

	//void setNodeId(NodeId nodeId);

	boolean isDelivered();

	void close();

	void reset();

	void flood();

	void startDropping();

	void startMessageTamper();

	void publishNotDelivered(IMessage message);

	void setMQProducer(KafkaMessageProducer producer);

	//void reset();


}
