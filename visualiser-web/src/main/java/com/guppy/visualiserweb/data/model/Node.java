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
package com.guppy.visualiserweb.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a node within a network graph. This class encapsulates the data
 * and behavior associated with individual nodes in the graph.
 * 
 * @author HegdeSagar
 */
public class Node {

	/**
	 * The core data associated with the node.
	 */
	private NodeData data;

	/**
	 * Constructs a Node instance with the provided node data.
	 * 
	 * @param data The data to initialize the node with.
	 */
	public Node(NodeData data) {
		this.data = data;
	}

	 /**
     * Retrieves the data associated with the node.
     * 
     * @return The data of the node.
     */
	@JsonProperty("data")
	public NodeData getNodeData() {
		return data;
	}

	 /**
     * Sets the data for the node.
     *
     * @param data The data to set for the node.
     */
	public void setNodeData(NodeData data) {
		this.data = data;
	}

}
