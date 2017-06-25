package com.youngcapital.tetris.complete;


import java.awt.Point;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//<<<<<<< HEAD
import com.youngcapital.tetris.complete.block.Block;
import com.youngcapital.tetris.complete.block.BlockRepository;
import com.youngcapital.tetris.complete.block.Orientation;
import com.youngcapital.tetris.complete.block.Pos;
import com.youngcapital.tetris.complete.websocket.ControlMessage;

import com.youngcapital.tetris.complete.websocket.ControlMessage;
import com.youngcapital.tetris.complete.websocket.Greeting;
import com.youngcapital.tetris.complete.websocket.MoveMessage;

@Controller
public class TetrisController {
	
	private boolean[][] grid;
	private Point[] currentPositions;
	private Block currentBlock;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private BlockRepository bRepo;
	
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
		return new Greeting(new Point[]{}, currentPositions, currentBlock.getColor());
	}
	
	public Point[] createBlock(){
		currentBlock = getRandomBlock();
		return getBlockPoints(currentBlock);		
	}
	
	private Point[] getBlockPoints(Block block) {
		Point[] points = new Point[4];
		Orientation[] ors = new Orientation[4];
		int orientation = block.getCurrentOrientation();
		
		int offX = block.getCurrentPos().getX();
		int offY = block.getCurrentPos().getY();
		
		ors[0] = block.getOrientation0();
		ors[1] = block.getOrientation1();
		ors[2] = block.getOrientation2();
		ors[3] = block.getOrientation3();
		
		points[0] = new Point(ors[orientation].getPosition0().getX() + offX,
								ors[orientation].getPosition0().getY() + offY);
		
		points[1] = new Point(ors[orientation].getPosition1().getX() + offX,
				ors[orientation].getPosition1().getY() + offY);
		
		points[2] = new Point(ors[orientation].getPosition2().getX() + offX,
				ors[orientation].getPosition2().getY() + offY);
		
		points[3] = new Point(ors[orientation].getPosition3().getX() + offX,
				ors[orientation].getPosition3().getY() + offY);
		
		return points;
	}
	
	private Block getRandomBlock() {
		List<Block> list = null;
		int blockNum = (int)(Math.random() * 7);
		switch(blockNum) {
		case 6:
			list = bRepo.findByBlockType("Line"); break;
		case 5:
			list = bRepo.findByBlockType("Square"); break;
		case 4:
			list = bRepo.findByBlockType("S"); break;
		case 3:
			list = bRepo.findByBlockType("Z"); break;
		case 2:
			list = bRepo.findByBlockType("T"); break;
		case 1:
			list = bRepo.findByBlockType("J"); break;
		case 0:
			list = bRepo.findByBlockType("L"); break;
		}
		
		if (list == null) {
			return null;
		} else
		{
			Block block = list.get(0);
			block.setCurrentOrientation((int)(Math.random() * 4));
			return block;
		}
	}
	
	@MessageMapping("/move")
	@SendTo("/tetris/move")
	public Greeting moveGreeting(MoveMessage message) {
		Point move = new Point(message.getX(), message.getY());
		Point[] newPositions = new Point[currentPositions.length];
		for(int i = 0; i < newPositions.length; i++){
			newPositions[i] = new Point(currentPositions[i].x + message.getX(), currentPositions[i].y + message.getY());
			if((message.getX() != 0 && checkGrid(newPositions[i]))){
				return new Greeting();
			}
			if(newPositions[i].y == grid.length || checkGrid(newPositions[i])){
				updateGrid();
				return initGreeting();
			}
		}
		return new Greeting(currentPositions, currentPositions = newPositions, currentBlock.getColor());
	}
	
	public void updateGrid(){
		for (int i = 0; i < currentPositions.length; i++) {
			int x = (int)currentPositions[i].getX();
			int y = (int)currentPositions[i].getY();
			if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
				grid[y][x] = true;
			}
		}
	}
	
	public boolean checkGrid(Point point){
		if(point.x >= 0 && point.x < grid[0].length && point.y >= 0 && point.y < grid.length){
			return grid[point.y][point.x];
		}
		return true;
	}
	
	@RequestMapping(value="/getblock/{blockType}")
	public @ResponseBody String getByName(@PathVariable String blockType) {
		List<Block> list = bRepo.findByBlockType(blockType);
		
		Block block = null;
		if (list.size() > 0) {
			block = list.get(0);
		} else {
			return "Invalid block name.<br>Valid names are Line, Square, S, Z. T, J, L";
		}
		
		
		
		Orientation[] ors = new Orientation[4];
		ors[0] = block.getOrientation0();
		ors[1] = block.getOrientation1();
		ors[2] = block.getOrientation2();
		ors[3] = block.getOrientation3();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Current orientation: ").append(block.getCurrentOrientation()).append("<br>");
		
		for (int i = 0; i < ors.length; i++) {
		sb.append("Orientation ").append(i).append("<br>");
			Pos[] pos = new Pos[4];
			pos[0] = ors[i].getPosition0();
			pos[1] = ors[i].getPosition1();
			pos[2] = ors[i].getPosition2();
			pos[3] = ors[i].getPosition3();
			for (int j = 0; j < pos.length; j++) {
			sb.append("Pos ").append(j);
			sb.append("(").append(pos[j].getX()).append(", ").append(pos[j].getY()).append(")<br>");
			}
		}
		
		sb.append("Color: ").append(block.getColor()).append("<br>");
		
		sb.append("Current position: ").append("(").append(block.getCurrentPos().getX())
		.append(", ").append(block.getCurrentPos().getY()).append(")");
			
		return "Info for block corresponding to " + blockType + ":<br>" + sb.toString();
	}

	@RequestMapping("/test")
	public @ResponseBody String makeDB() {
		bRepo.deleteAll();
		
		Block block = new Block();

		// line
		block.setOrientation0(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(2, 0), new Pos(3, 0)));
		block.setOrientation1(new Orientation(new Pos(1, 0), new Pos(1, 1), new Pos(1, 2), new Pos(1, 3)));
		block.setOrientation2(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(2, 0), new Pos(3, 0)));
		block.setOrientation3(new Orientation(new Pos(1, 0), new Pos(1, 1), new Pos(1, 2), new Pos(1, 3)));
		block.setColor("rgb(100, 149, 237)");
		block.setCurrentOrientation(0);
		block.setCurrentPos(new Pos(3, 0));
		block.setBlockType("Line");

		bRepo.save(block);

		// Square
		Block square = new Block();
		square.setOrientation0(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)));
		square.setOrientation1(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)));
		square.setOrientation2(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)));
		square.setOrientation3(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)));
		square.setColor("rgb(0, 0, 255)");
		square.setCurrentOrientation(0);
		square.setCurrentPos(new Pos(4, 0));
		square.setBlockType("Square");

		bRepo.save(square);

		// S
		Block s = new Block();
		s.setOrientation0(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 1)));
		s.setOrientation1(new Orientation(new Pos(-1, 1), new Pos(-1, 0), new Pos(0, 1), new Pos(0, 2)));
		s.setOrientation2(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 1)));
		s.setOrientation3(new Orientation(new Pos(-1, 1), new Pos(-1, 0), new Pos(0, 1), new Pos(0, 2)));
		s.setColor("rgb(0, 255, 0)");
		s.setCurrentOrientation(0);
		s.setCurrentPos(new Pos(5, 0));
		s.setBlockType("S");

		bRepo.save(s);

		// Z
		Block z = new Block();
		z.setOrientation0(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 1)));
		z.setOrientation1(new Orientation(new Pos(-1, 1), new Pos(-1, 0), new Pos(0, 1), new Pos(0, 2)));
		z.setOrientation2(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 1)));
		z.setOrientation3(new Orientation(new Pos(-1, 1), new Pos(-1, 0), new Pos(0, 1), new Pos(0, 2)));
		z.setColor("rgb(255, 0, 0)");
		z.setCurrentOrientation(0);
		z.setCurrentPos(new Pos(4, 0));
		z.setBlockType("Z");

		bRepo.save(z);

		// T
		Block t = new Block();
		t.setOrientation0(new Orientation(new Pos(0, -1), new Pos(-1, 0), new Pos(0, 0), new Pos(1, 0)));
		t.setOrientation1(new Orientation(new Pos(0, -1), new Pos(0, 0), new Pos(1, 0), new Pos(0, 1)));
		t.setOrientation2(new Orientation(new Pos(-1, -1), new Pos(0, -1), new Pos(1, -1), new Pos(0, 0)));
		t.setOrientation3(new Orientation(new Pos(0, -1), new Pos(0, 0), new Pos(-1, 0), new Pos(0, 1)));
		t.setColor("rgb(255, 165, 0)");
		t.setCurrentOrientation(0);
		t.setCurrentPos(new Pos(4, 1));
		t.setBlockType("T");

		bRepo.save(t);

		// J
		Block j = new Block();
		j.setOrientation0(new Orientation(new Pos(0, 0), new Pos(0, -1), new Pos(0, 1), new Pos(-1, 1)));
		j.setOrientation1(new Orientation(new Pos(1, 0), new Pos(0, 0), new Pos(-1, 0), new Pos(-1, -1)));
		j.setOrientation2(new Orientation(new Pos(-1, 0), new Pos(-1, -1), new Pos(0, -1), new Pos(-1, 1)));
		j.setOrientation3(new Orientation(new Pos(-1, -1), new Pos(0, -1), new Pos(1, -1), new Pos(1, 0)));
		j.setColor("rgb(255, 215, 0)");
		j.setCurrentOrientation(0);
		j.setCurrentPos(new Pos(5, 1));
		j.setBlockType("J");

		bRepo.save(j);

		// L
		Block l = new Block();
		l.setOrientation0(new Orientation(new Pos(0, 0), new Pos(0, -1), new Pos(0, 1), new Pos(1, 1)));
		l.setOrientation1(new Orientation(new Pos(1, -1), new Pos(0, -1), new Pos(-1, -1), new Pos(-1, 0)));
		l.setOrientation2(new Orientation(new Pos(-1, -1), new Pos(0, -1), new Pos(0, 0), new Pos(0, 1)));
		l.setOrientation3(new Orientation(new Pos(-1, 0), new Pos(0, 0), new Pos(1, 0), new Pos(1, -1)));
		l.setColor("rgb(148, 0, 211)");
		l.setCurrentOrientation(0);
		l.setCurrentPos(new Pos(4, 1));
		l.setBlockType("L");

		bRepo.save(l);

		return "Added database entries";
	}
}
