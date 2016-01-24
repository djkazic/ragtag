var updateFreq = 5; //In seconds

//def updateAll( function() {

//});
$(document).ready(
    function() {
        updateNumWorkers();
        var dataarr = [1, 2, 3, 5];
        d3.select("#workers-benchmark")
            .selectAll("div")
            .data(dataarr)
            .enter().append("div")
            .style("height", function(d) {
                return d * 10 + "px";
            })
            .style("class", function(d) {
                return d + "th-child";
            });
    });

window.setInterval(function() {
  updateNumWorkers();
}, 5000);

function updateWorkerChart(n) {
    d3.select("#workers-benchmark").data([n]).append("div")
        .style("height", function(d) {
            return d * 10 + "px";
        });

    $("#workers-benchmark").children().first().remove();
    console.log("button pressed");
    console.log($('#workers-benchmark'));
}

function getLargest(data) {
    var max = -9999999;
    for (var i = 0; i < data.length; i++) {
        if (data[i] > max) {
            max = data[i];
        }
    }
}

function updateNumWorkers() {
  $.post('localhost:8888/api', '{ "rpc": "num_workers" }')
    .done( function(newNumber) {
      $('#workers-number').html(newNumber);
  		updateWorkerChart(newNumber); })
    .fail( function() {
      $('#workers-number').html(3);
      updateWorkerChart(newNumber);
  });
}



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
