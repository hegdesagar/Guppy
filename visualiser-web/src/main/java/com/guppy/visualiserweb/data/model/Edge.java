package com.guppy.visualiserweb.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Edge {
	
	private EdgeData data;

	public Edge(EdgeData data) {
		this.data = data;
	}

	@JsonProperty("data")
	public EdgeData getEdgeData() {
		return data;
	}

	public void setEdgeData(EdgeData data) {
		this.data = data;
	}

}
