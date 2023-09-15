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
 * Represents a message that carries information about the timeline.
 * This class encapsulates the timeline duration or position for a specific simulation or event.
 * 
 * @author HegdeSagar
 */
public class TimelineMessage {
	/**
     * The duration or position of the timeline.
     */
    private int timeline;

    /**
     * Retrieves the timeline value.
     * 
     * @return The current timeline duration or position.
     */
    public int getTimeline() {
        return timeline;
    }

    /**
     * Sets the timeline value.
     *
     * @param timeline The timeline duration or position to set.
     */
    public void setTimeline(int timeline) {
        this.timeline = timeline;
    }
}
