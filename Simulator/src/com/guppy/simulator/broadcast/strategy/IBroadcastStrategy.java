package com.guppy.simulator.broadcast.strategy;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.MessageContent;

public interface IBroadcastStrategy {
	
	void executeStrategy(IMessage message);

	void broadcastMessage(IMessage message);

	void leaderBroadcast(MessageContent content);
	

}
