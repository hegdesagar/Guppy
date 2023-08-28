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
	//Default data for cyptoscape
	const elements = {
		nodes: [
			{ data: { id: 'a' } },
			{ data: { id: 'b' } },
			{ data: { id: 'c' } }
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

}

// define the cy variable without initializing
let cy = null;

let started = false;

//Websocket defination START
const stompClient = new StompJs.Client({
	brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

let simulateDataSubscription;
let highlightNodesSubscription;
// Subscription to alert_updates topic
let alertSubscription;

stompClient.onConnect = (frame) => {
	console.log('Connected: ' + frame);
	simulateDataSubscription = stompClient.subscribe('/topic/simulate_data', (greeting) => {
		//console.log(greeting.body);
		const receivedData = JSON.parse(greeting.body);
		const graphData = receivedData.elements;
		//const stringData = JSON.stringify(graphData);
		console.log('graphdata', graphData);
		drawGraph(graphData, '#node-0');
		//highlightSpecificElements(graphData, graphData,graphData,graphData);
	});


	highlightNodesSubscription = stompClient.subscribe('/topic/highlight_nodes', (message) => {
		// First, reset the styles for all nodes and edges to their base styles
		hideAllEdges();
		let mqRecordList = JSON.parse(message.body); // This will now be an array
		let nodesToHighlight = []; // Collect nodes to be highlighted
		let edgesToHighlight = []; // Collect edges to be highlighted
		let leadersToHighlight = []; // Collect leader nodes

		console.log('New list of record------------------------------------------------');
		// Process each MQRecord in the array
		mqRecordList.forEach(mqRecord => {
			const senderNode = mqRecord.senderId;
			const receiverNode = mqRecord.receiverId;
			const edgeHighlight = mqRecord.edgeHighlight;
			const eventType = mqRecord.eventType;
			const leadernode = mqRecord.leaderNode;

			console.log("EdgeHightlight :", edgeHighlight); // prints the eventType
			// prints the timeStamp console.log(senderNode.id);if(senderNode) 
			if (senderNode) {
				//console.log("Sender Node : ", senderNode.id);   // prints the id of the senderNode
			} else {
				//console.log("senderNode is undefined!");
			}

			if (receiverNode) {
				//console.log("Receiver Node : ", receiverNode.id); // prints the id of the receiverNode
			} else {
				//console.log("receiverNode is undefined!");
			}
			//console.log("event type : ", eventType);
			//console.log("leader Node : ", leadernode);


			// Get the styles determined by the event type
			const styles = determineStyles(eventType);

			// Apply styles based on whether the event refers to a sender or receiver
			if (senderNode) {
				nodesToHighlight.push({ id: senderNode.id, event: eventType });
			}
			if (receiverNode) {
				nodesToHighlight.push({ id: receiverNode.id, event: eventType });
			}
			if (edgeHighlight) {
				edgesToHighlight.push({ id: edgeHighlight, event: eventType });
			}
			if (leadernode) {
				leadersToHighlight.push(leadernode);
			}

		});

		// Apply styles to nodes
		nodesToHighlight.forEach(node => {
			//console.log("Node : ", node);
			if (node.event == 'SEND') {
				cy.getElementById(node.id).style('background-color', 'red');
			}
			if (node.event == 'ECHO') {
				cy.getElementById(node.id).style('background-color', 'blue');
			}

		});

		// Apply styles to edges
		edgesToHighlight.forEach(edge => {
			//cy.getElementById(edge).style('line-color', 'pink');
			cy.getElementById(edge.id).style({
				'line-color': 'pink',
				'target-arrow-color': 'pink',
				'label': edge.event
			});
			const edgeConstant = cy.getElementById(edge.id);
			edgeConstant.style('line-color', '#61bffc');
			edgeConstant.style('target-arrow-color', '#61bffc');
			edgeConstant.show();
		});

		// Apply styles to leaders
		leadersToHighlight.forEach(leader => {
			cy.getElementById(leader).addClass('leader');
		});


	});

	alertSubscription = stompClient.subscribe('/topic/alert_updates', (alertMessage) => {
		console.log('Received alert update', alertMessage);
		document.getElementById('mainalert').innerText = alertMessage.body;
	});

	cy.on('style', 'node', function(event) {
		const node = event.target;
		if (node.data('leader')) {
			node.style('content', node.data('id') + '\nLeader');
		}
	});


};

function disconnect() {
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

	stompClient.deactivate();
	//document.getElementById('SimulationButton').innerHTML = "Start";
	started = false;
	console.log("Disconnected");
}


stompClient.onWebSocketError = (error) => {
	console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
	console.error('Broker reported error: ' + frame.headers['message']);
	console.error('Additional details: ' + frame.body);
};

let selectedImplementation = ""; // global variable to hold the selected implementation

// Event handler for dropdown items
/*$(".dropdown-item").click(function(){
	selectedImplementation = $(this).text();
});*/

document.getElementById('customRange1').addEventListener('input', function() {
	document.getElementById('rangeValue').textContent = this.value;
});

function publish() {
	const nodesValue = $("#nodes").val();
	const timelineValue = $('#customRange1').val();
	//const implementation = document.querySelector('#implementation .dropdown-item').textContent;

	let messageJson = JSON.stringify({
		'nodes': nodesValue,
		'implementation': selectedImplementation,
		'timeline': timelineValue
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
/*function sendName() {
	
}*/

/*$(function() {
	$("form").on('submit', (e) => e.preventDefault());
	$("#SimulationButton").click(() => connect());
	//$("#disconnect").click(() => disconnect());
	//$("#send").click(() => sendName());
});*/


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
				'content': 'data(id)',
				'text-wrap': 'wrap',
				'text-max-width': 80 // adjust as per your needs
				//'text-valign': 'top'

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
				'border-width': '4px',
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
	stompClient.publish({
		destination: "/app/inject_fault",
		body: node.id()
	});
}

function flood(node){
	console.log("Sending random messge from Node:", node.id());
	stompClient.publish({
		destination: "/app/flood",
		body: node.id()
	});
}

function dropMessage(node){
	console.log("Dropping some message from node:", node.id());
	stompClient.publish({
		destination: "/app/drop_message",
		body: node.id()
	});
}

function alterMessage(node){
	console.log("Altering the content of message:", node.id());
	stompClient.publish({
		destination: "/app/alter_message",
		body: node.id()
	});
}


function stopSimulation(){
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

function resetStyles() {

	//cy.edges().hide();


	/*cy.nodes().style({
		'background-color': '',  // The base background color of your nodes
	});

	cy.edges().style({
		'line-color': '#ddd',   // The base line color of your edges
		'target-arrow-color': '#ddd',   // The base arrow color of your edges
		'label': '',
	});*/
}
