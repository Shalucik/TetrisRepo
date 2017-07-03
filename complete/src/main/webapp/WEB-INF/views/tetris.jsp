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


<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Game Over</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        Final score: 
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Submit</button>
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
			<button id="start" class="btn btn-default" type="submit"
				disabled="disabled">Start</button>
			<button id="stop" class="btn btn-default" type="submit"
				disabled="disabled">Stop</button>
			<button id="connect" class="btn btn-default" type="submit">Connect</button>
			<button id="disconnect" class="btn btn-default" type="submit"
				disabled="disabled">Disconnect</button>
			<div id="highscore">
				<div class="input-group">
					<input id="name" type="text" class="form-control"
						placeholder="name"> <span class="input-group-btn">
						<button id="scoring" class="btn btn-default" type="button">Submit</button>
					</span>
				</div>



			</div>
			<div>
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