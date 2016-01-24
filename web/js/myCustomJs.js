var updateFreq = 5; //In seconds

//def updateAll( function() {

//});
$(document).ready(
function() {
  updateNumWorkers();
});


function updateNumWorkers() {
  $.post('localhost:8888/api', '{ "rpc": "num_workers" }')
    .done( function(newNumber) {
      $('#workers-number').html(1);})
    .fail( function() {
      $('#workers-number').html(-1);
  });
}

// d3.select(".chart")
//   .selectAll("div")
//     .data(data)
//   .enter().append("div")
//     .style("height", function(d) { return d * 10 + "px"; })
//     .text(function(d) { return d; });

// function plott() {
//   var data = [{
//     "y": [1,3,5,7,9],
//     "x": [1,2,3,4,5],
//     "line": { "width": 5 }
//   }];
//   var layout = {
//     "type": "scatter",
//     "visible": "true",
//     "width":  $('#myDiv').clientWidth * 0.2,
//     "height": $('#myDiv').clientHeight * 0.2
//
//   };
//   Plotly.newPlot('myDiv', data, layout, {displaylogo: false});
// }
