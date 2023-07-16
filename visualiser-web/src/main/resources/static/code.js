var cy = cytoscape({
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

  elements: {
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
    },

  layout: {
    name: 'breadthfirst',
    directed: true,
    roots: '#a',
    padding: 10
  }
});

var bfs = cy.elements().bfs('#a', function(){}, true);

var i = 0;
var highlightNextEle = function(){
  if( i < bfs.path.length ){
    bfs.path[i].addClass('highlighted');

    i++;
    setTimeout(highlightNextEle, 1000);
  }
};

// kick off first highlight
//highlightNextEle();