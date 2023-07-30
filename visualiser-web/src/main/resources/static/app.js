// define the cy variable without initializing
let cy = null;
const stompClient = new StompJs.Client({
	brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
	setConnected(true);
	console.log('Connected: ' + frame);
	stompClient.subscribe('/topic/simulate_data', (greeting) => {
		console.log(greeting.body);
		const receivedData = JSON.parse(greeting.body); // parse the incoming data
		const graphData = receivedData.elements; // extract the 'elements' property from the received data
		//drawGraph(); // call a function to draw the graph with the incoming data
		drawGraph(graphData);
		//showGreeting(JSON.parse(greeting.body).content);
	});
	
	stompClient.subscribe('/topic/highlight_nodes', (message) => {
		console.log('Received highlight_nodes message', message);
		const nodesToHighlight = JSON.parse(message.body);
		console.log('Nodes to highlight', nodesToHighlight);
		nodesToHighlight.forEach(nodeId => {
			console.log('Highlighting node', nodeId);
			cy.getElementById(nodeId).addClass('highlighted');
		});
	});

};




stompClient.onWebSocketError = (error) => {
	console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
	console.error('Broker reported error: ' + frame.headers['message']);
	console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	}
	else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}

function connect() {
	stompClient.activate();
}

function disconnect() {
	stompClient.deactivate();
	setConnected(false);
	console.log("Disconnected");
}

function sendName() {
	stompClient.publish({
		destination: "/app/simulate",
		body: JSON.stringify({ 'name': $("#name").val() })
	});

	stompClient.publish({
		destination: "/app/highlight"
	});
}

function showGreeting(message) {
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function() {
	$("form").on('submit', (e) => e.preventDefault());
	$("#connect").click(() => connect());
	$("#disconnect").click(() => disconnect());
	$("#send").click(() => sendName());
});

// define a function to draw the graph
function drawGraph(elements) {
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
		/* elements: {
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
   },*/
		elements: elements, // use the incoming data to set the elements

		layout: {
			name: 'breadthfirst',
			directed: true,
			roots: '#a',
			padding: 10
		}
	});

	var bfs = cy.elements().bfs('#a', function() { }, true);

	var i = 0;
	var highlightNextEle = function() {
		if (i < bfs.path.length) {
			bfs.path[i].addClass('highlighted');

			i++;
			setTimeout(highlightNextEle, 1000);
		}
	};

	// kick off first highlight
	//highlightNextEle();
}
