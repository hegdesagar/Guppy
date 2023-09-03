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


//Load the graph and other details once the page loads with dummy data
window.onload = function() {

	//fetch the elements for dropdown
	fetch('http://localhost:8080/api/broadcasting_impl')
		.then(response => response.json())
		.then(data => {
			const dropdownMenu = document.getElementById('implementation');
			dropdownMenu.innerHTML = ''; // Clear previous items

			data.forEach(item => {
				const listItem = document.createElement('li');
				const link = document.createElement('a');
				link.className = 'dropdown-item';
				link.href = "#"; // or any specific link
				link.innerText = item.label;
				link.id = item.value; // Setting the ID to the value

				listItem.appendChild(link);
				dropdownMenu.appendChild(listItem);
			});

			addDropdownClickListeners();
		})
		.catch(error => {
			console.error('There was an error fetching the dropdown data:', error);
		});


	//Default data for cyptoscape
	const elements = {
		nodes: [
			{ data: { id: 'a', label: 'leader' } },
			{ data: { id: 'b', label: '' } },
			{ data: { id: 'c', label: '' } }
		],

		edges: [
			{ data: { id: 'ab', weight: 1, source: 'a', target: 'b', arrow: 'triangle', label: 'ab' } },
			{ data: { id: 'ac', weight: 1, source: 'a', target: 'c', arrow: 'triangle', label: 'ac' } },
			{ data: { id: 'ba', weight: 1, source: 'b', target: 'a', arrow: 'triangle', label: 'ba' } },
			{ data: { id: 'bc', weight: 1, source: 'b', target: 'c', arrow: 'triangle', label: '' } },
			{ data: { id: 'ca', weight: 1, source: 'c', target: 'a', arrow: 'triangle', label: '' } },
			{ data: { id: 'cb', weight: 1, source: 'c', target: 'b', arrow: 'triangle', label: '' } }
		]
	};
	resetStyles();
	document.getElementById('mainalert').innerText = "Welcome to Node Broadcasting simulator";
	drawGraph(elements, '#a');
	highlightSpecificElements('a', 'b', 'ab', 'send');
	cy.getElementById('a').addClass('leader');

	//event box
	const eventText = 'a' + '->' + 'c' + '\n' + 'SEND';
	//const eventText = 'node-0 ' + '->' + ' node-2' + '\n' + 'SEND';
	addEvent(eventText, 'red');
}

function addDropdownClickListeners() {
	document.querySelectorAll('.dropdown-menu .dropdown-item').forEach(function(item) {
		item.addEventListener('click', function(event) {
			event.preventDefault();
			const selectedItemText = event.target.textContent;
			document.getElementById('dropdownMenuButton').textContent = selectedItemText;
			console.log('Selected Implementation:', selectedItemText);
		});
	});
}


// define the cy variable without initializing
let cy = null;

let started = false;


//Crashed Nodes
const nodesCrashed = new Array();
const nodesByzantine = new Array();

//Websocket defination START
const stompClient = new StompJs.Client({
	brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

let simulateDataSubscription;
let highlightNodesSubscription;
// Subscription to alert_updates topic
let alertSubscription;
let errorSubscription;

let deliveredCount = 0;
let notDeliveredCount = 0;

stompClient.onConnect = (frame) => {
	console.log('Connected: ' + frame);

	startThroughputMonitoring();

	simulateDataSubscription = stompClient.subscribe('/topic/simulate_data', (greeting) => {
		//console.log(greeting.body);
		const receivedData = JSON.parse(greeting.body);
		const graphData = receivedData.elements;
		//const stringData = JSON.stringify(graphData);
		console.log('graphdata', graphData);
		const alertElement = document.getElementById('mainalert');
		alertElement.innerText = `Simulation Started`;
		alertElement.classList.add('alert-info');
		drawGraph(graphData, '#node-0');

		//publish the latency and throughput graph
		/*const nodeLists = graphData.nodes;
		nodeLists.forEach(node => {
			console.log(node.data.id);
			addNodeDataToChart(node.data.id, [5,6,8,9], "blue");
		});*/

	});


	highlightNodesSubscription = stompClient.subscribe('/topic/highlight_nodes', (message) => {
		// First, reset the styles for all nodes and edges to their base styles
		hideAllEdges();
		let mqRecord = JSON.parse(message.body); // This will now be an array

		const senderNode = mqRecord.senderId;
		const receiverNode = mqRecord.receiverId;
		const edgeHighlight = mqRecord.edgeHighlight;
		const eventType = mqRecord.eventType;
		const leadernode = mqRecord.leaderNode;

		//update throughput
		onMessageReceived();

		const eventText = eventType;

		if (eventType === 'SEND') {
			console.log("SEND Egde : ",edgeHighlight);
			cy.getElementById(edgeHighlight).style({
				'line-color': 'green',
				'target-arrow-color': 'green',
				'label': eventType
			});
			addEvent(eventText, 'green');
		}
		if (eventType === 'ECHO') {
			cy.getElementById(edgeHighlight).style({
				'line-color': 'blue',
				'target-arrow-color': 'blue',
				'label': eventType
			});
			addEvent(eventText, 'blue');
		}


		// Apply styles to leader
		cy.getElementById(leadernode).addClass('leader');
		console.log('Event: ', eventType);
		if (eventType == 'DELIVERED') {
			//cy.getElementById(leadernode).style('background-color', 'green');
			addEventToList('The Message is Delivered by Leader', 'black');
			addEvent(eventText, 'black');
		} else {
			cy.getElementById(leadernode).addClass('leader');
		}
		if (eventType == 'NOTDELIVERED') {
			//cy.getElementById(leadernode).style('background-color', 'black');
			addEventToList('The Message was not Delivered by Leader', 'red');
			addEvent(eventText, 'red');
		} else {
			cy.getElementById(leadernode).addClass('leader');
		}


		// Apply styles to edge
		/*cy.getElementById(edgeHighlight).style({
			'line-color': 'pink',
			'target-arrow-color': 'pink',
			'label': eventType
		});*/
		const edgeConstant = cy.getElementById(edgeHighlight);
		//edgeConstant.style('line-color', '#61bffc');
		//edgeConstant.style('target-arrow-color', '#61bffc');
		
		//update latency
		const latency = mqRecord.timeStamp;
		if (eventType !== "SEND" && eventType !== "DELIVERED" && eventType !== "NOTDELIVERED" && latency !== 0) {
			console.log('latency ', latency);
			updateNodeDataset(senderNode.id, latency);
		}

		//update send to deliver duration
		if (eventType === "DELIVERED") {
			updateSendToDeliverDuration(latency);
			deliveredCount = deliveredCount + 1;
			updateDeliveredCount(deliveredCount);
		} else if (eventType === "NOTDELIVERED") {
			notDeliveredCount = notDeliveredCount + 1;  // Get this from your data or logic
			updateNotDeliveredCount(notDeliveredCount);
		}
		edgeConstant.show();

	});

	alertSubscription = stompClient.subscribe('/topic/alert_updates', (alertMessage) => {
		console.log('Received alert update', alertMessage);

		const alertElement = document.getElementById('mainalert');
		alertElement.innerText = alertMessage.body;
		alertElement.classList.add('alert-info');

	});

	errorSubscription = stompClient.subscribe('/topic/error_updates', (alertMessage) => {
		console.log('Received error update', alertMessage);

		const alertElement = document.getElementById('mainalert');
		alertElement.innerText = alertMessage.body;
		alertElement.classList.add('alert-danger');
	});

	cy.on('style', 'node', function(event) {
		const node = event.target;
		if (node.data('leader')) {
			node.style('content', node.data('id') + '\nLeader');
		}
	});


};

function disconnect() {


	stopThroughputMonitoring();

	if (simulateDataSubscription) {
		simulateDataSubscription.unsubscribe();
	}

	if (highlightNodesSubscription) {
		highlightNodesSubscription.unsubscribe();
	}
	// Unsubscribe from the alert updates
	if (alertSubscription) {
		alertSubscription.unsubscribe();
	}
	if (errorSubscription) {
		errorSubscription.unsubscribe();
	}

	stompClient.deactivate();
	//document.getElementById('SimulationButton').innerHTML = "Start";
	started = false;
	console.log("Disconnected");

	const alertElement = document.getElementById('mainalert');
	alertElement.innerText = `Simulation Stopped`;
	alertElement.classList.add('alert-info');
}


stompClient.onWebSocketError = (error) => {
	console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
	console.error('Broker reported error: ' + frame.headers['message']);
	console.error('Additional details: ' + frame.body);
};

let selectedImplementation = ""; // global variable to hold the selected implementation


document.getElementById('customRange1').addEventListener('input', function() {
	document.getElementById('rangeValue').textContent = this.value;
});

document.querySelectorAll('#implementation .dropdown-item').forEach(item => {
	item.addEventListener('click', function(event) {
		event.preventDefault();
		const button = document.getElementById('dropdownMenuButton');
		button.textContent = event.target.textContent;
		console.log("Selected Implementation:", event.target.textContent);
		//console.log("Selected Implementation:", event.target.textContent);
	});
});

function publish() {
	const nodesValue = $("#nodes").val();
	const faultsValue = $("#faults").val();
	const timelineValue = $('#customRange1').val();
	//const selectedImplementation = document.querySelector('#implementation .dropdown-item').textContent;
	const selectedImplementation = document.getElementById('dropdownMenuButton').textContent;

	//initialize the latency graph nodes
	initializeNodes(nodesValue);

	let messageJson = JSON.stringify({
		'nodes': nodesValue,
		'implementation': selectedImplementation,
		'timeline': timelineValue,
		'faults': faultsValue
	});


	stompClient.publish({
		destination: "/app/simulate",
		body: messageJson
	});

	stompClient.publish({
		destination: "/app/highlight",
		body: messageJson
	});
}

$("#SimulationButton").click(function() {
	if ($(this).text() == "Start") {
		connect();
		$(this).text("Stop");
	} else {
		stopSimulation();
		disconnect();
		$(this).text("Start");
	}
});


function connect() {
	if (started) {
		disconnect();
		started = false;
		// document.getElementById('SimulationButton').innerText = "Stop";
	} else {
		stompClient.activate(); //establish connection
		//add delay to ensure connection is established before publish
		setTimeout(publish, 500);
		simulateDataSubscription = null;
		highlightNodesSubscription = null;
		started = true;
	}
}

// this event listener to handle changes to the range input
document.getElementById('customRange1').addEventListener('change', function() {
	document.getElementById('rangeValue').textContent = this.value;
	updateTimeline(this.value); // Call the function to update the timeline
});

// Function to send the new timeline value to the server
function updateTimeline(timelineValue) {
	const messageJson = JSON.stringify({ 'timeline': timelineValue });

	// Assuming you have a WebSocket endpoint to handle timeline updates
	stompClient.publish({
		destination: "/app/update_timeline",
		body: messageJson
	});
}


function highlightSpecificElements(nodeSender, nodeReceiver, edgeId, label) {
	// Highlight the node
	cy.getElementById(nodeSender).style('background-color', '#61bffc');
	cy.getElementById(nodeReceiver).style('background-color', '#61bffc');

	// Highlight and show the edge
	const edge = cy.getElementById(edgeId);
	edge.style('line-color', '#61bffc');
	edge.style('target-arrow-color', '#61bffc');
	edge.show();
}


// define a function to draw the graph
function drawGraph(elements, rootNode) {
	console.log("In Cryptoscape..");
	cy = cytoscape({
		container: document.getElementById('cy'),
		boxSelectionEnabled: false,
		autounselectify: true,
		style: cytoscape.stylesheet()
			.selector('node')
			.style({
				'content': function(ele) {
					return ele.data('id') + (ele.data('label') ? '\n' + ele.data('label') : "");
				},
				'text-wrap': 'wrap',
				'text-max-width': 80,
				'text-valign': 'center',  // Adjust based on where you want the combined content+label to appear
				'text-halign': 'center',
				'text-background-color': '#ffffff', // Optional: if you want a background for your label
				'text-background-opacity': 0.7,    // Adjust as needed
				'text-background-padding': '2px',  // Padding around the label
				'text-background-shape': 'rectangle' // Background shape
			})
			.selector('edge[label]')
			.style({
				'curve-style': 'straight',
				'target-arrow-shape': 'triangle',
				'width': 4,
				'line-color': '#ddd',
				'target-arrow-color': '#ddd',
				'label': 'data(label)',  // This line adds the label to  edge
				'text-rotation': 'autorotate'  // This ensures that the text rotation matches the edge direction
			})
			.selector('.highlighted')
			.style({
				'background-color': '#61bffc',
				'line-color': '#61bffc',
				'target-arrow-color': '#61bffc',
				'transition-property': 'background-color, line-color, target-arrow-color',
				'transition-duration': '0.5s'
			})
			.selector('.leader')  // define the leader node style
			.style({
				'background-color': '#66ff66',
				'border-width': '1px',
				'border-color': '#ff3333'
			}),

		elements: elements, // use the incoming data to set the elements

		layout: {
			//name: 'breadthfirst',
			name: 'circle',
			directed: true,
			roots: rootNode,
			padding: 10
		},
	});

	cy.contextMenus({
		menuItems: [{
			id: 'Crash',
			content: 'Crash Node',
			selector: 'node', // applies to nodes only
			onClickFunction: function(event) {
				var target = event.target || event.cyTarget;
				injectFault(target);
			},
			hasTrailingDivider: true
		},
		{
			id: 'Flood',
			content: 'Flood',
			selector: 'node', // applies to nodes only
			onClickFunction: function(event) {
				var target = event.target || event.cyTarget;
				flood(target);
			},
			hasTrailingDivider: true
		},
		{
			id: 'Drop',
			content: 'Drop Messages',
			selector: 'node', // applies to nodes only
			onClickFunction: function(event) {
				var target = event.target || event.cyTarget;
				dropMessage(target);
			},
			hasTrailingDivider: true
		},
		{
			id: 'Alter',
			content: 'Alter Message Content',
			selector: 'node', // applies to nodes only
			onClickFunction: function(event) {
				var target = event.target || event.cyTarget;
				alterMessage(target);
			}
		}

		],
		menuItemClasses: ['custom-menu-item'],
		contextMenuClasses: ['custom-context-menu']
	});

	hideAllEdges();

}

function hideAllEdges() {
	let edges = cy.edges();

	edges.hide();

	// Applying default styles to all edges
	cy.style()
		.selector('edge')
		.style({
			'line-color': '#ddd',
			'width': '2px',
			'curve-style': 'bezier',
			'target-arrow-shape': 'triangle',
			'target-arrow-color': '#ddd'
		})
		.update();
}



function injectFault(node) {
	// Implement your fault-injection logic here
	console.log("Crashin Node:", node.id());

	const alertElement = document.getElementById('mainalert');
	alertElement.innerText = `Crashing Node : ${node.id()}`;
	alertElement.classList.add('alert-warning');

	nodesCrashed.push(node.id());
	stompClient.publish({
		destination: "/app/inject_fault",
		body: node.id()
	});
	cy.getElementById(node.id()).style('background-color', 'red');
	updateNodeLabel(node, "Crashed");
}

function flood(node) {
	console.log("Sending random messge from Node:", node.id());

	const alertElement = document.getElementById('mainalert');
	alertElement.innerText = `Sending random messge from Node : ${node.id()}`;
	alertElement.classList.add('alert-warning');

	nodesByzantine.push(node.id());
	stompClient.publish({
		destination: "/app/flood",
		body: node.id()
	});
	cy.getElementById(node.id()).style('background-color', 'yellow');
	updateNodeLabel(node, "Flood");
}

function dropMessage(node) {
	console.log("Dropping some message from node:", node.id());

	const alertElement = document.getElementById('mainalert');
	alertElement.innerText = `Dropping 50% message from node : ${node.id()}`;
	alertElement.classList.add('alert-warning');

	nodesByzantine.push(node.id());
	stompClient.publish({
		destination: "/app/drop_message",
		body: node.id()
	});
	cy.getElementById(node.id()).style('background-color', 'yellow');
	updateNodeLabel(node, "Drop");
}

function alterMessage(node) {
	console.log("Altering the content of message:", node.id());

	const alertElement = document.getElementById('mainalert');
	alertElement.innerText = `Altering the content of message : ${node.id()}`;
	alertElement.classList.add('alert-warning');

	nodesByzantine.push(node.id());
	stompClient.publish({
		destination: "/app/alter_message",
		body: node.id()
	});
	cy.getElementById(node.id()).style('background-color', 'yellow');
	updateNodeLabel(node, "Alter");
}


function stopSimulation() {
	console.log("Stopping the simulation ");
	stompClient.publish({
		destination: "/app/stop_simulation"
	});
}

function determineStyles(eventType) {
	const styles = {
		node: {},
		edge: {}
	};

	switch (eventType) {
		case 'Delivered':
			styles.edge = {
				'line-color': 'green',
				'target-arrow-color': 'green'
			};
			break;
		case 'Echo':
			styles.edge = {
				'line-color': 'blue',
				'target-arrow-color': 'blue'
			};
			break;
		case 'Sent':
			styles.edge = {
				'line-color': 'red',
				'target-arrow-color': 'red'
			};
			break;
	}

	return styles;
}


function addEventToList(eventText, color) {
	// Get the <ul> element
	//const eventList = document.querySelector('.card-body .list-group');
	const eventList = document.getElementById('eventlist');
	// Create a new <li> element
	const newEvent = document.createElement('li');
	newEvent.className = 'list-group-item';
	newEvent.textContent = eventText;

	// Set the background color for the event
	newEvent.style.color = color;

	// Append the new event to the event list
	eventList.appendChild(newEvent);
}


function addEvent(eventName, color) {
	const eventBox = document.createElement('div');
	eventBox.className = 'event-box';
	eventBox.innerText = eventName;

	// Set the text color and word wrap
	eventBox.style.color = color;
	//eventBox.style.wordWrap = "break-word";
	eventBox.style.padding = "5px"; // You can adjust this value for desired padding
	eventBox.style.margin = "2px";  // Add some margin for separation between boxes
	eventBox.style.border = "1px solid #ddd"; // Optional border for better distinction

	// Append to the container
	eventsChain.appendChild(eventBox);
	eventsChain.scrollLeft = eventsChain.scrollWidth;
}

function updateNodeLabel(node, text) {
	// Get the current node's label
	const currentLabel = node.data('label') || ""; // Default to empty string if undefined

	// Check if the current label is empty or just whitespace
	if (currentLabel.trim() === "") {
		node.data('label', text);
	} else {
		// Append the new text to the current label
		const newLabel = currentLabel + '\n' + text;
		node.data('label', newLabel);
	}
}

function resetStyles() {


}
