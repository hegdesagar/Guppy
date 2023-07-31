package com.guppy.visualiserweb.data.model;

public class SimulationOptions {
	private Integer nodes;
	private String implementation;
	private Integer timeline; // Added this line

	public Integer getNodes() {
		return nodes;
	}

	public void setNodes(Integer nodes) {
		this.nodes = nodes;
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public Integer getTimeline() { // Added this method
		return timeline;
	}

	public void setTimeline(Integer timeline) { // Added this method
		this.timeline = timeline;
	}
}
