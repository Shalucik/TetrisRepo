package com.youngcapital.tetris.complete.block;

import java.awt.Point;

public class TetrisBlock {	
	private Point currentPosition;
	private Point[] currentPositions;
	private Point[][] orientations;
	private int currentOrientation;
	private String color;
	
	public TetrisBlock() {
	}


	public TetrisBlock(Point currentPosition, Point[][] orientations, int currentOrientation,  Point[] currentPositions, String color) {
		this.currentPosition = currentPosition;
		this.orientations = orientations;
		this.currentOrientation = currentOrientation;
		this.currentPositions = currentPositions;
		this.color = color;
	}

	public Point getCurrentPosition() {
		return currentPosition;
	}


	public void setCurrentPosition(Point currentPosition) {
		this.currentPosition = currentPosition;
	}


	public Point[][] getOrientations() {
		return orientations;
	}


	public void setOrientations(Point[][] orientations) {
		this.orientations = orientations;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public int getCurrentOrientation() {
		return currentOrientation;
	}


	public void setCurrentOrientation(int currentOrientation) {
		this.currentOrientation = currentOrientation;
	}

	public Point[] getCurrentPositions() {
		return currentPositions;
	}


	public void setCurrentPositions(Point[] currentPositions) {
		this.currentPositions = currentPositions;
	}
	
	
}
