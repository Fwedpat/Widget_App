function initGraph(dataset, pHeight, pWidth) {

    var height = parseInt(pHeight);
    var width = parseInt(pWidth);
    var textLabelSuffix = "%";

     showPieChart();
  }
  
function showPieChart()
  {


    var width = 300
    var height = 200

    // append the svg object to the body of the page
    var svg = d3.select("#streamgraph")
      .append("svg")
        .attr("width", 300)
        .attr("height", 200)

    // create dummy data -> just one element per circle
var data = [{ "name": "A", "group": 1 }, { "name": "B", "group": 1 }, { "name": "C", "group": 1 }, { "name": "D", "group": 1 }, { "name": "E", "group": 1 }, { "name": "F", "group": 1 },
            { "name": "G", "group": 2 }, { "name": "H", "group": 2 }, { "name": "I", "group": 2 }, { "name": "J", "group": 2 }, { "name": "K", "group": 2 }, { "name": "L", "group": 2 },
            { "name": "M", "group": 3 }, { "name": "N", "group": 3 }, { "name": "O", "group": 3 },
            { "name": "p", "group": 1 }, { "name": "Q", "group": 1 }, { "name": "R", "group": 1 }, { "name": "S", "group": 1 }, { "name": "T", "group": 1 }, { "name": "U", "group": 1 },
            ]


    var color = d3.scaleOrdinal()
      .domain([1, 2, 3])
      .range([ "#F8766D", "#00BA38", "#619CFF"])

    var x = d3.scaleOrdinal()
      .domain([1, 2, 3])
      .range([50, 200, 340])

    // Initialize the circle: all located at the center of the svg area
    var node = svg.append("g")
      .selectAll("circle")
      .data(data)
      .enter()
      .append("circle")
        .attr("r", 30)
        .attr("cx", width / 2)
        .attr("cy", height / 2)
        .style("fill", function(d){ return color(d.group)})
        .style("fill-opacity", 0.3)
        .attr("stroke", "black")
        .style("stroke-width", 4)
        .call(d3.drag() // call specific function when circle is dragged
              .on("start", dragstarted)
              .on("drag", dragged)
              .on("end", dragended));

        var simulation = d3.forceSimulation()
            .force("center", d3.forceCenter().x(width / 2).y(height / 2)) // Attraction to the center of the svg area
            .force("charge", d3.forceManyBody().strength(1)) // Nodes are attracted one each other of value is > 0
            .force("collide", d3.forceCollide().strength(.1).radius(30).iterations(1)) // Force that avoids circle overlapping

        // Apply these forces to the nodes and update their positions.
        // Once the force algorithm is happy with positions ('alpha' value is low enough), simulations will stop.
        simulation
            .nodes(data)
            .on("tick", function(d){
              node
                  .attr("cx", function(d){ return d.x; })
                  .attr("cy", function(d){ return d.y; })
            });

        // What happens when a circle is dragged?
        function dragstarted(d) {
          if (!d3.event.active) simulation.alphaTarget(.03).restart();
          d.fx = d.x;
          d.fy = d.y;
        }
        function dragged(d) {
          d.fx = d3.event.x;
          d.fy = d3.event.y;
        }
        function dragended(d) {
          if (!d3.event.active) simulation.alphaTarget(.03);
          d.fx = null;
          d.fy = null;
        }
  }