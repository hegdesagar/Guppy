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
 * Represents the core data associated with a node in a network graph.
 * This class encapsulates the identifying information and any associated
 * labels for individual nodes.
 * 
 * @author HegdeSagar
 */
public class NodeData {
	
	/**
     * The unique identifier for the node.
     */
    private String id;

    /**
     * The descriptive label for the node.
     */
    private String label;

    /**
     * Default constructor. Initializes a new instance of the NodeData class.
     */
	NodeData(){
		//Empty constructor
	}
	
	/**
     * Constructs a NodeData instance with the provided node ID and label.
     * 
     * @param nodeId The unique identifier for the node.
     * @param label  The descriptive label for the node.
     */
	public NodeData(String nodeId, String label){
		this.id = nodeId;
		this.label = label;
	}

	/**
    * Retrieves the unique identifier of the node.
    * 
    * @return The node's unique identifier.
    */
	public String getId() {
		return id;
	}

	/**
     * Sets the unique identifier for the node.
     *
     * @param id The identifier to set for the node.
     */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
     * Retrieves the descriptive label of the node.
     * 
     * @return The node's label.
     */
	public String getLabel() {
		return label;
	}

	/**
     * Sets the descriptive label for the node.
     *
     * @param label The label to set for the node.
     */
	public void setLabel(String label) {
		this.label = label;
	}

}
