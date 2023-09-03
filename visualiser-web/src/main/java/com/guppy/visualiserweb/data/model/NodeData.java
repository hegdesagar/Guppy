package com.guppy.visualiserweb.data.model;

public class NodeData {
	
	private String id;
	
	private String label;
	
	NodeData(){
		
	}
	
	public NodeData(String nodeId, String label){
		this.id = nodeId;
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
