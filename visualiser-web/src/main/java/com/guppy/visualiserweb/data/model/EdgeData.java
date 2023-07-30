package com.guppy.visualiserweb.data.model;

public class EdgeData {

	private String id;
	private int weight;
	private String source;
	private String target;

	public EdgeData(String edgeId, int wgt, String nodeA, String nodeB) {
		this.id = edgeId;
		this.weight = wgt;
		this.source = nodeA;
		this.target = nodeB;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
