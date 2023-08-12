package com.guppy.simulator.producermq;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.guppy.simulator.broadcast.events.BroadcastEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // You'll need the Jackson library for JSON serialization

public class KafkaMessageProducer {
	
	private final Producer<String, String> producer;
	private static long idCounter = 0;
	private static final ObjectMapper mapper = new ObjectMapper();

	public KafkaMessageProducer() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:29092");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		this.producer = new KafkaProducer<>(properties);
	}

	public void produce(BroadcastEvent event) {
		try {
			String key = String.valueOf(idCounter++);
			String eventAsJson = mapper.writeValueAsString(event);
			producer.send(new ProducerRecord<>("BROADCAST-CHANNEL", key, eventAsJson));
			System.out.println("Message with ID: " + key + " sent successfully!");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (producer != null) {
			producer.close();
		}
	}

}

