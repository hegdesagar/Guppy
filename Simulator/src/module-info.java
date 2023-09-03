module Simulator {
	requires com.rabbitmq.client;

	requires com.fasterxml.jackson.databind;
	requires org.slf4j;
	requires kafka.clients;

	exports com.guppy.simulator.broadcast.events to com.fasterxml.jackson.databind;

	exports com.guppy.simulator.common.typdef to com.fasterxml.jackson.databind;

	exports com.guppy.simulator.broadcast.strategy.annotation to com.guppy.visualiserweb.controller.rest;
}