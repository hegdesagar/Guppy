package com.guppy.visualiserweb.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Node {
	
	private NodeData data;
	
	public Node(NodeData data){
		this.data = data;
	}

	@JsonProperty("data")
	public NodeData getNodeData() {
		return data;
	}

	public void setNodeData(NodeData data) {
		this.data = data;
	}

}
