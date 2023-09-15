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

/**
 * Represents the data associated with an edge in a graph. Contains properties
 * such as ID, weight, source, and target nodes.
 * 
 * @author HegdeSagar
 */
public class EdgeData {

	/**
	 * The unique identifier of the edge.
	 */
	private String id;

	/**
	 * Weight of the edge. This could represent distance, cost, etc.
	 */
	private int weight;

	/**
	 * Identifier of the source node of this edge.
	 */
	private String source;

	/**
	 * Identifier of the target node of this edge.
	 */
	private String target;

	/**
	 * Constructor to initialize the edge data.
	 *
	 * @param edgeId The unique identifier for the edge.
	 * @param wgt    Weight of the edge.
	 * @param nodeA  Identifier for the source node.
	 * @param nodeB  Identifier for the target node.
	 */
	public EdgeData(String edgeId, int wgt, String nodeA, String nodeB) {
		this.id = edgeId;
		this.weight = wgt;
		this.source = nodeA;
		this.target = nodeB;
	}

	/**
	 * Returns the edge's identifier.
	 *
	 * @return The unique identifier of the edge.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the edge's identifier.
	 *
	 * @param id The unique identifier for the edge.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the weight of the edge.
	 *
	 * @return Weight of the edge.
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Sets the weight of the edge.
	 *
	 * @param weight The weight to be assigned to the edge.
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * Returns the source node's identifier of this edge.
	 *
	 * @return Identifier of the source node.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source node's identifier for this edge.
	 *
	 * @param source Identifier of the source node to be set.
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns the target node's identifier of this edge.
	 *
	 * @return Identifier of the target node.
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets the target node's identifier for this edge.
	 *
	 * @param target Identifier of the target node.
	 */
	public void setTarget(String target) {
		this.target = target;
	}

}
