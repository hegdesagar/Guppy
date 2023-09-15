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
package com.guppy.visualiserweb.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.guppy.simulator.broadcast.strategy.annotation.BroadCastStrategy;
import com.guppy.visualiserweb.data.model.DropdownItem;

/**
 * A utility class providing helper methods related to various operations within the application.
 * 
 * @author HegdeSagar
 */
public class Util {
	
	/**
     * Retrieves a list of broadcasting strategies registered in the system.
     * This method uses reflection to scan for classes annotated with {@link BroadCastStrategy}
     * within the specified package and constructs a list of dropdown items representing each strategy.
     * 
     * @return a list of {@link DropdownItem} objects representing the registered broadcasting strategies.
     */
	public static List<DropdownItem> getRegisteredBroadcastingStrategies() {

		List<DropdownItem> itemList = new ArrayList<>();
		
		//fetch the list of broadcasting strategies listed under the annotation
		Reflections reflections = new Reflections("com.guppy.simulator.broadcast.strategy");
		Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(BroadCastStrategy.class);

		for (Class<?> clazz : annotatedClasses) {
			BroadCastStrategy annotation = clazz.getAnnotation(BroadCastStrategy.class);
			DropdownItem item = new DropdownItem(clazz.getName(),annotation.value());
			itemList.add(item);
		}
		return itemList;
	}

}
