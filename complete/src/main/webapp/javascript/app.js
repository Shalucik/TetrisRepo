var stompClient = null;
var control = true;
var move = true;
var interval;
var time = 500;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	$("#test").prop("disabled", !connected);
}

function connect() {
	var socket = new SockJS('/tetris-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/user/queue/controls', function (greeting) {
			while(!move){}
			var update = JSON.parse(greeting.body);
			switch(update.status){
				case 0:
					updateBlock(update);
					control = true;
					move = true;
					break;
				case 1:
					updateLine(update);
					control = true;
					move = true;
					break;
				case 2:
					resetGrid();
					control = true;
					move = true;
					break;					
			}
		});
		stompClient.subscribe('/user/queue/move', function(greeting){
			while(!move){}
			var update = JSON.parse(greeting.body);
			switch(update.status){
			case 0:
				updateBlock(update);
				control = true;
				move = true;
				break;
			case 1:
				updateLine(update);
				control = true;
				move = true;
				break;
			case 2:
				resetGrid();
				control = true;
				move = true;
				break;					
			}
		});
		var speed = setInterval(function() {
			clearInterval(interval);
			time -= 1;
			loop(time);			
		}, 1000);
		
	});
}

function resetGrid(){
	$("#score").text(0);
	time = 500;
	for(var i = 0; i < 20; i++)
		for(var j = 0; j < 10; j++){
			$("#" + j + i).css('background', 'gray');
		}
}

function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function init() {
	connect();
}

function updateBlock(greeting) {
	move = false;
		$.each(greeting.grayPositions, function(key, value){
			$("#" + value.x + value.y).css('background', 'gray');
		});
		$.each(greeting.colorPositions, function(key, value){
			$("#" + value.x + value.y).css('background', greeting.color);
		});
}
				
function updateLine(greeting){
	$("#score").text(greeting.score);
	$.each(greeting.lines, function(key, value) {
		for (var j = value; j >= 0; j--) {
			for (var i = 0; i < 10; i++) {
				$("#" + i + j).css('background',
						$("#" + i + (j - 1)).css('background-color'));
			}
		}
	});
}


function loop(time) {
	interval = setInterval(function() {
		stompClient.send("/app/move", {}, JSON.stringify({
			'x' : 0,
			'y' : 1
		}));
	}, time);	
}


function keyInput(keycode) {
	if (keycode >= 37 && keycode <= 40 && control) {
		control = false;
		stompClient.send("/app/controls", {}, JSON.stringify({
			'keyboardCode' : keycode
		}));
	}
}

$(function() {
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$(document).keydown(function(event) {
		keyInput(event.which);
	});
	init();
})