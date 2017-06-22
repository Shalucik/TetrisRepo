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
		stompClient.subscribe('/tetris/output', function (greeting) {
			alert(JSON.parse(greeting.body).content);
		});
	});
}

function disconnect() {
	if(stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function test(keyCode) {
	if(keyCode >= 37 && keyCode <= 40)
		stompClient.send("/app/controls", {}, JSON.stringify({'keyboardCode': keyCode}));
}

$(function(){
	$("#connect").click(function() {connect();});
	$("#disconnect").click(function() {disconnect();});
	$("#test").click(function() {test(1);});
	$(document).keydown(function(event) {test(event.which);});
})