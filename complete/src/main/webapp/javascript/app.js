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
			switch (update.status) {
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
			switch (update.status) {
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
	$.each(greeting.grayPositions, function(key, value) {
		$("#" + value.x + value.y).css('background', 'gray');
	});
	$.each(greeting.colorPositions, function(key, value) {
		$("#" + value.x + value.y).css('background', greeting.color);
	});

	console.log(greeting);

	showNextBlock(greeting);
}
				
function updateLine(greeting){
	$("#score").text(greeting.score);
	$.each(greeting.lines, function(key, value) {
		for (var j = value; j >= 0; j--) {
			for (var i = 0; i < 10; i++) {
				$("#" + i + j).css('transition', '1s');
				$("#" + i + j).css('background',
						$("#" + i + (j - 1)).css('background-color'));
				$("#" + i + j).css('transition', 'none');
			}
		}
	});
}

function showNextBlock(greeting) {
	if (greeting.nextPositions != null && greeting.nextPositions.length > 0) {
		for (var y = 0; y < 4; y++) {
			for (var x = 0; x < 4; x++) {
				$("#next" + y + x).css('background', 'gray');
			}
		}

		var lowestX = 0;
		var lowestY = 0;

		$.each(greeting.nextPositions, function(key, value) {
			if (value.x < 0) {
				if (value.x < lowestX) {
					lowestX = value.x;
				}
			}
			if (value.y < 0) {
				if (value.y < lowestY) {
					lowestY = value.y;
				}
			}
		});

		lowestX *= -1;
		lowestY *= -1;

		var increaseX = true;
		var increaseY = true;
		$.each(greeting.nextPositions, function(key, value) {
			if (value.x + lowestX >= 2) {
				increaseX = false;
			}
			if (value.y + lowestY >= 3) {
				increaseY = false;
			}
		});
		
		if (increaseX) {
			lowestX++;
		}
		if (increaseY) {
			lowestY++;
		}

		$.each(greeting.nextPositions, function(key, value) {
			$("#next" + (value.x + lowestX) + (value.y + lowestY)).css(
					'background', greeting.nextColor);
		});
	}
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
	if ((keycode >= 37 && keycode <= 40 && control) || keycode == 32) {
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