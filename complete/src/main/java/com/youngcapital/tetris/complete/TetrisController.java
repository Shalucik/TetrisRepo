package com.youngcapital.tetris.complete;

import java.awt.Point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.youngcapital.tetris.complete.websocket.ControlMessage;
import com.youngcapital.tetris.complete.websocket.Greeting;
import com.youngcapital.tetris.complete.websocket.MoveMessage;

@Controller
public class TetrisController {
	
	private boolean[][] grid;
	private Point[] currentPositions;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@RequestMapping("/tetris")
	public String createPage(Model model){
		int gridHeight = 20;
		int gridWidth = 10;
		grid = new boolean[gridHeight][gridWidth];
		model.addAttribute("gridHeight", gridHeight-1);
		model.addAttribute("gridWidth", gridWidth-1);
		return "tetris";
	}
	
	@MessageMapping("/controls")
	@SendTo("/tetris/move")
	public Greeting controlgreeting(ControlMessage message){
		switch(message.getKeyboardCode()){
			case 37:
				return moveGreeting(new MoveMessage(-1,0));
			case 38:
			case 39:
				return moveGreeting(new MoveMessage(1,0));
			case 40:
			default:
				return new Greeting("no change");
		}
	}
	
	@MessageMapping("/init")
	@SendTo("/tetris/init")
	public Greeting initGreeting() {
		currentPositions = createBlock();
		return new Greeting(new Point[]{}, currentPositions, "red");
	}
	
	public Point[] createBlock(){
		return new Point[]{new Point(4,0)};
	}
	
	@MessageMapping("/move")
	@SendTo("/tetris/move")
	public Greeting moveGreeting(MoveMessage message) {
		Point move = new Point(message.getX(), message.getY());
		Point[] oldPositions = new Point[currentPositions.length];
		for(int i = 0; i < oldPositions.length; i++){
			oldPositions[i] = (Point) currentPositions[i].clone();
			currentPositions[i].setLocation(currentPositions[i].getX() + message.getX(), currentPositions[i].getY() + message.getY());
			if(currentPositions[i].getX() < 0 || currentPositions[i].getX() >= grid[0].length || currentPositions[i].getY() < 0)
				return new Greeting();
		}
		
		
		
		return new Greeting(oldPositions, currentPositions, "red");
	}
}
