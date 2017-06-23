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
<script>
var or = JSON.parse('${block.orientations}').orientations[0];
var text = "Calculated absolute positions:";
for (var i = 0; i < 4; i++) {
	text += (or[i].x + ${block.currentX}) + " " + (or[i].y + ${block.currentY}) + "<br>";	
}
$("body").html(text);
</script>
	<div class="btn-group btn-group" role="group">
		<button id="connect" class="btn btn-default" type="submit">Connect</button>
		<button id="disconnect" class="btn btn-default" type="submit"
			disabled="disabled">Disconnect</button>
		<button id="test" class="btn btn-default" type="submit"
			disabled="disabled">test</button>
	</div>
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

</body>
</html>