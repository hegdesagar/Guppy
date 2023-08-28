package com.guppy.visualiser.consumermq.data;

import java.util.List;

/**
 * @author SagarH
 *
 */
public class MQRecord {

	private List<NodeName> nodeNames;
	private NodeId senderId;
	private NodeId receiverId;
	private String eventType;
	private long timeStamp;
	private String leaderNode;
	private String edgeHighlight;

	public static class NodeName {
		private String id;

		public String getId() {
			// TODO Auto-generated method stub
			return id;
		}
	}

	public static class NodeId {
		private String id;

		public String getId() {
			// TODO Auto-generated method stub
			return id;
		}
	}

	public List<NodeName> getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(List<NodeName> nodeNames) {
		this.nodeNames = nodeNames;
	}

	public NodeId getSenderId() {
		return senderId;
	}

	public void setSenderId(NodeId senderId) {
		this.senderId = senderId;
	}

	public NodeId getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(NodeId receiverId) {
		this.receiverId = receiverId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getLeaderNode() {
		return leaderNode;
	}
	public void setLeaderNode(String node) {
		this.leaderNode = node;
	}

	public void setEdgeHighlight(String construtedEdge) {
		this.edgeHighlight = construtedEdge;
		
	}
	public String getEdgeHighlight() {
		return edgeHighlight;
	}

}
