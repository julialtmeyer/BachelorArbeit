let bitmapFaceImage = "";
let jobs = "";
let robots = {};
let map = {};
let runningCMD = "";

const MAP_HEIGHT = 589;
const MAP_WIDTH = 1200;

const REFRESH_RATE = 250;

const CURRENT_URL = "http://localhost:8080/"


let ctx;

$(document).ready(function() {
	getDocs();
	getRobots();

	canvas = document.getElementById('mapoverlay');
	canvas.width = $('.map').width();
	canvas.height = $('.map').height();

	ctx = canvas.getContext("2d");
	
	window.setInterval(function () {
		let robotID = $("#robotID").val();

		if(robotID != null){
			//getJobs(robotID);
			getLiveView(robotID);
			for (i in robots){
				robotPos(robots[i]);
				//getJobs(robots.robots[i].robotID);
			}
		}
		getRobots();
  	}, REFRESH_RATE);
});



/**
 * Adds an timer overlay on an given div
 * Shows how long each command takes to execute
 * @param seconds
 * @param div
 */

function timer(seconds, div){

	$("#" + div).append('<div class="timer"></div>');
	let timer = $("#" + div + " .timer")

	timer.css({transition : "width " + seconds + "s"})
	timer.width(0);

}


/**
 * All the REST Getter
 */

function getDocs(){
	$.ajax({
		url: CURRENT_URL + "docs"
	}).then(function(data) {
		displayData(data);
	});
}

function getLiveView(robotID){
	var response = '';

	$.ajax({
		url: CURRENT_URL + "robots/" + robotID + "/image"

    }).then(function(data) {
    	response = JSON.parse(data);

    	if(response["error"] === "001"){
    		console.log("cant get image for", robotID);
    	}else if(response["image"] !== "null" && response["image"].trim() !== ""){

    		// webcam
    		//image = new Image();
    		//image.src = response["image-webcam"];

    		//$('.webcam').empty();

			//$('.webcam').append(image);
			//$('.webcam img:first-of-type').remove();


			// vision
			imageVision = new Image();
    		imageVision.src = response["image"];
    		//$('.vision').empty();
			//$('.vision').append(imageVision);
			//$('.vision img').replaceWith(imageVision);
			$('.vision').append(imageVision);
			$('.vision img:first-of-type').remove();
    	}
    });
}

function getRobots(){
	$.ajax({
		url: CURRENT_URL + "robots/"
	}).then(function(data) {
		if(JSON.stringify(robots) !== JSON.stringify(data)){
    		displayRobots(data);
    	}
    	robots = JSON.parse(data)
	});
}

function getMap(){
	$.ajax({
		url: CURRENT_URL + "map"
	}).then(function(data) {
		if(map !== JSON.stringify(data)){
			displayMap(data);
		}
		map = JSON.stringify(data)
	});
}

function displayMap(data){
	let jsonMap = JSON.parse(data);
	$(".map .node").remove();

	for (node in jsonMap){
		node = jsonMap[node]

		let point = relativePosition(node.x, node.y)


		$(".map").append('<span class="node" style="top:'+ Math.round(point[1]) + 'px; left:' + Math.round(point[0]) + 'px;">'+ node.id +'</span>')
	}
}

function getJobs(robotID){
	$.ajax({
		type: "POST",
		url: CURRENT_URL + "jobs",
		data: '{"robotID":"'+robotID+'"}',
		contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).then(function(data) {
    	if(data["error"] == "001"){
    		console.log("cant get jobs for", robotID);
    	}else{

    		if(data.length > 0){
    			//data.jobs.reverse();

    			//data.map( (job) => job.robot = 0);

    			if(jobs !== JSON.stringify(data)){
    				displayJobs(data, robotID);
    			}

    			jobs = JSON.stringify(data)
    		}
    	}
    });
}

/**
 * All the REST Setter
 */

function sendAdjustment(robotID,headpos,forkpos){

	$.ajax({
		type: "POST",
		url: CURRENT_URL + "adjustment",
		data: '{"robotID":"'+robotID+'", "bitmapFace": "'+bitmapFaceImage+'", "headPosition": '+ headpos +', "forkPosition": '+forkpos+'}',
		contentType: "application/json; charset=utf-8",
		dataType: "json"

    }).then(function(data) {
		if(data["error"]){
			infoModal(data["msg"]);
		}
    });
}


function sendDrive(robotID,posX,posY){
	$.ajax({
		type: "POST",
		url: CURRENT_URL + "control/AtoB/" + robotID,
		data: '{"x":' + posX + ',"y":' + posY + '}',
		contentType: "application/json; charset=utf-8",
		dataType: "json"

    }).then(function(data) {


		if(data.code === "002") {
			pos = relativePosition(posX, posY);

			x = Math.round(pos[0]*100)/100
			y = Math.round(pos[1]*100)/100

			//$(".map").append('<span class="errorflag" style="top:'+y+'px;left:'+x+'px;"></span>');

			ctx.beginPath();

			ctx.arc(x, y, 2, 0, 2 * Math.PI);
			ctx.fillStyle = "red";
			ctx.fill();


			infoModal(data["msg"]);

		}else if(data.code === "001"){
			infoModal(data["msg"]);
			$(".map .flag:last-child").remove()
		}
		else if(data.code === "003"){
			infoModal(data["msg"]);
		}
    })
}

function sendInstructionDrive(robotID, distance){
	$.ajax({
		type: "POST",
		url: CURRENT_URL + "control/drive/" + robotID,
		data: JSON.stringify(distance),
		contentType: "application/json; charset=utf-8",
		dataType: "json"
    }).then(function(data) {
		if(data["error"]){
			infoModal(data["msg"]);
		}
	});
}


/**
 * All the Displayer
 */

function displayRobots(data){
	data = JSON.parse(data)

	for (i in data) {

		//getJobs(data["robots"][i]["robotID"]);

		// battery
		let battery = parseFloat(data[i]["robot_info"]["battery"]).toFixed(2)
		//batteryPercent = Math.round((battery/5) * 100)


		if(!$('#robotID option[value="' + data[i]["id"] + '"]').length){
			let option = $("<option>"+data[i]["roboterName"] + "</option>").attr("value", data[i]["id"])
			$('#robotID').append(option)
		}


		pos = relativePosition(data[i]["robot_info"]["location_x"],data[i]["robot_info"]["location_y"])
		x = pos[0]
		y = pos[1]

		orientation = data[i]["robot_info"]["direction"];

		if($('.map .robot#'+data[i]["id"]).length === 0){

			$('.map').append(
				'<span ' +
				'class="robot" ' +
				'id="'+data[i]["id"]+'" ' +
				'style="top:'+y+'px;left:'+x+'px;transform: rotate('+orientation+'deg)"' +
				'data-orientation="' + orientation + '">' +
				'<em class="name">' + data[i]["roboterName"] + '</em>' +
				'</span>'
			);
		}

		if($('#robotID').val() == data[i]["id"]) {
			$('#roboterName').html(data[i]["roboterName"])
			$('#id').html(data[i]["id"])
			$('#battery').html(battery + " v")
			$('#position').html("("+data[i]["robot_info"]["location_x"] + "," + data[i]["robot_info"]["location_y"] + ")")
		}


	}
}

// refresh the robot position on the map
function robotPos(robot){

	let x = robot.robot_info.location_x
	let y = robot.robot_info.location_y
	let orientation = robot.robot_info.direction
	// calculate mm to px
	let pos = relativePosition(x,y)

	x = pos[0]
	y = pos[1]

	let robotMap = $('.map .robot#' + robot.id)


	if(robotMap.length){

		robotMap.data("orientation", orientation)

		robotMap.css({
			"top": y+"px",
			"left": x + "px",
			"transform": "rotate("+orientation+"deg)",
			"transition-duration": seconds + "s"
		})
	}
}

function displayData(data){
	$('.data').prepend(data);
}

function displayJobs(data, robotID){

		for(jobC in data){

				job = data[jobC]

				if($('.jobs .job-' + job.jobID).length){

					jobState = $('.jobs .job-' + job.jobID + ' .jobstate')

					//console.log(job.jobID, job.state.toLowerCase(), jobState.hasClass(job.state.toLowerCase()), jobState)

					if (!jobState.hasClass(job.state.toLowerCase())){

						jobState.removeClass()
						jobState.addClass("jobstate " + job.state.toLowerCase())

					}
				}else{
					if(job.robot.robotID === $('#robotID').val()){
						let date  = new Date(job.created);
						let h = date.getHours();
						let m = "0" + date.getMinutes();
						let s = "0" + date.getSeconds();
						let created = h + ':' + m.substr(-2) + ':' + s.substr(-2);

						$('.jobs').prepend(
							'<li class="list-group-item job-'+job.jobID+'">'+
							'<span class="name">'+job.jobName+'</span>'+
							'<span class="desc">'+job.description+'</span>'+
							'<span class="jobstate '+job.state.toLowerCase()+'"></span>'+
							'<span class="date">'+created+'</span>'+
							'<span class="id">'+job.jobID+'</span>'+
							'<ul class="sublist"></ul>' +
							'</li>'
						)
					}
				}


				for(commandC in job.commandQueue){
					
					command = job.commandQueue[commandC]

					if ($('.jobs .job-' + job.jobID + ' .sublist #' + command.commandID).length){

						commandState = $('.jobs .job-' + job.jobID + ' .sublist #' + command.commandID + ' .state')

						if(!commandState.hasClass(command.state.toLowerCase())){

							commandState.removeClass()
							commandState.addClass("state " + command.state.toLowerCase())

							if(command.gyrolist){
								plotData(command.gyrolist, "plot-" + command.commandID);
							}
						}

					}else{
						if(job.robot.robotID === $('#robotID').val()) {
							seconds = 1;
							if (command.seconds !== undefined) {
								seconds = command.seconds;
							}

							$('.jobs .job-' + job.jobID + ' .sublist').append(
								'<li class="item" id="' + command.commandID + '">' +
								'<div class="plot" id="plot-' + command.commandID + '"></div>' +
								'<span class="state ' + command.state.toLowerCase() + '"></span>' +
								'<span class="desc">' + command.description + '</span>' +
								'<span class="time">' + seconds + 's</span>' +
								'<span class="id">' + command.commandID + '</span>' +
								'</li>'
							)

							if (command.gyrolist) {
								plotData(command.gyrolist, "plot-" + command.commandID);
							}
						}
					}


					if (command.state.toLowerCase() === "running"){
					//if (command.state.toLowerCase() === "running" && command.commandID !== runningCMD){
						if(!$('#' + command.commandID + '').find('.timer').length){
							timer(command.seconds, command.commandID)
						}

						robotPos(job.robot.robotID, command.endPosX, command.endPosY, command.endOrientation, command.seconds)
						runningCMD = command.commandID;
					}
				}
			}

	}	


// Click listeners

$('.map').click(function(e) {

	var posX = e.pageX - $(this).offset().left
	var posY = e.pageY - $(this).offset().top

	console.log(posX,posY)

	pos = absolutePosition(posX,posY)
	x = Math.round(pos[0]*100)/100
	y = Math.round(pos[1]*100)/100

	sendDrive($("#robotID").val(),x,y)

	$(this).append('<span class="flag" style="top:'+posY+'px;left:'+posX+'px;"></span>');
});

$('#sendAdjustment').click(function(e) {

	let head = $('#headpos')
	let fork = $('#forkpos')

	if(!head.val()){head.val(0)}
	if(!fork.val()){fork.val(0)}

	sendAdjustment($("#robotID").val(),head.val(),fork.val())
})

$('#breakInstruction').click(function(e) {

	sendInstruction($("#robotID").val(),"PAUSE")
});
$('#playInstruction').click(function(e) {
	sendInstructionDrive($("#robotID").val(),$('#drive'))
});
$('#stopInstruction').click(function(e) {
	sendInstruction($("#robotID").val(),"STOP")
});

$('#resetInstruction').click(function(e) {
	var elementDom = document.getElementById('map');
	var spanArr = elementDom.getElementsByTagName('span');
	while (spanArr.length !== 0){
		elementDom.removeChild(elementDom.getElementsByTagName('span')[0]);
	}
});


$("#robotID").change(function() {
	console.log("robot changed")
	$('.jobs').empty();
});


$("#bitmapFace").change(function() {
	if (this.files && this.files[0]) {
		let FR = new FileReader();
		FR.addEventListener("load", function(e) {
			bitmapFaceImage = e.target.result
		});
		FR.readAsDataURL( this.files[0] );


		$('.custom-file-label').html(this.files[0].name)
		console.log(this.files[0].name)
	}
});

/*
$('.jobs .item').click(function(e) {
	console.log("click")
	console.log($(this).children(".plot"))
});
*/

/**
 * Helper functions
 */

function infoModal(msg){

	$('.modal').text(msg).removeClass('hide')

	setTimeout(function() {
		$('.modal').text("").addClass('hide')
	},5000);

}

$('.modal').click(function (e){
	$('.modal').addClass('hide')
})

// Returns the position from the distorted map
function relativePosition(x,y){

	xFactor = (x/MAP_WIDTH)
	yFactor = (y/MAP_HEIGHT)

	return [$('.map').width()*xFactor , $('.map').height()*yFactor]
}

// Returns the position for the distorted map
function absolutePosition(x,y){

	xFactor = (x/$('.map').width())
	yFactor = (y/$('.map').height())

	return [MAP_WIDTH*xFactor , MAP_HEIGHT*yFactor]
} 

// plot
function plotData(data, container){

	listx = [] // hoch runter
	listy = [] // vorne hintern
	listz = [] // links rechts

	// TODO replace with lambda

	for (var i = 0; i < data.length; i++) {
		listx[i] = parseFloat(data[i].x) * 10000
		listy[i] = parseFloat(data[i].y) * 10000
		listz[i] = parseFloat(data[i].z) * 10000
	}

	Highcharts.chart(container, {
		series: [{
			name: 'x',
			data: listx,
			color: '#FF0000'
		},{
			name: 'y',
			data: listy,
			color: '#00FF00'
		},{
			name: 'z',
			data: listz,
			color: '#0000FF'
		}],
		tooltip: { 
			enabled: true,
			backgroundColor: '#333',
    		borderColor: '#333',
    		borderRadius: 2,
    		borderWidth: 1,
    		padding: 2,
    		style: {
    			color: '#fff'
    		},
    		formatter: function () {
            	return Math.round(this.y);
        	}
		},
		title: {
        	text: ''
    	},
    	yAxis: {
    		title: '',
    		labels: {
    			enabled: false
    		}
    	},
    	xAxis: {
    		visible: false,
    		labels: {
    			enabled: false
    		}
    	},
    	legend: {
        	enabled: false
    	},
    	credits: {
        	enabled: false
    	},
    	plotOptions: {
        	series: {

        		animation: false,
            	color: '#333'
        		
    		},
    		line: {
        		marker: {
        			enabled: false
        		}
        	}	
    	}
	});
}

async function testBounds(resolution){
	for (let y = resolution/2; y <= MAP_HEIGHT; y += resolution) {
		for (let x = resolution/2; x <= MAP_WIDTH; x += resolution){
			await sleep(100);
			sendDrive($("#robotID").val(), x, y)
		}
	}

	for (let x = 0; x <= MAP_WIDTH; x += resolution){
		for (let y = 0; y <= MAP_HEIGHT; y += resolution) {
			await sleep(100);
			sendDrive($("#robotID").val(), x, y)
		}
	}


}


function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}