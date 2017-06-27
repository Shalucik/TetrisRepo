package com.youngcapital.tetris.complete;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youngcapital.tetris.complete.block.Block;
import com.youngcapital.tetris.complete.block.BlockRepository;
import com.youngcapital.tetris.complete.block.Orientation;
import com.youngcapital.tetris.complete.block.Pos;
import com.youngcapital.tetris.complete.block.TetrisBlock;
import com.youngcapital.tetris.complete.websocket.ControlMessage;
import com.youngcapital.tetris.complete.websocket.Greeting;
import com.youngcapital.tetris.complete.websocket.MoveGreeting;
import com.youngcapital.tetris.complete.websocket.MoveMessage;

@Controller
public class TetrisController {

	private boolean[][] grid;
	private TetrisBlock currentBlock;
	private int gridHeight;
	private int gridWidth;
	
	@Autowired
	private BlockRepository bRepo;

	@RequestMapping("/tetris")
	public String createPage(Model model){
		gridHeight = 20;
		gridWidth = 10;
		grid = new boolean[gridHeight][gridWidth];
		model.addAttribute("gridHeight", gridHeight-1);
		model.addAttribute("gridWidth", gridWidth-1);
		currentBlock = null;
		return "tetris";
	}

	@MessageMapping("/controls")
	@SendTo("/tetris/output")
	public synchronized Greeting controlgreeting(ControlMessage message){
		switch(message.getKeyboardCode()){
			case 37:
				return moveGreeting(new MoveMessage(-1,0));
			case 38:
				return rotationGreeting();
			case 39:
				return moveGreeting(new MoveMessage(1,0));
			case 40:
			default:
				return new MoveGreeting("no change");
		}
	}
	
	private Greeting rotationGreeting(){
		int rotation = currentBlock.getCurrentOrientation() + 1;
		int max = currentBlock.getOrientations().length;
		if(rotation < 0)
			rotation = max-1;
		else if(rotation >= max)
			rotation = 0;
		Point[] curPos = currentBlock.getCurrentPositions();
		Point[] newPos = addPointToArray(currentBlock.getCurrentPosition(), currentBlock.getOrientations()[rotation]);
		for(Point pos : newPos){
			if(checkGrid(pos))
				return new MoveGreeting("can't rotate");
		}
		currentBlock.setCurrentOrientation(rotation);
		currentBlock.setCurrentPositions(newPos);
		return new MoveGreeting("rotating", curPos, newPos, currentBlock.getColor());
	}
	
	public TetrisBlock createBlock(){
		Block block = getRandomBlock();
		
		Point curPos = getPoint(block.getCurrentPos());
		
		Point[][] oris = new Point[4][];
		oris[0] = getOrientation(block.getOrientation0());
		oris[1] = getOrientation(block.getOrientation1());
		oris[2] = getOrientation(block.getOrientation2());
		oris[3] = getOrientation(block.getOrientation3());
		
		return new TetrisBlock(curPos, oris, 0, addPointToArray(curPos, oris[0]), block.getColor());
	}
	
	private Point[] addPointToArray(Point point, Point[] array){
		Point[] curPos = new Point[array.length];
		for(int i = 0; i < curPos.length; i++){
			curPos[i] = addPointToPoint(point, array[i]);
		}
		return curPos;
	}
	
	private Point addPointToPoint(Point p, Point q){
		return new Point(p.x + q.x, p.y + q.y);
	}
	
	public Point[] getOrientation(Orientation ori){
		Point[] oris = new Point[4];
		oris[0] = getPoint(ori.getPosition0());
		oris[1] = getPoint(ori.getPosition1());
		oris[2] = getPoint(ori.getPosition2());
		oris[3] = getPoint(ori.getPosition3());
		return oris;
	}
	
	public Point getPoint(Pos position){
		return new Point(position.getX(), position.getY());
	}

	private Block getRandomBlock() {
		List<Block> list = null;
		int blockNum = (int) (Math.random() * 7);
		switch (blockNum) {
		case 6:
			list = bRepo.findByBlockType("Line");
			break;
		case 5:
			list = bRepo.findByBlockType("Square");
			break;
		case 4:
			list = bRepo.findByBlockType("S");
			break;
		case 3:
			list = bRepo.findByBlockType("Z");
			break;
		case 2:
			list = bRepo.findByBlockType("T");
			break;
		case 1:
			list = bRepo.findByBlockType("J");
			break;
		case 0:
			list = bRepo.findByBlockType("L");
			break;
		}
		
		return list == null ? null : list.get(0);
	}

	@MessageMapping("/move")
	@SendTo("/tetris/move")
	public Greeting moveGreeting(MoveMessage message) {
		if (currentBlock == null) {
			currentBlock = createBlock();
			return new MoveGreeting("there was no block", new Point[]{}, currentBlock.getCurrentPositions(), currentBlock.getColor());
		}
		
		Point move = new Point(message.getX(), message.getY());
		
		Point[] currentPositions = currentBlock.getCurrentPositions();
		Point[] newPositions = addPointToArray(move, currentPositions);
		for(int i = 0; i < newPositions.length; i++){
			if((message.getX() != 0 && checkGrid(newPositions[i]))){
				return new MoveGreeting("can't move");
			}
			if(newPositions[i].y == grid.length || checkGrid(newPositions[i])){
				updateGrid(currentPositions);
				currentBlock = createBlock();
				Point[] line = checkForLines();
				if (line.length > 0) {
					return new MoveGreeting("clearLines", line, null, null);
				}
				return new MoveGreeting("new block", new Point[]{}, currentBlock.getCurrentPositions(), currentBlock.getColor());
			}
		}
		currentBlock.setCurrentPosition(addPointToPoint(currentBlock.getCurrentPosition(), move));
		currentBlock.setCurrentPositions(newPositions);
		return new MoveGreeting("continue", currentPositions, newPositions, currentBlock.getColor());
	}
	
	public void updateGrid(Point[] positions){
		for (int i = 0; i < positions.length; i++) {
			int x = positions[i].x;
			int y = positions[i].y;
			if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
				grid[y][x] = true;
				if(checkLine(y)){
					
				}
			}
		}
	}
	
	private boolean checkLine(int height){
		for(int i = 0; i < gridWidth; i++){
			if(!checkGrid(i, height));
				return false;
		}
		return true;
	}

	private Point[] checkForLines() {
		ArrayList<Point> linePositions = new ArrayList<>();
		for (int i = gridHeight - 1; i >= 0; i--) {
			boolean fullLine = true;
			for (int j = 0; j < gridWidth; j++) {
				if(!grid[i][j]) {
					fullLine = false;
					break;
				}
			}
			
			if (fullLine) {
				System.out.println("Full line");
				linePositions.add(new Point(0, i));
				for (int j = i - 1; j >= 0; j--) {
					for (int k = 0; k < grid[i].length; k++) {
						grid[j + 1][k] = grid[j][k];
					}
				}
			}
		}
		return linePositions.toArray(new Point[0]);
		
	}

	private boolean checkGrid(Point point) {
		return checkGrid(point.x, point.y);
	}
	
	private boolean checkGrid(int width, int height){
		if (width >= 0 && width < gridWidth && height >= 0 && height < gridHeight) {
			return grid[height][width];
		}
		return true;
	}

	@RequestMapping(value = "/getblock/{blockType}")
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

		sb.append("Current position: ").append("(").append(block.getCurrentPos().getX()).append(", ")
				.append(block.getCurrentPos().getY()).append(")");

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
		s.setCurrentPos(new Pos(5, 0));
		s.setBlockType("S");

		bRepo.save(s);

		// Z
		Block z = new Block();
		z.setOrientation0(new Orientation(new Pos(0, 0), new Pos(-1, 0), new Pos(0, 1), new Pos(1, 1)));
		z.setOrientation1(new Orientation(new Pos(0, 0), new Pos(0, 1), new Pos(-1, 1), new Pos(-1, 2)));
		z.setOrientation2(new Orientation(new Pos(0, 0), new Pos(-1, 0), new Pos(0, 1), new Pos(1, 1)));
		z.setOrientation3(new Orientation(new Pos(0, 0), new Pos(0, 1), new Pos(-1, 1), new Pos(-1, 2)));
		z.setColor("rgb(255, 0, 0)");
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
		l.setCurrentPos(new Pos(4, 1));
		l.setBlockType("L");

		bRepo.save(l);

		return "Added database entries";
	}
}
