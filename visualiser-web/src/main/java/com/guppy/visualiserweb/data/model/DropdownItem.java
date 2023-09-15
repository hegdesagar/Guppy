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
 * Represents a dropdown item data model that holds broadcasting strategies.
 * Each dropdown item has a value (usually for backend use) and a label (for display).
 * 
 * @author HegdeSagar
 */
public class DropdownItem {
	
	/** The backend value associated with the dropdown item */
	private String value;
	/** The label or name to display on the frontend */
	private String label;
	
	/**
     * Constructor initializing the dropdown item with given value and label.
     * 
     * @param value The backend value associated with the dropdown item.
     * @param label The label or name to display on the frontend.
     */
	public DropdownItem(String value, String label){
		this.value = value;
		this.label = label;
	}
	
	/**
     * Retrieves the back end value of the dropdown item.
     * 
     * @return The back end value associated with the dropdown item.
     */
	public String getValue() {
		return value;
	}

	 /**
     * Sets the backend value of the dropdown item.
     * 
     * @param value The backend value to be associated with the dropdown item.
     */
	public void setValue(String value) {
		this.value = value;
	}

	/**
     * Retrieves the display label of the dropdown item.
     * 
     * @return The label or name of the dropdown item.
     */
	public String getLabel() {
		return label;
	}

	/**
     * Sets the display label for the dropdown item.
     * 
     * @param label The label or name to be set for the dropdown item.
     */
	public void setLabel(String label) {
		this.label = label;
	}

}
