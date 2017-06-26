package com.youngcapital.tetris.complete.websocket;

import java.awt.Point;

import com.youngcapital.tetris.complete.block.Pos;

public class Greeting {
	private String content;
	private Point[] grayPositions;
	private Point[] colorPositions;
	private String color;
	
	public Greeting() {}

	public Greeting(String content) {
		this.content = content;
	}

	public Greeting(Point[] grayPositions, Point[] colorPositions, String color) {
		this("no comment");
		this.grayPositions = grayPositions;
		this.colorPositions = colorPositions;
		this.color = color;
	}

	public Greeting(String content, Point[] curPos, Point[] newPos, String color) {
		this.content = content;
		this.grayPositions = curPos;
		this.colorPositions = newPos;
		this.color = color;
	}

	public String getContent() {
		return content;
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
