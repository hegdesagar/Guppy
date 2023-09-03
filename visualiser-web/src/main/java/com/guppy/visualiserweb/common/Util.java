package com.guppy.visualiserweb.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.guppy.simulator.broadcast.strategy.annotation.BroadCastStrategy;
import com.guppy.visualiserweb.data.model.DropdownItem;

public class Util {
	
	public static List<DropdownItem> getRegisteredBroadcastingStrategies() {

		List<DropdownItem> itemList = new ArrayList<>();

		Reflections reflections = new Reflections("com.guppy.simulator.broadcast.strategy");
		Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(BroadCastStrategy.class);

		for (Class<?> clazz : annotatedClasses) {
			BroadCastStrategy annotation = clazz.getAnnotation(BroadCastStrategy.class);
			DropdownItem item = new DropdownItem(clazz.getName(),annotation.value());
			//System.out.println("Found strategy: " + annotation.value());
			itemList.add(item);
		}
		return itemList;
	}

}
