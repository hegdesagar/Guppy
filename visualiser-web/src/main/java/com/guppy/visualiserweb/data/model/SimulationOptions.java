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
 * Represents the configuration options for a network simulation.
 * This class encapsulates settings such as the number of nodes,
 * the implementation type, the simulation timeline, and the number 
 * of faults to be introduced during the simulation.
 * 
 * @author HegdeSagar
 */
public class SimulationOptions {
	 /**
     * The number of nodes in the simulation.
     */
    private Integer nodes;

    /**
     * The type of implementation to be used in the simulation.
     */
    private String implementation;

    /**
     * The simulation timeline duration.
     */
    private Integer timeline;

    /**
     * The number of faults to be introduced in the simulation.
     */
    private Integer faults;

    /**
     * Retrieves the number of faults for the simulation.
     *
     * @return The number of faults.
     */
	public Integer getFaults() {
		return faults;
	}

	 /**
     * Sets the number of faults for the simulation.
     *
     * @param faults The number of faults to set.
     */
	public void setFaults(Integer faults) {
		this.faults = faults;
	}

	/**
     * Retrieves the number of nodes for the simulation.
     *
     * @return The number of nodes.
     */
	public Integer getNodes() {
		return nodes;
	}

	/**
     * Sets the number of nodes for the simulation.
     *
     * @param nodes The number of nodes to set.
     */
	public void setNodes(Integer nodes) {
		this.nodes = nodes;
	}

	/**
     * Retrieves the type of implementation for the simulation.
     *
     * @return The type of implementation.
     */
	public String getImplementation() {
		return implementation;
	}

	 /**
     * Sets the type of implementation for the simulation.
     *
     * @param implementation The type of implementation to set.
     */
	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	/**
     * Retrieves the timeline duration of the simulation.
     *
     * @return The simulation timeline duration.
     */
	public Integer getTimeline() { 
		return timeline;
	}

	/**
     * Sets the timeline duration for the simulation.
     *
     * @param timeline The timeline duration to set.
     */
	public void setTimeline(Integer timeline) { 
		this.timeline = timeline;
	}
}
