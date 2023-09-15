var ctx = document.getElementById('latencyChart').getContext('2d');
const simulationStartTime = new Date();

var chart = new Chart(ctx, {
	type: 'line',
	data: {
		datasets: []
	},
	options: {
		scales: {
			x: {
				type: 'time',
				time: {
					unit: 'second', // This can be 'minute', 'hour', etc. based on granularity
				},
				title: {
					display: true,
					text: 'Time'
				}
			},
			y: {
				beginAtZero: true,
				title: {
					display: true,
					text: 'Latency (ms)'  // Assuming latency is in milliseconds
				}
			}
		}
	}
});

// Use this function when you get to know the number of nodes at runtime
function initializeNodes(numberOfNodes) {
	for (let i = 0; i < numberOfNodes; i++) {
		const nodeName = 'node-' + i;
		addNodeDataToChart(nodeName, [], getRandomColor());  // Initialize with empty data
	}
}

function getRandomColor() {
	var letters = '0123456789ABCDEF';
	var color = '#';
	for (var i = 0; i < 6; i++) {
		color += letters[Math.floor(Math.random() * 16)];
	}
	return color;
}

function addNodeDataToChart(nodeName, nodeData, nodeColor) {
	const newDataset = {
		label: nodeName,
		data: nodeData,
		borderColor: nodeColor,
		fill: false,
	};

	chart.data.datasets.push(newDataset);
	chart.update();
}

function updateNodeDataset(nodeName, latencyValue) {
	const dataset = chart.data.datasets.find(ds => ds.label === nodeName);
	if (dataset) {
		const currentTime = new Date();
		const elapsedTime = currentTime - simulationStartTime; // This gives time in milliseconds

		dataset.data.push({
			x: elapsedTime,  // elapsed time in milliseconds since simulation start
			y: latencyValue
		});
		chart.update();
	}
}


// Char for throughput
var throughputCtx = document.getElementById('throughputChart').getContext('2d');

var throughputChart = new Chart(throughputCtx, {
	type: 'line',
	data: {
		datasets: [{
			label: 'Throughput',
			borderColor: 'rgb(255, 99, 132)',
			data: [],
			fill: false,
		}]
	},
	options: {
		scales: {
			x: {
				type: 'time',
				time: {
					unit: 'second'
				},
				title: {
					display: true,
					text: 'Time'
				}
			},
			y: {
				beginAtZero: true,
				title: {
					display: true,
					text: 'Messages per Second'
				}
			}
		}
	}
});

let messageCount = 0;
let throughputInterval;


function onMessageReceived() {
	messageCount++;
}

// Update the throughput every second
/*setInterval(function() {
	const currentTime = new Date().getTime();

	// Update the chart
	throughputChart.data.datasets[0].data.push({
		x: currentTime,
		y: messageCount
	});
	throughputChart.update();

	// Reset message count
	messageCount = 0;
}, 1000);*/


function startThroughputMonitoring() {
    // If already started, return
    if (throughputInterval) {
        return;
    }

    throughputInterval = setInterval(function() {
        const currentTime = new Date().getTime();

        // Update the chart
        throughputChart.data.datasets[0].data.push({
            x: currentTime,
            y: messageCount
        });
        throughputChart.update();

        // Reset message count
        messageCount = 0;
    }, 1000);
}

function stopThroughputMonitoring() {
    if (throughputInterval) {
        clearInterval(throughputInterval);
        throughputInterval = null;
    }
}


//Send to Deliver
var ctxDeliver = document.getElementById('sendToDeliverChart').getContext('2d');

var sendToDeliverChart = new Chart(ctxDeliver, {
    type: 'line',
    data: {
        datasets: [{
            label: 'Send-to-Deliver/Not Deliver Duration',
            borderColor: 'rgb(255, 99, 132)', // Color of the line
            data: [],
            fill: false,
        }]
    },
    options: {
        scales: {
            x: {
                type: 'time',
                time: {
                    unit: 'second',
                },
                title: {
                    display: true,
                    text: 'Time'
                }
            },
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Duration (ms)'
                }
            }
        }
    }
});

function updateSendToDeliverDuration(duration) {
    const currentTime = new Date().getTime();

    sendToDeliverChart.data.datasets[0].data.push({
        x: currentTime,
        y: duration
    });
    sendToDeliverChart.update();
}


//Deliver Status chart
var ctxStatus = document.getElementById('deliveryStatusChart').getContext('2d');

var deliveryStatusChart = new Chart(ctxStatus, {
    type: 'pie',
    data: {
        labels: ['Delivered', 'Not Delivered'],
        datasets: [{
            data: [deliveredCount, notDeliveredCount],  // Initial data, can be updated dynamically
            backgroundColor: ['rgb(75, 192, 192)', 'rgb(255, 99, 132)'] // Color for "delivered" and "not delivered" respectively
        }]
    },
    options: {
        responsive: true,
        legend: {
            position: 'top',
        },
        title: {
            display: true,
            text: 'Delivery Status'
        },
        animation: {
            animateScale: true,
            animateRotate: true
        }
    }
});

function updateDeliveredCount(count) {
    deliveryStatusChart.data.datasets[0].data[0] = count;  // Update data for "Delivered"
    deliveryStatusChart.update();
}

function updateNotDeliveredCount(count) {
    deliveryStatusChart.data.datasets[0].data[1] = count;  // Update data for "Not Delivered"
    deliveryStatusChart.update();
}






