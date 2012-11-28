// Load the Visualization API
google.load('visualization', '1.0', {'packages':['corechart']});

// Get vote count when the Google Visualization API is loaded.
google.setOnLoadCallback(function() {
	$.get('servlets/VoteServlet', {dataType: "application/json"}, drawChart);
});

// Populates a chart with the given data 
function drawChart(data) {
	var rows = [];
	$.each(data, function(index, value) {
		rows[index] =[value.name, value.votes];
	});

    // Create the data table.
    var dataTable = new google.visualization.DataTable();
    dataTable.addColumn('string', 'Contestant');
    dataTable.addColumn('number', 'Votes');
    dataTable.addRows(rows);

    // Set chart options
    var options = {
           width: 520,
           height: 300,
           backgroundColor: '#eee',
           colors: ['#FA6121'],
           legend: {position: 'none'},
           chartArea: {left: 25, top: 20, width: '100%', height: '83%'},
           fontName: 'Sanchez',
           vAxis: {minValue: 0, format:'#'}
	};

    // Instantiate and draw the chart with options.
    var chart = new google.visualization.ColumnChart(document.getElementById('standingsChart'));
    chart.draw(dataTable, options);
}