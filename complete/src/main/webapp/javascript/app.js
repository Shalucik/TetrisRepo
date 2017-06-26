var stompClient = null;

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
		stompClient.subscribe('/tetris/init', function(greeting){
			updateBlock(greeting);
		});
		stompClient.subscribe('/tetris/output', function (greeting) {
			alert(JSON.parse(greeting.body).content);
		});
		stompClient.subscribe('/tetris/move', function(greeting){
			updateBlock(greeting);
		})
		stompClient.send("/app/init", {}, "");
		loop();
	});
}

function disconnect() {
	if(stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function init(){
	connect();
}

function updateBlock(greeting){
	var update = JSON.parse(greeting.body);
		if (update.color != "gray") {
		$.each(update.grayPositions, function(key, value){
			$("#" + value.x + value.y).css('background', 'gray');
		});
		$.each(update.colorPositions, function(key, value){
			$("#" + value.x + value.y).css('background', update.color);
		});
	} else {
		$.each(update.grayPositions, function(key, value) {
			for (var j = value.y; j >= 0; j--) {
				for (var i = 0; i < 10; i++) {
					$("#" + i + j).css('background', 
					$("#" + i + (j - update.grayPositions.length)).css('background-color')		
					);
				}	
			}
		});
	}
}

function loop(){
	var interval = setInterval(function(){
		stompClient.send("/app/move", {}, JSON.stringify({'x' : 0, 'y' : 1}));
	},100);
}

function keyInput(keycode) {
		if(keycode >= 37 && keycode <= 40){
			stompClient.send("/app/controls", {}, JSON.stringify({'keyboardCode': keycode}));
		}
}

$(function(){
	$("#connect").click(function() {connect();});
	$("#disconnect").click(function() {disconnect();});
	$(document).keydown(function(event) {keyInput(event.which);});
	init();
})