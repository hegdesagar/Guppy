package com.guppy.simulator.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.distributed.node.INode;

public abstract class AbstractNetworkSimulator{
	
	protected List<INode> nodeList;
	
	protected static Map<String, String> strategiesMap = new HashMap<String, String>();
	
	
	public AbstractNetworkSimulator() {
		// TODO Auto-generated constructor stub
	}

	
	protected IBroadcastStrategy createObject(String strategyString, int noOfNodes, int faults) {
		String fullyQualifiedStrategyName = strategiesMap.get(strategyString);

		IBroadcastStrategy object = null;
		try {
			Class<?> classDefinition = Class.forName(fullyQualifiedStrategyName);
			Constructor<?> constructor = classDefinition.getConstructor(int.class, int.class);
			object = (IBroadcastStrategy) constructor.newInstance(noOfNodes, faults);
		} catch (InstantiationException e) {
			System.out.println(e);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	protected static void populateStrategies() {

		strategiesMap.put("AuthenticatedEchoBroadcast",
				"com.guppy.simulator.broadcast.strategy.AuthenticatedEchoBroadcastStrategy");

	}

}
