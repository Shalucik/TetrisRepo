package com.youngcapital.tetris.complete.websocket;

import java.awt.Point;

public class MoveGreeting extends Greeting{
	private Point[] grayPositions;
	private Point[] colorPositions;
	private String color;
	

	public MoveGreeting(String content) {
		super(content, false);
	}

	public MoveGreeting(Point[] grayPositions, Point[] colorPositions, String color) {
		this("no comment");
		this.grayPositions = grayPositions;
		this.colorPositions = colorPositions;
		this.color = color;
	}

	public MoveGreeting(String content, Point[] curPos, Point[] newPos, String color) {
		this(content);
		this.grayPositions = curPos;
		this.colorPositions = newPos;
		this.color = color;
	}

	public Point[] getGrayPositions() {
		return grayPositions;
	}

	public Point[] getColorPositions() {
		return colorPositions;
	}

	public String getColor() {
		return color;
	}

	
}
