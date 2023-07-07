package com.guppy.simulator.common.typdef;

/**
 * Represents a custom data type for a message ID.
 * 
 * @author HegdeSagar
 */
public class MessageId {

	private String id;

	/**
	 * Constructs a message ID with the specified ID value.
	 * 
	 * @param id the ID value of the message ID
	 */
	public MessageId(String id) {
		this.id = id;
	}

	/**
	 * Returns the string representation of the message ID.
	 * 
	 * @return the string representation of the message ID
	 */
	public String toString() {

		return id;

	}
}