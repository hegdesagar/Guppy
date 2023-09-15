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
package com.guppy.visualiser.consumermq;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * A Kafka consumer class that subscribes to the "BROADCAST-CHANNEL" topic.
 * @author HegdeSagar
 */
public class KafkaMessageConsumer {

	/** Kafka consumer properties. */
	private final Properties properties;
	/** Kafka consumer instance. */
	private final Consumer<String, String> consumer;

	/**
     * Default constructor initializes the Kafka consumer with necessary properties.
     */
	public KafkaMessageConsumer() {
		properties = new Properties();
		properties.put("bootstrap.servers", "localhost:29092"); 
		properties.put("group.id", "guppy-consumer-group-id");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); 

		consumer = new KafkaConsumer<>(properties);
		consumer.subscribe(Arrays.asList("BROADCAST-CHANNEL"));
	}

    /**
     * Consumes messages from the Kafka topic.
     *
     * @return Iterable of consumed records.
     */
	public Iterable<ConsumerRecord<String, String>> consume() {
		var records = consumer.poll(Duration.ofMillis(100)); // This is a blocking call
		return records;
	}

    /**
     * Closes the Kafka consumer.
     */
	public void close() {
		consumer.close();
	}
}