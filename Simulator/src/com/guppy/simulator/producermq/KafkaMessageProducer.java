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
package com.guppy.simulator.producermq;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guppy.simulator.broadcast.events.BroadcastEvent;
import com.guppy.simulator.core.NetworkSimulator;


/**
 * A producer that publishes messages to a Kafka topic. This producer is specifically
 * designed to send {@code BroadcastEvent} objects to the "BROADCAST-CHANNEL" topic.
 * 
 * <p>The class leverages the Apache Kafka library for message production and 
 * the Jackson library for JSON serialization of events. Configuration settings for
 * the Kafka producer, such as the Kafka server location and serialization classes,
 * are hardcoded but can be modified as necessary.</p>
 *
 * @see BroadcastEvent
 */
public class KafkaMessageProducer {
    
    /** The core Kafka producer instance. */
    private final Producer<String, String> producer;
    
    /** Counter to keep track of unique message IDs. */
    private static long idCounter = 0;
    
    /** Jackson object mapper for converting objects to JSON. */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Initializes a new Kafka producer with predefined properties.
     */
    public KafkaMessageProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:29092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("max.request.size", 2097152); // set the size to 2MB
        this.producer = new KafkaProducer<>(properties);
    }

    /**
     * Produces a {@code BroadcastEvent} to the "BROADCAST-CHANNEL" Kafka topic.
     * 
     * @param event The event to be published.
     */
    public void produce(BroadcastEvent event) {
        try {
            String key = String.valueOf(idCounter++);
            String eventAsJson = mapper.writeValueAsString(event);
            producer.send(new ProducerRecord<>("BROADCAST-CHANNEL", key, eventAsJson));
            // Introducing latency
            Thread.sleep(NetworkSimulator.getInstance().getNetworkLatency());
            
        } catch (JsonProcessingException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the Kafka producer, releasing any resources it might hold.
     */
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }
}



