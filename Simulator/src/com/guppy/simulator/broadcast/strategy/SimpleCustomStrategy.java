/*
====================================================
Copyright (c) 2023 SagarH
All Rights Reserved.
Permission to use, copy, modify, and distribute this software and its
documentation for any purpose, without fee, and without a written agreement is hereby granted, 
provide that the above copyright notice and this paragraph and the following two paragraphs appear in all copies.

IN NO EVENT SHALL YOUR NAME BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING
OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF YOU HAVE BEEN
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

SagarH SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND YOUR NAME HAS NO
OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
====================================================
*/
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
