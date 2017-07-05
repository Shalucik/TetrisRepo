var stompClient = null;
var control = true;
var move = true;
var gameInterval;
var time = 500;
var lines = 0;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	$("#test").prop("disabled", !connected);
	$("#start").prop("disabled", !connected);
}

function connect() {
	var socket = new SockJS('/tetris-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/user/queue/controls', function(greeting) {
			while (!move) {
			}
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
				score();
				control = true;
				move = true;
				break;
			}
		});
		stompClient.subscribe('/user/queue/move', function(greeting) {
			while (!move) {
			}
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
				score();
				control = true;
				move = true;
				break;
			}
		});
		stompClient.subscribe('/user/queue/reset', function(greeting) {
			while (!move) {
			}
			var update = JSON.parse(greeting.body);
			switch (update.status) {
			case 2:
				resetGrid();
				control = true;
				move = true;
				break;
			}
		});
		stompClient.subscribe('/user/queue/highscore', function(greeting){
			$(".highscoreRow").remove();
			var scores = JSON.parse(greeting.body).scores;
			for(var i = 0; i < scores.length; i++){
				$('#highscoreTable tr:last').after('<tr class="highscoreRow"><td>' + (i+1) + '</td><td>'+ scores[i].name + '</td><td>' + scores[i].level + '</td><td>' + scores[i].score + '</td></tr>')
			}
		});
	});
}

function resetGrid() {
	$(".Score").text(0);
	$(".Level").text(0);
	lines = 0;
	$(".Lines").text(lines);
	time = 500;
	for (var i = 0; i < 20; i++)
		for (var j = 0; j < 10; j++) {
			$("#" + j + i).css('background', 'gray');
		}
	for (var y = 0; y < 4; y++) {
		for (var x = 0; x < 4; x++) {
			$("#next" + y + x).css('background', 'gray');
		}
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

function updateLine(greeting) {
	$(".Score").text(greeting.score);
	$(".Level").text(greeting.level);
	lines += greeting.lines.length;
	$(".Lines").text(lines);
	
	$.each(greeting.lines, function(key, value) {
		for (var j = value; j >= 0; j--) {
			for (var i = 0; i < 10; i++) {
				$("#" + i + j).css('background',
						$("#" + i + (j - 1)).css('background-color'));
				$("#" + i + j).css('transition', 'none');
			}
		}
	});
	adjustLevel(greeting.level);
}

function adjustLevel(lvl) {
		time = 500 * Math.pow(0.9, lvl);		
	
	clearInterval(gameInterval);
	gameInterval = setInterval(loop, time);
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

function loop() {
	stompClient.send("/app/move", {}, JSON.stringify({
		'x' : 0,
		'y' : 1
	}));
}

function start() {
	stompClient.send('/app/reset', {}, "");
	$("#start").prop("disabled", true);
	$("#stop").prop("disabled", false);
	
	if(gameInterval) {
		clearInterval(gameInterval);
		gameInterval = 0;
	} else {
		gameInterval = setInterval(loop, time);
	}
}

function stop() {
	$("#start").prop("disabled", false);
	$("#stop").prop("disabled", true);
	clearInterval(gameInterval);
	gameInterval = 0;
}

function score() {
	stop();
	$('#myModal').modal('toggle');
}

function highscore() {

	stompClient.send("/app/score", {}, JSON.stringify({
		'name' : $("#name").val()
	}));
	$('#myModal').modal('toggle');
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
	$(document).keydown(function(event) {
		keyInput(event.which);
	});
	$("#start").click(function() {
		start();
	});
	$("#stop").click(function() {
		stop();
	});
	$("#scoring").click(function() {
		highscore();
	});
	$("#highscoreButton").click(function () {
		stompClient.send("/app/highscore", {} ,"");
		$("#highscoreModal").modal('toggle');
	});
	init();
})