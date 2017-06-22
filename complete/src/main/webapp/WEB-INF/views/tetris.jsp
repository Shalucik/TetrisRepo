<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Tetris</title>
	<link rel="stylesheet" href="css/tetris.css">
	<script src="/webjars/jquery/jquery.min.js"></script>
	<script src="/webjars/sockjs-client/sockjs.min.js"></script>
	<script src="/webjars/stomp-websocket/stomp.min.js"></script>
	<script src="javascript/app.js"></script>
</head>

<body>
	<button id="connect" class="btn btn-default" type="submit">Connect</button>
	<button id="disconnect" class="btn btn-default" type="submit" disabled="disabled">Disconnect</button>
	<button id="test" class="btn btn-default" type="submit" disabled="disabled">test</button>
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