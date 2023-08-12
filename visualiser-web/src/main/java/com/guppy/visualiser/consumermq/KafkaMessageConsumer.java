package com.guppy.visualiser.consumermq;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaMessageConsumer {
	
    public void consume(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:29092");  // Broker address updated
        properties.put("group.id", "guppy-consumer-group-id");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // To consume messages from the start

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("BROADCAST-CHANNEL")); //TODO

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));  // This is a blocking call
            records.forEach(record -> {
                System.out.printf("Consumed record with key %s and value %s%n", record.key(), record.value());
            });
        }
    }
}

