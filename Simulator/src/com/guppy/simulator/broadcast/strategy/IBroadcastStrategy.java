package com.guppy.simulator.broadcast.strategy;

import com.guppy.simulator.broadcast.message.IMessage;

public interface IBroadcastStrategy {
	
	public void executeStrategy(IMessage message);

	void broadcastMessage(IMessage message);

	public void leaderBroadcast(String string);
	
	

}
