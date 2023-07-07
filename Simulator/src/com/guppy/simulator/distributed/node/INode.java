package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;

import com.guppy.simulator.broadcast.message.IMessage;

public interface INode {

	BlockingQueue<IMessage> getMessageQueue();

}
