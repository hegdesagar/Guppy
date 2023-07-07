package com.guppy.simulator.common.typdef;

import java.time.LocalDateTime;

public class TimeStamp {

	private LocalDateTime timeStamp;

	public TimeStamp(LocalDateTime timeStamp) {
		
		this.timeStamp = timeStamp;

	}
	
	/*private String getYear() {
		//this.timeStamp.get
	}*/
	
	public String toString() {

		return timeStamp.toString();

	}

}
