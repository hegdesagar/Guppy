package com.guppy.simulator.broadcast.strategy;

import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.producermq.KafkaMessageProducer;

public abstract class AbstractBroadcastStrategy {

	protected int N; // Number of nodes
	protected int F; // Maximum number of faulty nodes
	protected NodeId nodeId;
	
	protected KafkaMessageProducer producer;
	
	
	protected boolean maybeDontSendMessage = false;
	protected boolean maybeSendRedundantMessages = false;
	protected boolean maybeAlterMessageContent = false;

	AbstractBroadcastStrategy(int _N, int _f, NodeId nodeId){
		this.N = _N;
		this.F = _f;
		this.nodeId = nodeId;
	}

	/**
	 * Generates a unique message ID.
	 * 
	 * @return the generated message ID
	 */
	protected AtomicLong generateIteration() {
		AtomicLong idCounter = new AtomicLong();
		return idCounter;
	}
	
	public void close() {
		if (producer != null) {
			producer.close();
		}
	}
	
	public void setMQProducer(KafkaMessageProducer mqProducer) {
		this.producer = mqProducer;
		
	}
	
	public void flood() {
		this.maybeSendRedundantMessages = true;

	}

	public void startDropping() {
		this.maybeDontSendMessage = true;
	}

	public void startMessageTamper() {
		this.maybeAlterMessageContent = true;
	}

	

}
