package com.guppy.visualiserweb.data.model;

public class NetworkGraph {
	
	private Elements elements;
	
	private String highlightedNode;
	
	private String leaderNode;
	
	private String highlightSendEdge;
	
	private String highlightEchoEdge;
	
	
	public String getHighlightedNode() {
		return highlightedNode;
	}

	public void setHighlightedNode(String highlightedNode) {
		this.highlightedNode = highlightedNode;
	}

	public String getLeaderNode() {
		return leaderNode;
	}

	public void setLeaderNode(String leaderNode) {
		this.leaderNode = leaderNode;
	}

	public String getHighlightSendEdge() {
		return highlightSendEdge;
	}

	public void setHighlightSendEdge(String highlightSendEdge) {
		this.highlightSendEdge = highlightSendEdge;
	}

	public String getHighlightEchoEdge() {
		return highlightEchoEdge;
	}

	public void setHighlightEchoEdge(String highlightEchoEdge) {
		this.highlightEchoEdge = highlightEchoEdge;
	}

	public Elements getElements() {
		return elements;
	}

	public void setElements(Elements elements) {
		this.elements = elements;
	}

}
