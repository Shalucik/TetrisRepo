<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tetris</title>
<link rel="stylesheet" href="css/tetris.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/bootstrap-theme.css">

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>
<script src="javascript/app.js"></script>
</head>

<body>
	<button id="start" class="btn btn-default" type="submit" disabled="disabled">Start</button>
	<button id="stop" class="btn btn-default" type="submit" disabled="disabled">Stop</button>
	<button id="connect" class="btn btn-default" type="submit">Connect</button>
	<button id="disconnect" class="btn btn-default" type="submit" disabled="disabled">Disconnect</button>
	<div id="highscore">
	<div class="input-group">		
		<input id="name" type="text" class="form-control" placeholder="name">
		<span class="input-group-btn">
			<button id="scoring" class="btn btn-default" type="button">Submit</button>
		</span>
	</div>
	</div>
	<div>Score = <span id="score">0</span></div>
	<table>
		<tbody>
			<c:forEach var="i" begin="0" end="3">
				<tr>
					<c:forEach var="j" begin="0" end="3">
						<td id="next${j}${i}"></td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<table><tbody>
		<c:forEach var="i" begin="0" end="${gridHeight}">
			<tr>
				<c:forEach var="j" begin="0" end="${gridWidth}">
					<td id="${j}${i}"></td>
				</c:forEach>
			</tr>
		</c:forEach>
	</tbody></table>
</body>
</html>