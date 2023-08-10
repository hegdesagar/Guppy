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
			{ data: { id: 'ab', weight: 1, source: 'a', target: 'b' } },
			{ data: { id: 'ac', weight: 1, source: 'a', target: 'c' } },
			{ data: { id: 'ba', weight: 1, source: 'b', target: 'a' } },
			{ data: { id: 'bc', weight: 1, source: 'b', target: 'c' } },
			{ data: { id: 'ca', weight: 1, source: 'c', target: 'a' } },
			{ data: { id: 'cb', weight: 1, source: 'c', target: 'b' } }
		]
	};
	document.getElementById('mainalert').innerText = "Welcome to Node Broadcasting simulator";
	drawGraph(elements,'#a');
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
		console.log(graphData);
		drawGraph(graphData,'#node-0');
	});

	highlightNodesSubscription = stompClient.subscribe('/topic/highlight_nodes', (message) => {
		console.log('Received highlight_nodes message', message);
		const nodesToHighlight = JSON.parse(message.body);
		console.log('Nodes to highlight', nodesToHighlight);
		nodesToHighlight.forEach(nodeId => {
			console.log('Highlighting node', nodeId);
			cy.getElementById(nodeId).addClass('highlighted');
		});
	});

	alertSubscription = stompClient.subscribe('/topic/alert_updates', (alertMessage) => {
		console.log('Received alert update', alertMessage);
		document.getElementById('mainalert').innerText = alertMessage.body;
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


$(document).ready(function() {
	$('#implementation a').on('click', function(event) {
		event.preventDefault();
		$('#dropdownMenuButton').html($(this).text() + ' <span class="caret"></span>');
		selectedImplementation = $(this).attr('id');
	});

});



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
		$(this).text("Stop");
	} else {
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

$(function() {
	$("form").on('submit', (e) => e.preventDefault());
	$("#SimulationButton").click(() => connect());
	//$("#disconnect").click(() => disconnect());
	//$("#send").click(() => sendName());
});

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
				'content': 'data(id)'
			})
			.selector('edge')
			.style({
				'curve-style': 'straight',
				'target-arrow-shape': 'none',
				'width': 4,
				'line-color': '#ddd',
				'target-arrow-color': '#ddd'
			})
			.selector('.highlighted')
			.style({
				'background-color': '#61bffc',
				'line-color': '#61bffc',
				'target-arrow-color': '#61bffc',
				'transition-property': 'background-color, line-color, target-arrow-color',
				'transition-duration': '0.5s'
			}),

		elements: elements, // use the incoming data to set the elements

		layout: {
			name: 'breadthfirst',
			directed: true,
			roots: rootNode,
			padding: 10
		}
	});

	var bfs = cy.elements().bfs(rootNode, function() { }, true);

	var i = 0;
	var highlightNextEle = function() {
		if (i < bfs.path.length) {
			bfs.path[i].addClass('highlighted');

			i++;
			setTimeout(highlightNextEle, 1000);
		}
	};
}
