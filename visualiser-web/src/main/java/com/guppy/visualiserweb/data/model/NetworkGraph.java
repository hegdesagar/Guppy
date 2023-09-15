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
 * Represents a network graph consisting of nodes and edges. This class
 * holds all the elements that form the structure and behavior of the graph.
 * 
 * @author HegdeSagar
 */
public class NetworkGraph {
	
	 /**
     * The core components of the network graph, which include nodes and edges.
     */
	private Elements elements;
	
	/**
     * Retrieves the elements (nodes and edges) of the network graph.
     * 
     * @return The elements of the graph.
     */
	public Elements getElements() {
		return elements;
	}

	/**
     * Sets the elements (nodes and edges) for the network graph.
     *
     * @param elements The elements to set for the graph.
     */
	public void setElements(Elements elements) {
		this.elements = elements;
	}

}
