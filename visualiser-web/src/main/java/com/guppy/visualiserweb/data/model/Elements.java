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

import java.util.List;

/**
 * Represents the elements of a graph, including both nodes and edges.
 * 
 * @author HegdeSagar
 */
public class Elements {

	/**
     * List of nodes in the graph.
     */
	private List<Node> nodes;

    /**
     * List of edges in the graph.
     */
	private List<Edge> edges;

    /**
     * Default constructor. Initializes empty lists for nodes and edges.
     */
	public Elements() {
		//Empty constructor
	}

	/**
     * Constructor to initialize the elements with specified nodes and edges.
     *
     * @param nodes2 List of nodes to be initialized.
     * @param edges2 List of edges to be initialized.
     */
	public Elements(List<Node> nodes2, List<Edge> edges2) {
		this.nodes = nodes2;
		this.edges = edges2;
	}

	/**
     * Returns the list of nodes in the graph.
     *
     * @return List of nodes.
     */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
     * Sets the list of nodes for the graph.
     *
     * @param nodes List of nodes to be set.
     */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	/**
     * Returns the list of edges in the graph.
     *
     * @return List of edges.
     */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
     * Sets the list of edges for the graph.
     *
     * @param edges List of edges to be set.
     */
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

}
