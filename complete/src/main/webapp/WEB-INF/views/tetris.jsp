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
<script src="javascript/tether.min.js"></script>
<script src="javascript/bootstrap.js"></script>
<script src="javascript/app.js"></script>
</head>

<body>


	<!-- Scoring Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Game Over</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Final score:
					<div id="highscore">
						<div class="input-group">
							<input id="name" type="text" class="form-control"
								placeholder="name"> <span class="input-group-btn">
							</span>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
					<button id="scoring" class="btn btn-primary" type="button">Submit</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Highscore Modal -->
	<div class="modal fade" id="highscoreModal" tabindex="-1" role="dialog"
		aria-labbeledby="HighscoreModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="HighscoreModalLabel">High Scores</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Hello
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	
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
			<div>
				<span class="scoreFont">Score<span id="score">0</span></span>
			</div>
					<div>
				<span class="scoreFont">Level <span id="level">0</span></span>
			</div>
			<div><button id="start" class="btn btn-default" type="submit"
				disabled="disabled">Start</button></div>
			<div><button id="stop" class="btn btn-default" type="submit"
				disabled="disabled">Stop</button></div>
			<div><button id="connect" class="btn btn-default" type="submit">Connect</button></div>
			<div><button id="disconnect" class="btn btn-default" type="submit"
				disabled="disabled">Disconnect</button></div>
			<div><button id="highscores" class="btn btn-default" type="submit">Highscores</button></div>
		</div>
	</div>

</body>
</html>