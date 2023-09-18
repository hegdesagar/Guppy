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
package com.guppy.simulator.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.distributed.node.INode;

/*
 * Abstract class for NetworkSimulator
 * @author HegdeSagar
 */
public abstract class AbstractNetworkSimulator{
	
	protected List<INode> nodeList;
	
	protected static Map<String, String> strategiesMap = new HashMap<String, String>();
	
	
	public AbstractNetworkSimulator() {
		// Empty Constructor
	}

	/*
	 * Create the instance of the strategy and return it
	 */
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
	


}
