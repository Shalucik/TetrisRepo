<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tetris</title>
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/bootstrap-theme.css">
<link rel="stylesheet" href="css/tetris.css">

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>
<script src="javascript/app.js"></script>
</head>

<body>
	<div id="interface">
			<div class="box field">
				<table>
					<tbody>
						<c:forEach var="i" begin="0" end="${gridHeight}">
							<tr>
								<c:forEach var="j" begin="0" end="${gridWidth}">
									<td id="${j}${i}"></td>
								</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="box score">
				<button id="connect" class="btn btn-default" type="submit">Connect</button>
				<button id="disconnect" class="btn btn-default" type="submit"
					disabled="disabled">Disconnect</button>
				<div id="scoredisplay">
					<span id="scoreFont">Score<span id="score">0</span></span>
				</div>
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
			</div>
		</div>
</body>
</html>