package com.guppy.visualiserweb.data.model;

import java.util.List;

public class Elements {

	private List<Node> nodes;
	private List<Edge> edges;

	public Elements() {
	}

	public Elements(List<Node> nodes2, List<Edge> edges2) {
		this.nodes = nodes2;
		this.edges = edges2;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

}
