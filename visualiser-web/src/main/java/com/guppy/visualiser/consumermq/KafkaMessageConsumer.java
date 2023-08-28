package com.guppy.visualiser.consumermq;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public class KafkaMessageConsumer {

	private final Properties properties;
	private final Consumer<String, String> consumer;

	public KafkaMessageConsumer() {
		properties = new Properties();
		properties.put("bootstrap.servers", "localhost:29092"); // Broker address updated
		properties.put("group.id", "guppy-consumer-group-id");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // To consume messages from the start

		consumer = new KafkaConsumer<>(properties);
		consumer.subscribe(Arrays.asList("BROADCAST-CHANNEL"));
	}

	/*
	 * public Optional<ConsumerRecord<String, String>> consume() { var records =
	 * consumer.poll(Duration.ofMillis(100)); // This is a blocking call if
	 * (records.count() > 0) { return Optional.of(records.iterator().next()); }
	 * return Optional.empty(); }
	 */

	public Iterable<ConsumerRecord<String, String>> consume() {
		var records = consumer.poll(Duration.ofMillis(100)); // This is a blocking call
		return records;
	}

	public void close() {
		consumer.close();
	}
}