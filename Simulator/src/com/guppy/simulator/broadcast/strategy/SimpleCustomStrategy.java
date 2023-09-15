package com.guppy.simulator.broadcast.strategy;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.strategy.annotation.BroadCastStrategy;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.producermq.KafkaMessageProducer;


@BroadCastStrategy("user-defined-test-algorithm")
public class SimpleCustomStrategy implements IBroadcastStrategy {

	@Override
	public boolean executeStrategy(IMessage message, long latency) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean leaderBroadcast(MessageContent content) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDelivered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flood() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDropping() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startMessageTamper() {
		// TODO Auto-generated method stub

	}

	@Override
	public void publishNotDelivered(IMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMQProducer(KafkaMessageProducer producer) {
		// TODO Auto-generated method stub

	}

}
