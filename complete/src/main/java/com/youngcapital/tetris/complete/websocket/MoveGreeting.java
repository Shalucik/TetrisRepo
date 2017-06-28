package com.youngcapital.tetris.complete.websocket;

import java.awt.Point;

public class MoveGreeting extends Greeting{
	private Point[] grayPositions;
	private Point[] colorPositions;
	private Point[] nextPositions;
	private String nextColor;
	private String color;
	
	
	public Point[] getNextPositions() {
		return nextPositions;
	}


	public String getNextColor() {
		return nextColor;
	}
	

	public MoveGreeting(String content) {
		super(content, 0);
	}
	
	public MoveGreeting(String content, Point[] nextPositions, String nextColor) {
		super(content, 0);
		this.nextPositions = nextPositions;
		this.nextColor = nextColor;
	}

	public MoveGreeting(Point[] grayPositions, Point[] colorPositions, String color, Point[] nextPositions, String nextColor) {
		this("no comment");
		this.grayPositions = grayPositions;
		this.colorPositions = colorPositions;
		this.nextPositions = nextPositions;
		this.nextColor = nextColor;
		this.color = color;
	}

	public MoveGreeting(String content, Point[] curPos, Point[] newPos, String color, Point[] nextPositions, String nextColor) {
		this(content);
		this.grayPositions = curPos;
		this.colorPositions = newPos;
		this.nextPositions = nextPositions;
		this.nextColor = nextColor;
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
