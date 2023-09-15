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
 * Represents an edge in a graph structure. 
 * An edge is defined by its associated data, which is represented by the {@code EdgeData} class.
 * 
 * @author HegdeSagar
 */
public class Edge {
	
	/** 
     * The associated data of the edge, which holds details such as the source, target, and other attributes.
     */
	private EdgeData data;

	/**
     * Constructs an edge with the provided associated data.
     * 
     * @param data The data associated with the edge.
     */
	public Edge(EdgeData data) {
		this.data = data;
	}

	/**
     * Retrieves the associated data of the edge.
     * 
     * @return The data of the edge.
     */
	@JsonProperty("data")
	public EdgeData getEdgeData() {
		return data;
	}

	/**
     * Sets the associated data for the edge.
     * 
     * @param data The data to be associated with the edge.
     */
	public void setEdgeData(EdgeData data) {
		this.data = data;
	}

}
