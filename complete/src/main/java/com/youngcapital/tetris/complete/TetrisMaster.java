package com.youngcapital.tetris.complete;

import java.awt.Point;
import java.util.ArrayList;

import com.youngcapital.tetris.complete.block.TetrisBlock;
import com.youngcapital.tetris.complete.websocket.Greeting;
import com.youngcapital.tetris.complete.websocket.LineGreeting;
import com.youngcapital.tetris.complete.websocket.MoveGreeting;
import com.youngcapital.tetris.complete.websocket.MoveMessage;

public class TetrisMaster {
	
	private int gridWidth;
	private int gridHeight;
	private boolean[][] grid;
	private Long score = new Long(0);

	public TetrisMaster(int gridWidth, int gridHeight){
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		resetGrid();
	}
	
	void resetGrid(){
		grid = new boolean[gridHeight][gridWidth];
	}
	
	Greeting rotationGreeting(TetrisBlock currentBlock, TetrisBlock nextBlock){
		if (currentBlock != null) {
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
		return new MoveGreeting("");
	}
	
	static Point[] addPointToArray(Point point, Point[] array){
		Point[] curPos = new Point[array.length];
		for(int i = 0; i < curPos.length; i++){
			curPos[i] = addPointToPoint(point, array[i]);
		}
		return curPos;
	}
	
	static Point addPointToPoint(Point p, Point q){
		return new Point(p.x + q.x, p.y + q.y);
	}
	
	int[] updateGrid(Point[] positions){
		ArrayList<Integer> lines = new ArrayList<>();
		for (int i = 0; i < positions.length; i++) {
			int x = positions[i].x;
			int y = positions[i].y;
			if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
				grid[y][x] = true;				
				if(checkLine(y)){
					if (!lines.contains(y)) {
						lines.add(y);
					}
				}
			}
		}
		
		int[] sortedLines = sortLines(lines);
		return sortedLines;
	}
	
	private static int[] sortLines(ArrayList<Integer> lines) {
		int[] sortedLines = new int[lines.size()];
		for (int i = 0; i < sortedLines.length; i++) {
			sortedLines[i] = lines.get(i);
		}
		
		for (int i = 0; i < sortedLines.length; i++) {
			for (int j = i + 1; j < sortedLines.length; j++) {
				if (sortedLines[i] > sortedLines[j]) {
					int temp = sortedLines[i];
					sortedLines[i] = sortedLines[j];
					sortedLines[j] = temp;
				}
			}
		}
		return sortedLines;
	}

	private boolean checkLine(int height){
		for(int i = 0; i < gridWidth; i++){
			if(!checkGrid(i, height))
				return false;
		}
		return true;
	}

	void updateGridAfterLineRemoval(int[] linesToRemove) {
		for (int line:linesToRemove) {
			for (int y = line; y >= 0; y--) {
				for (int x = 0; x < gridWidth; x++) {
					if (y == 0) {
						grid[y][x] = false;
					} else {
						grid[y][x] = grid[y - 1][x];	
					}
				}
			}
		}
	}

	boolean checkGrid(Point point) {
		return checkGrid(point.x, point.y);
	}
	
	private boolean checkGrid(int width, int height){
		if (width >= 0 && width < gridWidth && height >= 0 && height < gridHeight) {
			return grid[height][width];
		}
		return true;
	}
	
	Greeting moveGreeting(MoveMessage message, TetrisBlock currentBlock, TetrisBlock nextBlock) {
		Point move = new Point(message.getX(), message.getY());
		
		Point[] currentPositions = currentBlock.getCurrentPositions();
		Point[] newPositions = TetrisMaster.addPointToArray(move, currentPositions);
		for(int i = 0; i < newPositions.length; i++){
			if((message.getX() != 0 && checkGrid(newPositions[i]))){
				return new MoveGreeting("can't move");
			}
			if(newPositions[i].y == grid.length || checkGrid(newPositions[i])){
				int[] lines = updateGrid(currentPositions);
				if (lines.length > 0) {
					updateGridAfterLineRemoval(lines);
					score += lines.length;
					return new LineGreeting("clearLines", lines, score);
				}
				
				return null;
			}
		}
		currentBlock.setCurrentPosition(TetrisMaster.addPointToPoint(currentBlock.getCurrentPosition(), move));
		currentBlock.setCurrentPositions(newPositions);
		return new MoveGreeting("continue", currentPositions, newPositions, currentBlock.getColor(), 
				nextBlock.getOrientations()[nextBlock.getCurrentOrientation()], nextBlock.getColor());
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}
}
